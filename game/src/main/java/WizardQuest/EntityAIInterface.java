package WizardQuest;

/**
 * Interface for the Entity AI.
 */
public interface EntityAIInterface {
    /**
     * Picks which ability to use and returns it, validating it has enough magic to
     * do so in the case of a simulated player.
     * 
     * @param self the creature that will use the ability.
     * @return a reference to the chosen ability.
     */
    public AbilityEnum pickAbility(EntityInterface self);

    /**
     * Picks which enemy to target with an ability
     * 
     * @param enemies the enemies of the entity
     * @return a reference to the chosen target.
     */
    public EntityInterface pickTarget(EntityInterface[] enemies);

    /**
     * Called when an Entity AI attempts to purchase an upgrade from the shop
     * between encounters.
     *
     * @param upgrades the array of upgrades available to the entity for purchase.
     * @param coins    the total coins that the entity currently has.
     *                 Each upgrade requires a specific total of coins, an entity
     *                 may only purchase what they can afford.
     * @return the single selected upgrade.
     */
    public UpgradeEnum pickUpgrade(UpgradeEnum[] upgrades, int coins);
}
