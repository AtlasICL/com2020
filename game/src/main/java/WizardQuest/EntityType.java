package WizardQuest;

/**
 * Enumerates all entity types in the game.
 */

public enum EntityType {
    ENTITYTYPE_1(),
    ENTITYTYPE_2(),
    ENTITYTYPE_3();

    private final Class<? extends EntityInterface> enemyClass;
    private final String telemetryName;

    private EntityType(Class<? extends EntityInterface> enemyClass, String telemetryName) {
        this.enemyClass = enemyClass;
        this.telemetryName = telemetryName;
    }

    public EntityInterface createEnemy() {
        return null;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}

