package WizardQuest;

/**
 * Interface the game manager implements. Acts as a interface between the front
 * and back end.
 */
public interface GameManagerInterface {
    /**
     * Retruns whether there is a instance of the game running.
     * @return true if there is a game running, false if not.
     */
    public boolean isGameRunning();

    /**
     * Returns the current difficulty of the current run.
     * @return the current difficulty of the current run.
     */
    public Difficulty getCurrentDifficulty();

    /**
     * Starts a new game, with difficulty.
     * @param difficulty the difficulty to start the game on.
     */
    public void startNewGame(Difficulty difficulty);

    /** 
     * Returns the current game run.
     * @return the current game run.
    */
    public GameRunInterface getCurrentRun();

    /**
     * Returns the current player.
     * @return the current player.
     */
    public String getCurrentPlayer();

    /**
     * Picks a new (random) encounter/fight for the current level.
     * Stored as the current encounter.
     * @return the new chosen encounter.
     */
    public EncounterInterface setNewEncounter();


    /**
     * Returns the current encounter/fight within a level.
     * @return the current encounter/fight within a level.
     */
    public EncounterInterface getCurrentEncounter();

    /**
     * Removes current encounter/fight within a level.
     * Returns null state.
     */
    public void removeCurrentEncounter();

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
     * @return all upgrades available in the shop.
     */
    public UpgradeType[] viewShop();

    /**
     * Attempts to purchase an upgrade from the shop.
     * Checks if the player has enough currency to purchase specified upgrade.
     * if not, throws an exception.
     * @param upgrade the upgrade being bought from the shop.
     * @throws NotEnoughResourceException if the player doesn't have enough coins to
     *                                    buy the upgrade.
     */

    public void purchaseUpgrade(UpgradeType upgrade) throws NotEnoughResourceException;


    /**
     * Ends current game.
     * Returns to main menu and resets game data --- no save.
     */    
    public void endGame();
}
