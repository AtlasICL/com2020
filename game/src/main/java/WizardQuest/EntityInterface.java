package WizardQuest;

import java.util.List;

/**
 * Interface for entities.
 * Provides methods for damage calculation and health management.
 */
public interface EntityInterface {
    /**
     * Decrements an entity's health by a given amount, as a result of a certain
     * damage type being inflicted upon them.
     *
     * @param amount the total health that the entity will lose.
     * @param type the inflicted damage type that has caused this health loss.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void loseHealth(int amount, DamageEnum type) throws IllegalArgumentException;

    /**
     * Gets an entity's current health.
     *
     * @return the entity's current health.
     */
    public int getHealth();

    /**
     * Gets an entity's maximum health.
     *
     * @return the entity's maximum health.
     */
    public int getMaxHealth();

    /**
     * Calculate the damage inflicted by one entity onto another.
     * Applies damage type modifiers to the base damage to calculate the final amount.
     *
     * @param base the base damage dealt by the entity's attack.
     * @param type the damage type of the entity's attack.
     * @return the total damage inflicted by this attack.
     */
    public int calcDamage(int base, DamageEnum type);

    /**
     * List the abilities that are currently usable by the entity.
     *
     * @return a list of abilities.
     */
    public List<AbilityEnum> getAbilities();

    /**
     * Reset an entity's health to its starting value.
     */
    public void resetHealth();

    /**
     * Get the enumerated type of the entity.
     *
     * @return the entity type.
     */
    public EntityEnum getType();
}