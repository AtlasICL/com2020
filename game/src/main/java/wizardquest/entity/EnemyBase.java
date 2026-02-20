package wizardquest.entity;

import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;

/**
 * Abstract base Enemy class for all the enemies that occur in the game
 * This class provides ways to calculate health and damage caused
 * Should be extended by Concrete enemy classes with their own definitions of
 * stats, abilities and entity type
 */
public abstract class EnemyBase implements EntityInterface {
    private int health;
    private final int maxHealth;

    @Override
    public abstract List<AbilityEnum> getAbilities();

    /**
     * Constructor with specified max health.
     * 
     * @param maxHealth the maximum health for this enemy
     */
    public EnemyBase(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage amount cannot be negative");
        }
        this.health = Math.max(0, this.health - amount); // s.t health never goes below 0
    }

    @Override
    public int calcDamage(int base, DamageEnum type) {
        return base;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void resetHealth() {
        this.health = this.maxHealth;
    }
}
