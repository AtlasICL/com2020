package WizardQuest;

import java.time.LocalDateTime;

/**
 * Provides the interface for a game run. A game run stores information about a
 * single run of the game, including the encounters in that run and the player.
 */
public interface GameRunInterface {
    /**
     * Picks a random encounter for the current stage from the encounter pool for
     * that stage and returns a reference to it. It is the responsibility of the
     * caller to keep track of the encounter, mark it as complete when it is
     * finished, and reset it if the player retries it after dying.
     * 
     * @return a reference to the chosen encounter.
     */
    public EncounterInterface pickEncounter();

    /**
     * Picks a number of upgrades from the shop determined by the ShopItemCount
     * design parameter and returns an array of references to them.
     * 
     * @return an array of references to the upgrades.
     */
    public UpgradeType[] viewShop();

    /**
     * Attempts to buy the selected upgrade, throwing an error if the player doesn't
     * have enough coins, and otherwise reducing their number of coins by the
     * upgrade's price. It then removes the upgrade from the poo
     * of upgrades the shop selects from and decorates the player with it.
     * 
     * @param upgrade the upgrade being bought from the shop.
     * @throws LackingResourceException if the player doesn't have enough coins to
     *                                  buy the upgrade.
     */
    public void purchaseUpgrade(UpgradeType upgrade) throws LackingResourceException;

    /**
     * Returns a reference to the player.
     * 
     * @return a reference to the player.
     */
    public PlayerInterface getPlayer();

    /**
     * Increments the stage count, so the pickEncounter method can draw from the
     * encounter pool for the correct stage.
     */
    public void nextStage();

    /**
     * Gets the current stage's number.
     * 
     * @return the current stage number (between 1 and 10 inclusive).
     */
    public int getStage();

    /**
     * GIves the time when this game run instance was created (and thus the run
     * started).
     * 
     * @return the LocalDateTime of when the run started.
     */
    public LocalDateTime getRunStartTime();

    /**
     * Returns the number of deaths the player has had in this run.
     * 
     * @return the number of time the player's died this run.
     */
    public int getDeathCount();

    /**
     * Increments the run's death count. It is the job of the caller to decrement
     * the player's lives and register when their health reaches 0 to call this
     * method.
     */
    public void incrementDeathCount();

    /**
     * Gets the difficulty of the run.
     * @return the difficulty setting for this run.
     */
    public Difficulty getDifficulty();
}
