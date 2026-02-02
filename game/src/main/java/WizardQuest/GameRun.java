package WizardQuest;

import com.sun.scenario.Settings;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

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

    // Pool upgrades for the shop are chosen from. When bought they're removed from this pool.
    private UpgradeType[] shopUpgrades;
    
    private PlayerInterface player;
    private int currentStage;
    private Difficulty currentDifficulty;
    private LocalDateTime startTime;

    /**
     * Creates a run for the game in the specified difficulty. Also takes note of
     * when the run started.
     *
     * @param difficulty the difficulty setting for the run.
     */
    public GameRun(Difficulty difficulty) {
        phase1NormalEncounters = new EncounterInterface[] {}; // Contents of this array are TBC
        phase2NormalEncounters = new EncounterInterface[] {}; // Contents of this array are TBC
        phase3NormalEncounters = new EncounterInterface[] {}; // Contents of this array are TBC
        phase1Boss = new Encounter(null); // Placeholder, contents are TBC
        phase2Boss = new Encounter(null); // Placeholder, contents are TBC
        phase3Boss = new Encounter(null); // Placeholder, contents are TBC
        finalBoss = new Encounter(null); // Placeholder, contents are TBC
        shopUpgrades = new UpgradeType[] {}; // Contents of this array are TBC

        this.player = new Player(difficulty);
        this.currentStage = 1;
        this.currentDifficulty = difficulty;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public EncounterInterface pickEncounter() {
        Random r = new Random();
        int rInt;
        switch (this.currentStage) {
            case 1:
            case 2:
                rInt = r.nextInt(this.phase1NormalEncounters.length);
                return this.phase1NormalEncounters[rInt];
            case 3:
                return this.phase1Boss;
            case 4:
            case 5:
                rInt = r.nextInt(this.phase2NormalEncounters.length);
                return this.phase2NormalEncounters[rInt];
            case 6:
                return this.phase2Boss;
            case 7:
            case 8:
                rInt = r.nextInt(this.phase3NormalEncounters.length);
                return this.phase3NormalEncounters[rInt];
            case 9:
                return this.phase3Boss;
            case 10:
                return this.finalBoss;
            default:
                return null;
        }
    }

    @Override
    public UpgradeType[] viewShop() {
        // Fisher-Yates shuffling algorithm used to randomise order of upgrade pool
        Random r = new Random();
        for (int i = this.shopUpgrades.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            UpgradeType temp = this.shopUpgrades[i];
            this.shopUpgrades[i] = this.shopUpgrades[j];
            this.shopUpgrades[j] = temp;
        }
        int totalUpgradesInShop = SettingsSingleton.getSettings().getShopItemCount(this.currentDifficulty);
        UpgradeType[] shop = new UpgradeType[totalUpgradesInShop];
        int i = 0;
        // Loop through the shuffled array of upgrades, adding each element to the shop until the shop item count has
        // been reached. Skip over any null values - these are upgrades that have already been purchased during the run.
        for (int j = 0; j < this.shopUpgrades.length; j++) {
            if (this.shopUpgrades[j] != null) {
                shop[i] = this.shopUpgrades[j];
                i++;
                if (i == totalUpgradesInShop) {
                    break;
                }
            }
        }
        return shop;
    }

    @Override
    public void purchaseUpgrade(UpgradeType upgrade) throws LackingResourceException {
        // If upgrade is unaffordable, throw the relevant exception
        if (upgrade.getPrice() > this.player.getCoins()) {
            int difference = upgrade.getPrice() - this.player.getCoins();
            throw new LackingResourceException(String.format("Not enough coins to purchase this upgrade. %d more coins needed.", difference));
        } else {
            // Remove the upgrade from the pool of shop upgrades.
            removeUpgradeFromPool(upgrade);
            // Deduct the upgrade price from the player's coins.
            player.loseCoins(upgrade.getPrice());
            // Apply the upgrade to the player.
            player = upgrade.applyUpgrade(player);
        }
    }

    @Override
    public PlayerInterface getPlayer() {
        return player;
    }

    @Override
    public void nextStage() {
        this.currentStage++;
    }

    @Override
    public int getStage() {
        return currentStage;
    }

    @Override
    public LocalDateTime getRunStartTime() {
        return startTime;
    }

    @Override
    public int getDeathCount() {
        int startingLives = SettingsSingleton.getSettings().getStartingLives(this.currentDifficulty);
        int currentLives = this.player.getLives();
        return startingLives - currentLives;
    }

    @Override
    public void incrementDeathCount() {
        this.player.loseLives(1);
    }

    /**
     * Iterates through the pool of upgrades displayable in the shop, and set the
     * value of the one just purchased to null.
     *
     * @param upgrade the upgrade to remove from the pool.
     */
    private void removeUpgradeFromPool(UpgradeType upgrade) {
        for (int i = 0; i < this.shopUpgrades.length; i++) {
            if (this.shopUpgrades[i] == upgrade) {
                this.shopUpgrades[i] = null;
            }
        }
    }
}
