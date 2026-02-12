package WizardQuest;

import java.util.List;

/**
 * Abstract base Enemy class for all the enemies that occur in the game
 * This class provides ways to calculate health and damage caused
 * Should be extended by Concrete enemy classes with their own definitions of stats, abilities and entity type
 */
public abstract class EnemyBase implements EntityInterface {
    private int health;
    private int maxHealth;

    @Override
    public abstract List<AbilityType> getAbilities();

    /**
     * Constructor with specified max health.
     * @param maxHealth the maximum health for this enemy
     */
    public EnemyBase(int maxHealth){
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }
    @Override
    public int getHealth() {
        return health;
    }
    @Override
    public void loseHealth(int amount, DamageType type) throws IllegalArgumentException{
        if (amount < 0){ throw new IllegalArgumentException("Damage amount cannot be negative!!!");}
        this.health = Math.max(0, this.health - amount); // s.t health never goes below 0
    }

    /**
     * Calculates the total damage dealt after applying enemy's modifier for the damage type given. Absolute damage ignores modifiers
     *
     * @param base the base damage dealt by the entity's attack.
     * @param type the damage type of the entity's attack.
     * @return the total damage inflicted by this attack.
     */
    @Override
    public abstract int calcDamage(int base, DamageType type);


    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    @Override
    public void resetHealth(){
        this.health = this.maxHealth;
    }
}
