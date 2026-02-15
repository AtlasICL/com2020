package WizardQuest;

import java.util.Random;

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
     * Internal Random Entity AI class, picks a random ability or upgrade when the
     * relevant method is used.
     */
    private static class RandomEntityAI implements EntityAIInterface {
        private Random random;

        public RandomEntityAI() {
            this.random = new Random();
        }

        @Override
        public EntityInterface pickTarget(EntityInterface[] enemies) {
            if (enemies == null || enemies.length == 0)
                return null;
            shuffleArray(enemies);
            for (EntityInterface e : enemies) {
                if (e.getHealth() > 0) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public AbilityEnum pickAbility(EntityInterface self) {
            AbilityEnum[] abilities = self.getAbilities().toArray(new AbilityEnum[0]);
            if (abilities == null || abilities.length == 0)
                return null;
            shuffleArray(abilities);
            
            // If player check that they have enough magic to use the ability.
            if (self instanceof PlayerInterface){
                PlayerInterface player = (PlayerInterface) self;
                for (AbilityEnum a: abilities){
                    if(a.getMagicCost() <= player.getMagic()){
                        return a;
                    }
                }
            }
            // Enemies don't use magic
            return abilities[0];
        }

        @Override
        public UpgradeEnum pickUpgrade(UpgradeEnum[] upgrades, int coins) {
            if (upgrades == null || upgrades.length == 0)
                return null;
            shuffleArray(upgrades);
            for (UpgradeEnum u : upgrades) {
                if (u.getPrice() <= coins) {
                    return u;
                }
            }
            return null;
        }

        // Fisher-Yates shuffling algorithm used to randomise a given array.
        private <T> void shuffleArray(T[] arr) {
            for (int i = arr.length - 1; i > 0; i--) {
                int j = this.random.nextInt(i + 1);
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }
}
