package io.github.ratherzenith.fluidxray;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/**
 * Small client helpers shared by the command and the key bind.
 */
public final class FluidXrayClient {

    private FluidXrayClient() {
    }

    /**
     * Applies a mode and forces every loaded chunk to be re-tessellated so the change takes effect
     * immediately (this is the same refresh that F3+A performs).
     */
    public static FluidXrayMode applyMode(FluidXrayMode mode) {
        FluidXrayState.setMode(mode);
        rebuildChunks();
        return FluidXrayState.getMode();
    }

    public static FluidXrayMode cycleMode() {
        FluidXrayState.cycle();
        rebuildChunks();
        return FluidXrayState.getMode();
    }

    public static void rebuildChunks() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.levelRenderer != null) {
            minecraft.levelRenderer.allChanged();
        }
    }

    /**
     * Shows the current mode above the hotbar. Does nothing outside of a world.
     */
    public static void showMode(FluidXrayMode mode) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(Component.literal("Fluid X-ray: " + mode.getId()), true);
        }
    }
}
