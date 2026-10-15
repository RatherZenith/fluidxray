package io.github.ratherzenith.fluidxray;

/**
 * Holds the currently selected {@link FluidXrayMode}.
 *
 * <p>The mode is cached to disk (see {@link FluidXrayStorage}) and restored the first time it is needed, so the
 * last mode you switched to is used again on the next launch. The state class itself stays free of any
 * Minecraft references so that it can safely be touched from inside a Mixin; the disk access lives in
 * {@link FluidXrayStorage}, which is only class-loaded when the state is first touched.</p>
 */
public final class FluidXrayState {

    private static volatile FluidXrayMode mode = FluidXrayMode.DEFAULT;
    private static volatile boolean loaded = false;

    private FluidXrayState() {
    }

    public static FluidXrayMode getMode() {
        if (!loaded) {
            load();
        }
        return mode;
    }

    private static synchronized void load() {
        if (loaded) {
            return;
        }
        FluidXrayMode saved = FluidXrayStorage.read();
        if (saved != null) {
            mode = saved;
        }
        loaded = true;
    }

    public static void setMode(FluidXrayMode newMode) {
        if (newMode == null) {
            return;
        }
        mode = newMode;
        loaded = true;
        FluidXrayStorage.write(newMode);
    }

    /**
     * @return the new mode
     */
    public static FluidXrayMode cycle() {
        FluidXrayMode next = getMode().next();
        setMode(next);
        return next;
    }
}
