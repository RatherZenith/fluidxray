package io.github.ratherzenith.fluidxray;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Persists the selected mode as a tiny text file in the config directory, so the last mode chosen is restored
 * on the next launch.
 *
 * <p>All failure paths are swallowed on purpose: a missing or unreadable file simply falls back to the default
 * mode, and a failed write must never break the game.</p>
 */
final class FluidXrayStorage {

    private static final String FILE_NAME = "fluidxray-client.txt";

    private FluidXrayStorage() {
    }

    /**
     * @return the saved mode, or {@code null} when there is nothing (valid) on disk
     */
    static FluidXrayMode read() {
        try {
            Path path = configPath();
            if (!Files.isRegularFile(path)) {
                return null;
            }
            return FluidXrayMode.byId(Files.readString(path, StandardCharsets.UTF_8).trim());
        } catch (Exception e) {
            return null;
        }
    }

    static void write(FluidXrayMode mode) {
        try {
            Path path = configPath();
            Files.createDirectories(path.getParent());
            Files.writeString(path, mode.getId(), StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            // Not being able to remember the mode is not worth a crash.
        }
    }

    private static Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
    }
}
