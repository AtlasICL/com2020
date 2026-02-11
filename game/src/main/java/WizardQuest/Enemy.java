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
    private float physicalDamageModifier = 1.0f;
    private float fireDamageModifier = 1.0f;
    private float waterDamageModifier = 1.0f;
    private float thunderDamageModifier = 1.0f;

    @Override
    public abstract List<AbilityType> getAbilities();

    /**
     * Constructor with specified max health.
     * @param maxHealth the maximum health for this enemy
     */
    public Enemy(int maxHealth){
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

    /**
     * Calculates the total damage dealt after applying enemy's modifier for the damage type given. Absolute damage ignores modifiers
     *
     * @param base the base damage dealt by the entity's attack.
     * @param type the damage type of the entity's attack.
     * @return the total damage inflicted by this attack.
     */
    @Override
    public int calcDamage(int base, DamageType type){
        float modifier =  switch(type){
            case WATER -> waterDamageModifier;
            case PHYSICAL -> physicalDamageModifier;
            case FIRE -> fireDamageModifier;
            case THUNDER -> thunderDamageModifier;
            case ABSOLUTE -> 1.0f;
        };
        return Math.round(base * modifier);
    }


    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    @Override
    public void resetHealth(){
        this.health = this.maxHealth;
    }


    public void setPhysicalDamageModifier(float physicalDamageModifier) {
        this.physicalDamageModifier = physicalDamageModifier;
    }

    public void setFireDamageModifier(float fireDamageModifier) {
        this.fireDamageModifier = fireDamageModifier;
    }

    public void setWaterDamageModifier(float waterDamageModifier) {
        this.waterDamageModifier = waterDamageModifier;
    }

    public void setThunderDamageModifier(float thunderDamageModifier) {
        this.thunderDamageModifier = thunderDamageModifier;
    }


}
