package io.github.ratherzenith.fluidxray;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

/**
 * Registers the key bind. {@link RegisterKeyMappingsEvent} is a mod bus event, so NeoForge automatically
 * routes this subscriber to the mod event bus (the {@code bus} attribute of {@link EventBusSubscriber} is
 * ignored since 1.21.1).
 *
 * <p>The displayed name and category come from {@code assets/fluidxray/lang/en_us.json}.</p>
 */
@EventBusSubscriber(modid = FluidXray.MODID, value = Dist.CLIENT)
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
