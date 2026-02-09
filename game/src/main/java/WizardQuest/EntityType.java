package WizardQuest;

/**
 * Enumerates all entity types in the game.
 */

public enum EntityType {
    PLAYER(Player.class, "Player"),
    GOBLIN(null, "Goblin"), // PLACEHOLDER ARGS
    FISH_MAN(null, "Fish Man"), // PLACEHOLDER ARGS
    PYROMANCER(null, "Pyromancer"), // PLACEHOLDER ARGS
    EVIL_WIZARD(null, "Evil Wizard"), // PLACEHOLDER ARGS
    ARMOURED_GOBLIN(null, "Armoured Goblin"), // PLACEHOLDER ARGS
    GHOST(null, "Ghost"), // PLACEHOLDER ARGS
    BLACK_KNIGHT(null, "Black Knight"), // PLACEHOLDER ARGS
    DRAGON(null, "Dragon"),; // PLACEHOLDER ARGS

    private final Class<? extends EntityInterface> enemyClass;
    private final String telemetryName;

    private EntityType(Class<? extends EntityInterface> enemyClass, String telemetryName) {
        this.enemyClass = enemyClass;
        this.telemetryName = telemetryName;
    }

    public EntityInterface createEnemy() {
        return null; // PLACEHOLDER
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}

