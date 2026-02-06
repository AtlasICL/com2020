package WizardQuest;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base Enemy class for all the enemies that occur in the game
 * This class provides ways to calculate health and damage caused
 * Should be extended by Concrete enemy classes with their own definitions of stats, abilities and entity type
 */
public abstract class Enemy implements EntityInterface {
    private int health;
    private int maxHealth;
    private List<AbilityType> abilities;

    /**
     * Enemy Constructor.
     * The base values from settings are adjusted based on difficulty levels.
     */
    public Enemy() {
        this.abilities = new ArrayList<>();
        this.maxHealth = 100;
        this.health = maxHealth;
    }

    /**
     * Constructor with specified max health.
     * @param maxHealth the maximum health for this enemy
     */
    public Enemy(int maxHealth){
        this.abilities = new ArrayList<>();
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

        int realDamage = calcDamage(amount, type);
        this.health = Math.max(0, this.health - realDamage); // s.t health never goes below 0
    }
    @Override
    public int calcDamage(int base, DamageType type){
          //to be implemented
        return 0; // PLACEHOLDER
    }

    @Override
    public List<AbilityType> getAbilities() {
        return abilities;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    @Override
    public void resetHealth(){
        this.health = this.maxHealth;
    }

    /**
     * Sets the maximum health.
     * This gets called within the constructor of concrete enemy classes
     * @param maxHealth defines the maximum health
     */
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
}
