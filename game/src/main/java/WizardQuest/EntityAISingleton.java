package WizardQuest;

/**
 * Provides global access to the random entity AI.
 */
public class EntityAISingleton {
    private static final EntityAIInterface randomEntityAI = new RandomEntityAI();

    private EntityAISingleton() {
    }

    /**
     * Provides a reference to the random entity AI.
     *
     * @return a reference to the entity AI singleton.
     */
    public static EntityAIInterface getInstance() {
        return randomEntityAI;
    }

    /**
     * Internal Random Entity AI class, picks a random ability or upgrade when the relevant method is used.
     */
    private static class RandomEntityAI implements EntityAIInterface {

        public RandomEntityAI() {
        }

        @Override
        public void useAbility(AbilityEnum[] abilities, EntityInterface self, EntityInterface[] enemies) {
            if (abilities == null || abilities.length == 0) return;
            if (self == null) return;
            if (enemies == null || enemies.length == 0) return;

            //makes player target
            EntityInterface target = null;

            for (EntityInterface e : enemies) {
                if (e != null && e.getHealth() > 0) {
                    target = e;
                    break;
                }
            }

            if (target == null) return;

            //pick ability at random
            AbilityEnum chosen =
                    abilities[(int)(Math.random() * abilities.length)];

            //execute attack
            try {
                chosen.execute(self, target);
            }
            catch (LackingResourceException ignored) {
                //placeholder
            }
        }

        @Override
        public UpgradeEnum pickUpgrade(UpgradeEnum[] upgrades, int coins) {return null;} // PLACEHOLDER
    }
}

