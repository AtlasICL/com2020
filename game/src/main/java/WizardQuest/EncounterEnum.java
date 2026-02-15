package WizardQuest;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterEnum {
    GOBLIN_ENCOUNTER(new EntityEnum[]{EntityEnum.GOBLIN}, "GoblinEncounter"),
    FISHMAN_ENCOUNTER(new EntityEnum[]{EntityEnum.FISH_MAN}, "FishManEncounter");

    private final EntityEnum[] enemies;
    private final String telemetryName;

    /**
     * Constructor for EncounterEnum
     *
     * @param enemies array of EntityType objects that appear occur an encounter
     * @param telemetryName the name used for telemetry events
     */
    private EncounterEnum(EntityEnum[] enemies, String telemetryName) {
        this.enemies = enemies;
        this.telemetryName = telemetryName;
    }

    /**
     * Gets the types of enemies that occur during this Encounter
     *
     * @return an array of EntityType objects
     */
    public EntityEnum[] getEnemies() {
        return enemies;
    }

    /**
     * This creates a new instance of an Encounter of this type
     * Uses EntityType reflection for the instantiation of the actual encounters.
     * @return a new EncounterInterface with specified enemies
     */
    public EncounterInterface createEncounter(DifficultyEnum difficulty) {
        return new Encounter(this, difficulty);
    }

    /**
     * This method gets the telemetry name for this encounter
     * @return tje class object for this entity
     */
    public String getTelemetryName() {
        return telemetryName;
    }

}
