package WizardQuest;

/**
 * Interface for the Entity AI.
 */
public interface EntityAIInterface {
    /**
     * Called when an Entity AI uses one of their owned abilities during an encounter.
     *
     * @param abilities the array of abilities available for use by the calling entity.
     * @param self      the object calling this method.
     * @param allies    the array of entities that are friendly to the calling entity in this encounter.
     * @param enemies   the array of entities that are hostile to the calling entity in this encounter.
     */
    public void useAbility(AbilityInterface[] abilities, EntityInterface self, EntityInterface[] allies, EntityInterface[] enemies);

    /**
     * Called when an Entity AI attempts to purchase an upgrade from the shop between encounters.
     *
     * @param upgrades the array of upgrades available to the entity for purchase.
     * @param coins    the total coins that the entity currently has.
     *                 Each upgrade requires a specific total of coins, an entity may only purchase what they can afford.
     * @return the single selected upgrade.
     */
    public UpgradeType pickUpgrade(UpgradeType[] upgrades, int coins);
}



