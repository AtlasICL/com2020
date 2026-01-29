package WizardQuest;

/**
 * Interface for abilities. Stores their description and type, and holds a method for execution.
 */
public interface AbilityInterface {
    /**
     * Called by a source entity executing their ability on a single target.
     *
     * @param source the entity executing the ability.
     * @param target the entity that is subject to an attack with this ability.
     */
    public void execute(EntityInterface source, EntityInterface target);

    /**
     * Get the description of what the ability is.
     *
     * @return the ability description.
     */
    public String getDescription();

    /**
     * Get the type of the ability.
     *
     * @return the ability type.
     */
    public AbilityType getType();
}
