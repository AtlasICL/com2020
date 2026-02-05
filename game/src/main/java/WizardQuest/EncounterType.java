package WizardQuest;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterType {
    ENCOUNTERTYPE_1(null, "Name TBC"), // PLACEHOLDER
    ENCOUNTERTYPE_2(null, "Name TBC"), // PLACEHOLDER
    ENCOUNTERTYPE_3(null, "Name TBC"); // PLACEHOLDER

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
        return null; // PLACEHOLDER
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}
