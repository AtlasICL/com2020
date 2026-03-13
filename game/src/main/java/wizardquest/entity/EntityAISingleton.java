package wizardquest.entity;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.UpgradeEnum;

import wizardquest.gamemanager.Utils;

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
        @Override
        public EntityInterface pickTarget(EntityInterface[] enemies) {
            if (enemies == null || enemies.length == 0)
                return null;
            Utils.shuffleArray(enemies);
            for (EntityInterface e : enemies) {
                if (!Utils.isDead(e)) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public AbilityEnum pickAbility(EntityInterface self) {
            final AbilityEnum[] abilities = self.getAbilities().toArray(AbilityEnum[]::new);
            if (abilities == null || abilities.length == 0)
                return null;
            Utils.shuffleArray(abilities);

            // If player check that they have enough magic to use the ability.
            if (self instanceof PlayerInterface player) {
                for (AbilityEnum a : abilities) {
                    if (a.getMagicCost() <= player.getMagic()) {
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
            Utils.shuffleArray(upgrades);
            for (UpgradeEnum u : upgrades) {
                if (u.getPrice() <= coins) {
                    return u;
                }
            }
            return null;
        }
    }
}
