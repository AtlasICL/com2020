package WizardQuest;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterType {
    ENCOUNTERTYPE_1(),
    ENCOUNTERTYPE_2(),
    ENCOUNTERTYPE_3();

    private final EntityType[] enemies;
    private final String telemetryName;

    private EncounterType(EntityType[] enemies, String telemetryName) {
        this.enemies = enemies;
        this.telemetryName = telemetryName;
    }

    public EntityType[] getEnemies() {
        return enemies;
    }

    public Encounter createEncounter() {
        return null;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}
