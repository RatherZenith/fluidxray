package io.github.ratherzenith.fluidxray;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Registers the key bind. This runs on the mod event bus, so it lives in its own subscriber class.
 *
 * <p>The displayed name and category come from {@code assets/fluidxray/lang/en_us.json}.</p>
 */
@Mod.EventBusSubscriber(modid = FluidXray.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FluidXrayKeyBindings {

    public static final String CATEGORY = "key.categories.fluidxray";

    /** Cycles off -> default -> killFog. Default key: G. */
    public static final KeyMapping TOGGLE = new KeyMapping(
            "key.fluidxray.toggle",
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY);

    private FluidXrayKeyBindings() {
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE);
    }
}
