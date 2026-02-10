package WizardQuest;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterEnum {
    ENCOUNTERTYPE_1(null, "Name TBC"), // PLACEHOLDER
    ENCOUNTERTYPE_2(null, "Name TBC"), // PLACEHOLDER
    ENCOUNTERTYPE_3(null, "Name TBC"); // PLACEHOLDER

    private final EntityEnum[] enemies;
    private final String telemetryName;

    private EncounterEnum(EntityEnum[] enemies, String telemetryName) {
        this.enemies = enemies;
        this.telemetryName = telemetryName;
    }

    public EntityEnum[] getEnemies() {
        return enemies;
    }

    public Encounter createEncounter() {
        return null; // PLACEHOLDER
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}
