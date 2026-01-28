package WizardQuest;

/**
 * Provides global access to the random entity AI.
 */
public class EntityAISingleton {
    private static EntityAIInterface RandomEntityAI = new RandomEntityAI();

    private EntityAISingleton() {
    }

    /**
     * Provides a reference to the random entity AI.
     *
     * @return a reference to the entity AI singleton.
     */
    public static EntityAIInterface getEntityAI() {
        return RandomEntityAI;
    }

    /**
     * Internal Random Entity AI class, picks a random ability or upgrade when the relevant method is used.
     */
    private static class RandomEntityAI implements EntityAIInterface {

        public RandomEntityAI() {
        }

        @Override
        public void useAbility(AbilityInterface[] abilities, EntityInterface self, EntityInterface[] allies, EntityInterface[] enemies) {}

        @Override
        public UpgradeType pickUpgrade(UpgradeType[] upgrades, int coins) {return null;}
    }
}

