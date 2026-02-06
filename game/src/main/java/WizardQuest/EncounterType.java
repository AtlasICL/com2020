package WizardQuest;

/**
 * Enumerates all encounter types in the game.
 */
public enum EncounterType {
    GOBLIN_ENCOUNTER(new EntityType[]{EntityType.GOBLIN}, "GoblinEncounter"),
    FISHMAN_ENCOUNTER(new EntityType[]{EntityType.FISH_MAN}, "FishManEncounter");

    private final EntityType[] enemies;
    private final String telemetryName;

    /**
     * Constructor for EncounterType
     *
     * @param enemies array of EntityType objects that appear occur an encounter
     * @param telemetryName the name used for telemetry events
     */
    private EncounterType(EntityType[] enemies, String telemetryName) {
        this.enemies = enemies;
        this.telemetryName = telemetryName;
    }

    /**
     * Gets the types of enemies that occur during this Encounter
     *
     * @return an array of EntityType objects
     */
    public EntityType[] getEnemies() {
        return enemies;
    }

    /**
     * This creates a new instance of an Encounter of this type
     * Uses EntityType reflection for the instantiation of the actual encounters.
     * @return a new Encounter with specified enemies
     */
    public Encounter createEncounter() {
        return new Encounter(this);
    }

    /**
     * This method gets the telemetry name for this encounter
     * @return tje class object for this entity
     */
    public String getTelemetryName() {
        return telemetryName;
    }

    /**
     * Gets the number of enemies during an encounter
     * @return the enemy count
     */
    public int getEnemyCount(){
        return enemies.length;
    }
}
