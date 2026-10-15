package io.github.ratherzenith.fluidxray;

/**
 * The three behaviour modes of the mod.
 */
public enum FluidXrayMode {

    /** Feature disabled: fluids and fog render exactly like vanilla. */
    OFF("off"),

    /**
     * Default mode: only the fluid faces that touch air are rendered, and the dense liquid fog
     * (water / lava) plus the underwater screen overlay are removed.
     */
    DEFAULT("default"),

    /**
     * Same face culling as {@link #DEFAULT}, but <b>every</b> kind of fog is disabled: distance fog,
     * sky fog, water / lava fog, powder snow fog and the mob effect (blindness / darkness) fog.
     */
    KILL_FOG("killFog");

    private final String id;

    FluidXrayMode(String id) {
        this.id = id;
    }

    /**
     * @return the id shown to the player and used as the command literal, e.g. {@code "killFog"}
     */
    public String getId() {
        return id;
    }

    /**
     * @return true when fluid faces should be culled down to the ones exposed to air
     */
    public boolean cullsFluidFaces() {
        return this == DEFAULT || this == KILL_FOG;
    }

    /**
     * @return true when the dense liquid fog (water / lava) is removed as well
     */
    public boolean removesLiquidFog() {
        return this != OFF;
    }

    /**
     * @return true when every kind of fog is removed
     */
    public boolean removesEveryFog() {
        return this == KILL_FOG;
    }

    /**
     * @return the next mode in the cycle order (off -&gt; default -&gt; killFog -&gt; off)
     */
    public FluidXrayMode next() {
        FluidXrayMode[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    /**
     * @return the mode matching {@code id} (case-insensitive), or {@code null}
     */
    public static FluidXrayMode byId(String id) {
        for (FluidXrayMode mode : values()) {
            if (mode.id.equalsIgnoreCase(id)) {
                return mode;
            }
        }
        return null;
    }
}
