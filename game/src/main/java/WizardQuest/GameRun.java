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
    private EncounterInterface phase1Boss; 

    //Drawn from for stage 6.
    private EncounterInterface phase2Boss; 

    //Drawn from for stage 9.
    private EncounterInterface phase3Boss; 

    //Drawn from for stage 10.
    private EncounterInterface finalBoss;

    //Pool upgrades for the shop are chosen from. When bought they're removed from this pool.
    private UpgradeType[] shopUpgrades;
    
    private PlayerInterface player;
    private int currentStage;
    private Difficulty currentDifficulty;

    /**
     * Creates a run for the game in the specified difficulty. Also takes note of
     * when the run started.
     *
     * @param difficulty the difficulty setting for the run.
     */
    public GameRun(Difficulty difficulty) {}

    @Override
    public EncounterInterface pickEncounter() {
        return null; // PLACEHOLDER
    }

    @Override
    public UpgradeType[] viewShop() {
        return null; // PLACEHOLDER
    }

    @Override
    public void purchaseUpgrade(UpgradeType upgrade) throws LackingResourceException {}

    @Override
    public PlayerInterface getPlayer() {
        return player;
    }

    @Override
    public void nextStage() {}

    @Override
    public int getStage() {
        return currentStage;
    }

    @Override
    public LocalDateTime getRunStartTime() {
        return LocalDateTime.now(); // PLACEHOLDER
    }

    @Override
    public int getDeathCount() {
        return -1; // PLACEHOLDER
    }

    @Override
    public void incrementDeathCount() {}
}
