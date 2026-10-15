package io.github.ratherzenith.fluidxray;

import net.neoforged.fml.common.Mod;

/**
 * Entry point of Fluid X-ray.
 *
 * <p>The mod is client side only. All of its content lives behind client-gated hooks: the event subscribers
 * ({@link ClientEvents} on the game bus, {@link FluidXrayKeyBindings} on the mod bus) are annotated with
 * {@code value = Dist.CLIENT}, and the Mixin is declared in the {@code "client"} section of
 * {@code fluidxray.mixins.json}. Nothing client specific is ever loaded on a dedicated server, and
 * {@code displayTest = "IGNORE_ALL_VERSION"} keeps it from showing a version mismatch.</p>
 */
@Mod(FluidXray.MODID)
public class FluidXray {

    public static final String MODID = "fluidxray";

    public FluidXray() {
        // Nothing to do here. Hooks are registered through @EventBusSubscriber and the Mixin
        // configuration (fluidxray.mixins.json).
    }
}
