package WizardQuest;

public class Encounter implements EncounterInterface {
    private EntityInterface[] enemies;
    private boolean completed;
    private EncounterType type;

    /**
     * Builds the encounter based on the specified type
     * 
     * @param type the name for the encounter.
     */
    public Encounter(EncounterType type);

    /**
     * Provides a reference to all enemies in the encounter.
     * 
     * @return an array of references to the enemies in the encounter.
     */
    public EntityInterface[] getEnemies();

    /**
     * Gives whether the encounter has been marked as complete or not.
     * 
     * @return Whether the encounter is complete.
     */
    public boolean isComplete();

    /**
     * Marks the encounter as complete, meaning all entities within it are dead.
     */
    public void markComplete();

    /**
     * returns the type (name) of the encounter.
     * 
     * @return the encounter's type.
     */
    public EncounterType getType();

    /**
     * Resets the health of all enemies in the encounter. Should be called when the
     * encounter is retried.
     */
    public void resetEnemyHealth();
}
