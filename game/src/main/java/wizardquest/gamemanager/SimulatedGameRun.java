package wizardquest.gamemanager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

import java.io.File;
import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.entity.Player;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.EntityAISingleton;
import wizardquest.entity.EntityAIInterface;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.StartSessionEvent;
import wizardquest.telemetry.EndSessionEvent;
import wizardquest.telemetry.GainCoinEvent;
import wizardquest.telemetry.KillEnemyEvent;
import wizardquest.telemetry.BossEncounterStartEvent;
import wizardquest.telemetry.BuyUpgradeEvent;
import wizardquest.telemetry.BossEncounterCompleteEvent;
import wizardquest.telemetry.BossEncounterFailEvent;
import wizardquest.telemetry.NormalEncounterStartEvent;
import wizardquest.telemetry.NormalEncounterCompleteEvent;
import wizardquest.telemetry.NormalEncounterFailEvent;
import wizardquest.telemetry.TelemetryListenerSingleton;
import wizardquest.telemetry.TelemetryListenerInterface;

/**
 * Simulates a game run, runs a simulated game run upon construction, then
 * stores
 * the results of the run, which can be accessed via several getter methods.
 */
public class SimulatedGameRun {
    private int stage;
    private int deaths;
    private int coins;

    private Instant currentTime;

    // Note: not static to allow the use of mock objects by instantiating a new
    // SimulatedGameRun.
    private final GameManagerInterface gameManager;
    private final TelemetryListenerInterface telemetryListener;
    private final SettingsInterface settings;
    private final EntityAIInterface ai;
    private final Random random;

    /**
     * Constructs and executes a simulated run of the given difficulty, writing
     * simulation events to the given filepath.
     * 
     * @param difficulty the difficulty of the simulated run.
     * @param filepath   the filepath to write events to.
     */
    public SimulatedGameRun(DifficultyEnum difficulty, String filepath) {

        // setup
        this.currentTime = TimeManagerSingleton.getInstance().getCurrentTime();
        this.random = new Random();
        this.deaths = 0;
        this.gameManager = GameManagerSingleton.getInstance();
        this.telemetryListener = TelemetryListenerSingleton.getInstance();
        this.settings = SettingsSingleton.getInstance();
        this.ai = EntityAISingleton.getInstance();
        telemetryListener.setDestinationFile(new File(filepath));

        this.startSession(difficulty);

        while (!this.gameOver()) {
            if (!this.simulateEncounter()) {
                break;
            }
            this.simulateShop();
        }

        this.endSession();

    }

    private void startSession(DifficultyEnum difficulty) {

        this.gameManager.startNewGame(difficulty);
    }

    private boolean gameOver() {
        return (this.gameManager.getCurrentPlayer().getLives() <= 0)
                || (this.gameManager.getCurrentRun().getStage() > 10);
    }

    /**
     * Simulates an encounter until it is complete or the player dies.
     * 
     * @return true if the encounter is completed, false if the player dies.
     */
    private boolean simulateEncounter() {
        EncounterInterface currentEncounter = this.gameManager.pickEncounter();

        if (currentEncounter == null){
            throw new IllegalStateException("Err: No encounters to draw from");
        }

        while(true){
            
        }

        if (Utils.isBossEncounter(this.gameManager.getCurrentRun().getStage()){
            telemetryListener.onBossEncounterStart(
                    new BossEncounterStartEvent(
                            settings.getUserID(), gameManager.getSessionID(),
                            getTimestamp(),
                            encounter.getType(),
                            gameManager.getCurrentDifficulty(),
                            run != null ? run.getStage() : 1));
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private boolean simulateEncounter(EncounterInterface encounter, boolean isBossEncounter) {

        GameRunInterface run = gameManager.getCurrentRun();

        if (isBossEncounter) {
            telemetryListener.onBossEncounterStart(
                    new BossEncounterStartEvent(
                            settings.getUserID(), gameManager.getSessionID(),
                            getTimestamp(),
                            encounter.getType(),
                            gameManager.getCurrentDifficulty(),
                            run != null ? run.getStage() : 1));
        } else {
            telemetryListener.onNormalEncounterStart(
                    new NormalEncounterStartEvent(
                            settings.getUserID(), gameManager.getSessionID(),
                            getTimestamp(),
                            encounter.getType(),
                            gameManager.getCurrentDifficulty(),
                            run != null ? run.getStage() : 1));
        }

        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player == null) {
            gameManager.endGame();
            return false;
        }
        player.resetHealth();
        player.resetMagic();

        while (true) {

            player.gainMagic(Math.min(player.getMagicRegenRate(), (player.getMaxMagic() - player.getMagic())));

            EntityInterface[] enemies = encounter.getEnemies();

            if (player.getHealth() <= 0) {
                if (isBossEncounter) {
                    telemetryListener.onBossEncounterFail(
                            new BossEncounterFailEvent(
                                    settings.getUserID(), gameManager.getSessionID(),
                                    getTimestamp(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getLives()));
                } else {
                    telemetryListener.onNormalEncounterFail(
                            new NormalEncounterFailEvent(
                                    settings.getUserID(), gameManager.getSessionID(),
                                    getTimestamp(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getLives()));
                }
                gameManager.resetFailedEncounter();

                if (player.getLives() == 0) {
                    gameManager.endGame();
                }

                return false;
            }

            EntityInterface target = ai.pickTarget(enemies);
            AbilityEnum ability = ai.pickAbility(player);

            if (ability == null) {
                gameManager.endGame();
                return false;
            }

            if (target == null) {
                gameManager.endGame();
                return false;
            }

            try {
                ability.execute(player, target);
            } catch (LackingResourceException e) {
                e.printStackTrace();
            }

            if (target.getHealth() <= 0) {
                telemetryListener.onKillEnemy(
                        new KillEnemyEvent(
                                settings.getUserID(), gameManager.getSessionID(), getTimestamp(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                target.getType()));
            }

            if (allEnemiesDead(enemies)) {
                if (isBossEncounter) {
                    telemetryListener.onBossEncounterComplete(
                            new BossEncounterCompleteEvent(
                                    settings.getUserID(), gameManager.getSessionID(), getTimestamp(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getHealth()));
                } else {
                    telemetryListener.onNormalEncounterComplete(
                            new NormalEncounterCompleteEvent(
                                    settings.getUserID(), gameManager.getSessionID(), getTimestamp(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getHealth()));
                }
                gameManager.completeCurrentEncounter();
                player.gainCoins(COINS_GAINED);
                telemetryListener.onGainCoin(
                        new GainCoinEvent(
                                settings.getUserID(), gameManager.getSessionID(), getTimestamp(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                COINS_GAINED));
                return true;
            }

            for (EntityInterface enemy : enemies) {
                if (enemy == null)
                    continue;
                if (enemy.getHealth() <= 0)
                    continue;

                AbilityEnum a = ai.pickAbility(enemy);
                try {
                    a.execute(enemy, player);
                } catch (LackingResourceException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void simulateShop() {
        UpgradeEnum[] upgrades = gameManager.viewShop();
        UpgradeEnum u = ai.pickUpgrade(upgrades, gameManager.getCurrentPlayer().getCoins());
        if (u != null) {
            try {
                gameManager.purchaseUpgrade(u);
                telemetryListener.onBuyUpgrade(
                        new BuyUpgradeEvent(
                                settings.getUserID(),
                                gameManager.getSessionID(),
                                getTimestamp(),
                                EncounterEnum.GOBLIN_ENCOUNTER,
                                gameManager.getCurrentDifficulty(),
                                1,
                                u,
                                u.getPrice()));
            } catch (LackingResourceException e) {
                e.printStackTrace();
            }
        }
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

    private EncounterInterface pickEncounterFrom(EncounterInterface[] encounters) throws IllegalStateException {
        shuffleArray(encounters);
        for (EncounterInterface encounter : encounters) {
            if (!encounter.isComplete()) {
                return encounter;
            }
        }
        throw new IllegalStateException("Out of Encounters for stage: " + this.currentStage);
    }

    @Override
    public UpgradeEnum[] viewShop() {

        shuffleArray(this.shopUpgrades);

        SettingsInterface settings = SettingsSingleton.getInstance();
        int shopSize = settings.getShopItemCount(this.difficulty);
        UpgradeEnum[] shop = new UpgradeEnum[shopSize];

        int i = 0;

        for (UpgradeEnum u : shopUpgrades) {
            if (u != null) {
                shop[i] = u;
                i++;

                if (i == shopSize) {
                    break;
                }
            }
        }
        return shop;
    }

    @Override
    public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException {

        if (upgrade.getPrice() > player.getCoins()) {
            int difference = upgrade.getPrice() - player.getCoins();
            throw new LackingResourceException(
                    "Not enough coins to purchase this upgrade. " + difference + " more coins needed.");

        } else {
            removeUpgradeFromPool(upgrade);
            player.loseCoins(upgrade.getPrice());
            player = upgrade.applyUpgrade(player);
        }
    }

    @Override
    public PlayerInterface getPlayer() {
        return player;
    }

    @Override
    public void nextStage() {
        currentStage++;
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
        return deathCount;
    }

    @Override
    public void incrementDeathCount() {
        player.loseLives(1);
        deathCount++;
        player.resetHealth();
    }

    @Override
    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    @Override
    public int getSessionID() {
        return sessionID;
    }

    private Instant getTimestamp() {
        int randTime = random.nextInt(10) + 1; // does not take situation into account, currently.
        simTime = simTime.plusSeconds(randTime);
        return simTime;
    }

    private void removeUpgradeFromPool(UpgradeEnum upgrade) {

        for (int i = 0; i < shopUpgrades.length; i++) {

            if (shopUpgrades[i] == upgrade) {
                shopUpgrades[i] = null;
            }
        }
    }

    private <T> void shuffleArray(T[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    private void endSimulation(GameRunInterface lastRun, PlayerInterface lastPlayer) {
        telemetryListener.onEndSession(
                new EndSessionEvent(
                        settings.getUserID(),
                        gameManager.getSessionID(),
                        getTimestamp()));
    }
}
