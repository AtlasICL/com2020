package wizardquest.gamemanager;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;

/**
 * Interface the game manager implements. Acts as a interface between the front
 * and back end.
 */
public interface GameManagerInterface {
    /**
     * Returns whether there is a instance of the game running.
     * 
     * @return true if there is a game running, false if not.
     */
    public boolean isGameRunning();

    /**
     * Returns the current difficulty of the current run.
     * 
     * @return the current difficulty of the current run.
     */
    public DifficultyEnum getCurrentDifficulty();

    /**
     * Starts a new game, with difficulty. Sends SessionStartEvent to telemetry
     * listener.
     * 
     * @param difficulty the difficulty to start the game on.
     */
    public void startNewGame(DifficultyEnum difficulty);

    /**
     * Returns the current game run.
     * 
     * @return the current game run.
     */
    public GameRunInterface getCurrentRun();

    /**
     * Returns the current player.
     * 
     * @return the current player.
     */
    public PlayerInterface getCurrentPlayer();

    /**
     * Picks a new (random) encounter/fight for the current level.
     * Stored as the current encounter.
     * 
     * @return the new chosen encounter.
     */
    public EncounterInterface pickEncounter();

    /**
     * Returns the current encounter/fight within a level.
     * 
     * @return the current encounter/fight within a level.
     */
    public EncounterInterface getCurrentEncounter();

    /**
     * Resets the current encounter if failed.
     */
    public void resetFailedEncounter();

    /**
     * Completes the current encounter if won.
     */
    public void completeCurrentEncounter();

    /**
     * Advances to the next level (Encounter(s) completed).
     */
    public void advanceToNextLevel();

    /**
     * Opens shop interface after level completion.
     * Returns all upgrades available in the shop.
     * 
     * @return all upgrades available in the shop.
     */
    public UpgradeEnum[] viewShop();

    /**
     * Attempts to purchase an upgrade from the shop.
     * Checks if the player has enough currency to purchase specified upgrade.
     * if not, throws an exception.
     * 
     * @param upgrade the upgrade being bought from the shop.
     * @throws LackingResourceException if the player doesn't have enough coins to
     *                                  buy the upgrade.
     */
    public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException;

    /**
     * Ends current game. Sends EndSession to telemetry listener.
     * Returns to main menu and resets game data --- no save.
     */
    public void endGame();

    /**
     * Gets the ID for the current run's session.
     * 
     * @return the session ID for the current run.
     */
    public int getSessionID();
}
