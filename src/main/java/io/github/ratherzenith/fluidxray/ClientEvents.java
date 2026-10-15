package io.github.ratherzenith.fluidxray;

import io.github.ratherzenith.fluidxray.command.FluidXrayCommand;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

/**
 * Game bus hooks. Everything here only touches client-only classes, which is fine because the whole mod is
 * restricted to {@link Dist#CLIENT}.
 */
@EventBusSubscriber(modid = FluidXray.MODID, value = Dist.CLIENT)
public final class ClientEvents {

    /**
     * Pushes the fog planes far past the far clip plane. Combined with {@code setCanceled(true)} this removes
     * the fog: NeoForge only writes the event's plane distances to the shader when the event is cancelled.
     */
    private static final float FOG_NEAR = 1.0E6F;
    private static final float FOG_FAR = 2.0E6F;

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        FluidXrayCommand.register(event);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (FluidXrayKeyBindings.TOGGLE.consumeClick()) {
            FluidXrayClient.showMode(FluidXrayClient.cycleMode());
        }
    }

    /**
     * {@code default} removes the dense liquid fog; {@code killFog} removes every fog type and both fog modes,
     * which also covers distance fog, sky fog, powder snow and the blindness / darkness effect fog.
     */
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        FluidXrayMode mode = FluidXrayState.getMode();
        if (!mode.removesLiquidFog()) {
            return;
        }
        if (mode.removesEveryFog()
                || event.getType() == FogType.WATER
                || event.getType() == FogType.LAVA) {
            event.setNearPlaneDistance(FOG_NEAR);
            event.setFarPlaneDistance(FOG_FAR);
            event.setCanceled(true);
        }
    }

    /**
     * Removes the blue "underwater" texture that is drawn over the whole screen while your eyes are in water.
     */
    @SubscribeEvent
    public static void onBlockScreenEffect(RenderBlockScreenEffectEvent event) {
        if (!FluidXrayState.getMode().removesLiquidFog()) {
            return;
        }
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.WATER) {
            event.setCanceled(true);
        }
    }
}
