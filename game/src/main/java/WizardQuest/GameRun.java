package WizardQuest;

import java.time.LocalDateTime;

public class GameRun implements GameRunInterface {
    //Drawn from for stages 1 and 2.
    private EncounterInterface[] phase1NormalEncounters; 

    //Drawn from for stages 4 and 5.
    private EncounterInterface[] phase2NormalEncounters; 

    //Drawn from for stages 7 and 8.
    private EncounterInterface[] phase3NormalEncounters; 

    //Drawn from for stage 3.
    private EncounterInterface[] phase1BossEncounters; 

    //Drawn from for stage 6.
    private EncounterInterface[] phase2BossEncounters; 

    //Drawn from for stage 9.
    private EncounterInterface[] phase3BossEncounters; 

    //Drawn from for stage 10.
    private EncounterInterface finalBoss;

    //Pool upgrades for the shop are chosen from. When bought they're removed from this pool.
    private UpgradeType[] shopUpgrades;
    
    private PlayerInterface player

    private int currentStage

    private Difficulty currentDifficulty

    /**
     * Creates a run for the game in the specified difficulty. Also takes note of
     * when the run started.
     * 
     * @param Difficulty the difficulty setting for the run. 
     */
    public GameRun(Difficulty Difficulty);

    /**
     * Picks a random encounter for the current stage from the encounter pool for
     * that stage and returns a reference to it. It is the resonsiblity of the
     * caller to keep track of the encounter, mark it as complete when it is
     * finished, and reset it if the player retries it after dying.
     * 
     * @return a reference to the chosen encounter.
     */
    @Override
    public EncounterInterface pickEncounter();

    /**
     * Picks a number of upgrades from the shop determined by the ShopItemCount
     * design parameter and returns an array of references to them.
     * 
     * @return an array of references to the upgrades.
     */
    @Override
    public UpgradeType[] viewShop();

    /**
     * Attempts to buy the selected upgrade, throwing an error if the player doesn't
     * have enough coins, and otherwise reducing their number of coins by the
     * upgrade's price. It then removes the upgrade from the poo
     * of upgrades the shop selects from and decorates the player with it.
     * 
     * @param upgrade the upgrade being bought from the shop.
     * @throws NotEnoughResourceException if the player doesn't have enough coins to
     *                                    buy the upgrade.
     */
    @Override
    public void purchaseUpgrade(UpgradeType upgrade) throws NotEnoughResourceException;

    /**
     * Returns a reference to the player.
     * 
     * @return a reference to the player.
     */
    @Override
    public PlayerInterface getPlayer();

    /**
     * Increments the stage count, so the pickEncounter method can draw from the
     * encounter pool for the correct stage.
     */
    @Override
    public void nextStage();

    /**
     * Gets the current stage's number.
     * 
     * @return the current stage number (between 1 and 10 inclusive).
     */
    @Override
    public int getStage();

    /**
     * GIves the time when this game run instance was created (and thus the run
     * started).
     * 
     * @return the LocalDateTime of when the run started.
     */
    @Override
    public LocalDateTime getRunStartTime();

    /**
     * Returns the number of deaths the player has had in this run.
     * 
     * @return the number of time the player's died this run.
     */
    @Override
    public int getDeathCount();

    /**
     * Increments the run's death count. It is the job of the caller to decrement
     * the player's lives and register when their health reaches 0 to call this
     * method.
     */
    @Override
    public void incrementDeathCount();
}
