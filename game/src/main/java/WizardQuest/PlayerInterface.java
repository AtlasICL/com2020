package WizardQuest;

import java.util.List;

public interface PlayerInterface extends EntityInterface {
    /**
     * Gets the amount of coins a player currently has.
     *
     * @return the player's current coin total.
     */
    public int getCoins();

    /**
     * Decrements a player's coin total by a given amount.
     *
     * @param amount the total coins that the player will lose.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void loseCoins(int amount) throws IllegalArgumentException;

    /**
     * Increases a player's coin total by a given amount.
     *
     * @param amount the total coins that the player will gain.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void gainCoins(int amount) throws IllegalArgumentException;

    /**
     * Gets the amount of magic points a player currently has.
     *
     * @return the player's current magic total.
     */
    public int getMagic();

    /**
     * Gets the maximum amount of magic points a player can have.
     *
     * @return the player's maximum magic total.
     */
    public int getMaxMagic();

    /**
     * Gets the rate at which a player's magic points regenerates between each
     * stage.
     *
     * @return the player's magic regeneration rate per stage.
     */
    public int getMagicRegenRate();

    /**
     * Increases a player's magic points by a given amount.
     *
     * @param amount the total magic points that the player will gain.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void gainMagic(int amount) throws IllegalArgumentException;

    /**
     * Decrements a player's magic total by a given amount.
     *
     * @param amount the total magic points that the player will lose.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void loseMagic(int amount) throws IllegalArgumentException;

    /**
     * Resets the player's magic to 0.
     */
    public void resetMagic();

    /**
     * Gets the remaining lives that a player has in a session.
     *
     * @return the player's total remaining lives.
     */
    public int getLives();

    /**
     * Decrements a player's remaining lives by a given amount.
     *
     * @param amount the total lives that the player will lose.
     * @throws IllegalArgumentException if amount is not a non-negative number.
     */
    public void loseLives(int amount) throws IllegalArgumentException;

    /**
     * List the upgrades that have been purchased between encounters by the player.
     *
     * @return a list of purchased upgrades.
     */
    public List<UpgradeEnum> getUpgrades();
}
