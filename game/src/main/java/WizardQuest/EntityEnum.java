package WizardQuest;

/**
 * Enumerates all entity types in the game.
 */

public enum EntityEnum {
    PLAYER(Player.class, "Player"),
    GOBLIN(null, "Goblin"), // PLACEHOLDER ARGS
    FISH_MAN(null, "Fish Man"), // PLACEHOLDER ARGS
    PYROMANCER(null, "Pyromancer"), // PLACEHOLDER ARGS
    EVIL_WIZARD(null, "Evil Wizard"), // PLACEHOLDER ARGS
    ARMOURED_GOBLIN(null, "Armoured Goblin"), // PLACEHOLDER ARGS
    GHOST(null, "Ghost"), // PLACEHOLDER ARGS
    BLACK_KNIGHT(null, "Black Knight"), // PLACEHOLDER ARGS
    DRAGON(null, "Dragon"),; // PLACEHOLDER ARGS

    private final Class<? extends EntityInterface> entityClass;
    private final String telemetryName;

    private EntityEnum(Class<? extends EntityInterface> entityClass, String telemetryName) {
        this.entityClass = entityClass;
        this.telemetryName = telemetryName;
    }

    /**
     * Creates an instance of the entity for this enum
     * @return an instance of the entity corresponding to the enum value.
     * @throws IllegalStateException if reflection fails.
     */
    public EntityInterface createEntity() throws IllegalStateException{
        return null; // PLACEHOLDER
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}

