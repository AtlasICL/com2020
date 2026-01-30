package WizardQuest;

/**
 * Provides global access to the random entity AI.
 */
public class EntityAISingleton {
    private static EntityAIInterface randomEntityAI = new RandomEntityAI();

    private EntityAISingleton() {
    }

    /**
     * Provides a reference to the random entity AI.
     *
     * @return a reference to the entity AI singleton.
     */
    public static EntityAIInterface getEntityAI() {
        return randomEntityAI;
    }

    /**
     * Internal Random Entity AI class, picks a random ability or upgrade when the relevant method is used.
     */
    private static class RandomEntityAI implements EntityAIInterface {

        public RandomEntityAI() {
        }

        @Override
        public void useAbility(AbilityType[] abilities, EntityInterface self, EntityInterface[] enemies) {}

        @Override
        public UpgradeType pickUpgrade(UpgradeType[] upgrades, int coins) {return null;} // PLACEHOLDER
    }
}

