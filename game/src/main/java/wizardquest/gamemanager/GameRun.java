package wizardquest.gamemanager;

import java.time.LocalDateTime;
import java.util.Random;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;

public class GameRun implements GameRunInterface {
    // Drawn from for stages 1 and 2.
    private final EncounterInterface[] phase1NormalEncounters;

    // Drawn from for stages 4 and 5.
    private final EncounterInterface[] phase2NormalEncounters;

    // Drawn from for stages 7 and 8.
    private final EncounterInterface[] phase3NormalEncounters;

    // Drawn from for stage 3.
    private final EncounterInterface phase1Boss;

    // Drawn from for stage 6.
    private final EncounterInterface phase2Boss;

    // Drawn from for stage 9.
    private final EncounterInterface phase3Boss;

    // Drawn from for stage 10.
    private final EncounterInterface finalBoss;

    // Pool upgrades for the shop are chosen from. When bought they're removed from
    // this pool.
    private final UpgradeEnum[] shopUpgrades;

    private PlayerInterface player;
    private int currentStage;
    private final DifficultyEnum difficulty;
    private final LocalDateTime startTime;
    private final Random random;
    private int deathCount;
    private final int sessionID;

    /**
     * Creates a run for the game in the specified difficulty. Also takes note of
     * when the run started.
     *
     * @param difficulty the difficulty setting for the run.
     */

    public GameRun(DifficultyEnum difficulty, int sessionID) {
        phase1NormalEncounters = new EncounterInterface[] {
                new Encounter(EncounterEnum.GOBLIN_ENCOUNTER, difficulty),
                new Encounter(EncounterEnum.FISHMAN_ENCOUNTER, difficulty)
        };
        phase2NormalEncounters = null;
        phase3NormalEncounters = null;
        phase1Boss = null; // Placeholder, contents are TBC -put back to null
                           // after testing
        phase2Boss = null; // Placeholder, contents are TBC -put back to null
                           // after testing
        phase3Boss = null; // Placeholder, contents are TBC -put back to null
                           // after testing
        finalBoss = null; // Placeholder, contents are TBC -put back to null
                          // after testing
        shopUpgrades = new UpgradeEnum[] {
                UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE,
                UpgradeEnum.FIRE_DAMAGE_RESISTANCE,
                UpgradeEnum.WATER_DAMAGE_RESISTANCE,
                UpgradeEnum.THUNDER_DAMAGE_RESISTANCE,
                UpgradeEnum.SLASH_UNLOCK,
                UpgradeEnum.ABSOLUTE_PULSE_UNLOCK,
                UpgradeEnum.WATER_JET_UNLOCK,
                UpgradeEnum.FIRE_BALL_UNLOCK,
                UpgradeEnum.THUNDER_STORM_UNLOCK
        }; // Contents of this array are TBC

        this.player = new Player(difficulty);
        this.currentStage = 1;
        this.difficulty = difficulty;
        this.startTime = LocalDateTime.now();
        this.random = new Random();
        this.deathCount = 0;
        this.sessionID = sessionID;
    }

    @Override
    public int getSessionID() {
        return this.sessionID;
    }

    @Override
    public EncounterInterface pickEncounter() throws IllegalStateException {
        return switch (this.currentStage) {
            case 1, 2 -> pickEncounterFrom(this.phase1NormalEncounters);
            case 3 -> this.phase1Boss;
            case 4, 5 -> pickEncounterFrom(this.phase2NormalEncounters);
            case 6 -> this.phase2Boss;
            case 7, 8 -> pickEncounterFrom(this.phase3NormalEncounters);
            case 9 -> this.phase3Boss;
            case 10 -> this.finalBoss;
            default -> null;
        };
    }

    @Override
    public UpgradeEnum[] viewShop() {

        shuffleArray(this.shopUpgrades);
        int totalUpgradesInShop = SettingsSingleton.getInstance().getShopItemCount(this.difficulty);
        UpgradeEnum[] shop = new UpgradeEnum[totalUpgradesInShop];
        int i = 0;
        // Loop through the shuffled array of upgrades, adding each element to the shop
        // until the shop item count has
        // been reached. Skip over any null values - these are upgrades that have
        // already been purchased during the run.
        for (UpgradeEnum shopUpgrade : this.shopUpgrades) {
            if (shopUpgrade != null) {
                shop[i] = shopUpgrade;
                i++;
                if (i == totalUpgradesInShop) {
                    break;
                }
            }
        }
        return shop;
    }

    @Override
    public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException {
        // If upgrade is unaffordable, throw the relevant exception
        if (upgrade.getPrice() > this.player.getCoins()) {
            int difference = upgrade.getPrice() - this.player.getCoins();
            throw new LackingResourceException(
                    String.format("Not enough coins to purchase this upgrade. %d more coins needed.", difference));
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
        return this.deathCount;
    }

    @Override
    public void incrementDeathCount() {
        this.player.loseLives(1);
        this.deathCount++;
        this.player.resetHealth();
    }

    @Override
    public DifficultyEnum getDifficulty() {
        return this.difficulty;
    }

    /**
     * Iterates through the pool of upgrades displayable in the shop, and set the
     * value of the one just purchased to null.
     *
     * @param upgrade the upgrade to remove from the pool.
     */
    private void removeUpgradeFromPool(UpgradeEnum upgrade) {
        for (int i = 0; i < this.shopUpgrades.length; i++) {
            if (this.shopUpgrades[i] == upgrade) {
                this.shopUpgrades[i] = null;
            }
        }
    }

    // Fisher-Yates shuffling algorithm used to randomise a given array.
    private <T> void shuffleArray(T[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = this.random.nextInt(i + 1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * Picks a random encounter (that is not yet completed) from the given array.
     * 
     * @param encounters the array to chose the encounter from
     * @return a reference to said encounter.
     * @throws IllegalStateException if all encounters in the array are complete.
     */
    private EncounterInterface pickEncounterFrom(EncounterInterface[] encounters) throws IllegalStateException {
        shuffleArray(encounters);
        for (EncounterInterface encounter : encounters) {
            if (!encounter.isComplete()) {
                return encounter;
            }
        }
        throw new IllegalStateException("Out of Encounters for stage: " + this.currentStage);
    }
}
