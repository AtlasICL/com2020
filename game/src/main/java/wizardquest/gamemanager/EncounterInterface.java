package wizardquest.gamemanager;

import wizardquest.entity.EntityInterface;

/**
 * Interface for encounters. Stores the enemies within the encounter and
 * provides methods to interface with them.
 */
public interface EncounterInterface {
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
    public EncounterEnum getType();

    /**
     * Resets the health of all enemies in the encounter. Should be called when the
     * encounter is retried.
     */
    public void resetEnemyHealth();
}
