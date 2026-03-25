package wizardquest.gamemanager;

import java.io.File;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.EntityAISingleton;
import wizardquest.entity.EntityAIInterface;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.BossEncounterCompleteEvent;
import wizardquest.telemetry.BossEncounterFailEvent;
import wizardquest.telemetry.BossEncounterStartEvent;
import wizardquest.telemetry.BuyUpgradeEvent;
import wizardquest.telemetry.EndSessionEvent;
import wizardquest.telemetry.GainCoinEvent;
import wizardquest.telemetry.KillEnemyEvent;
import wizardquest.telemetry.NormalEncounterCompleteEvent;
import wizardquest.telemetry.NormalEncounterFailEvent;
import wizardquest.telemetry.NormalEncounterStartEvent;
import wizardquest.telemetry.StartSessionEvent;
import wizardquest.telemetry.TelemetryListenerInterface;
import wizardquest.telemetry.TelemetryListenerSingleton;

/**
 * Simulates a game run, runs a simulated game run upon construction, then
 * stores
 * the results of the run, which can be accessed via several getter methods.
 */
public class SimulatedGameRun {
    private int stage;
    private int deaths;
    private int coins;
    private int sessionID;

    // Note: not static to allow the use of mock objects by instantiating a new
    // SimulatedGameRun.
    private final GameManagerInterface gameManager;
    private final TelemetryListenerInterface telemetryListener;
    private final SettingsInterface settings;
    private final EntityAIInterface ai;
    private final TimeManagerInterface time;

    /**
     * Constructs and executes a simulated run of the given difficulty, writing
     * simulation events to the given filepath.
     * 
     * @param difficulty the difficulty of the simulated run.
     * @param filepath   the filepath to write events to.
     */
    public SimulatedGameRun(DifficultyEnum difficulty, String filepath) {

        // setup

        this.gameManager = GameManagerSingleton.getInstance();
        this.settings = SettingsSingleton.getInstance();
        this.ai = EntityAISingleton.getInstance();

        TimeManagerSingleton.useSimulationTime();
        this.time = TimeManagerSingleton.getInstance();

        this.telemetryListener = TelemetryListenerSingleton.getInstance();
        telemetryListener.setDestinationFile(new File(filepath));

        // Run the game
        this.startSession(difficulty);


        while (!Utils.isRunOver(this.gameManager.getCurrentRun())) {
            if (!this.simulateEncounter()) {
                break;
            }
            this.simulateShop();
            this.gameManager.advanceToNextLevel();
        }

        this.endSession();

    }

    public int getStage() {
        return this.stage;
    }

    public int getDeathCount() {
        return this.deaths;
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public int getCoins() {
        return this.coins;
    }

    private void startSession(DifficultyEnum difficulty) {

        this.gameManager.startNewGame(difficulty);
        GameRunInterface run = gameManager.getCurrentRun();
        if (run != null) {
            telemetryListener.onStartSession(new StartSessionEvent(
                    this.settings.getUserID(), run.getSessionID(), this.time.getCurrentTime(), difficulty));
        }
    }

    /**
     * Simulates an encounter until it is complete or the player dies.
     * 
     * @return true if the encounter is completed, false if the player dies.
     */
    private boolean simulateEncounter() {
        EncounterInterface currentEncounter = this.gameManager.pickEncounter();
        GameRunInterface run = this.gameManager.getCurrentRun();

        if (currentEncounter == null) {
            throw new IllegalStateException("No encounters to draw from");
        }

        // Send event to game manager
        if (Utils.isBossEncounter(run.getStage())) {
            this.telemetryListener.onBossEncounterStart(
                    new BossEncounterStartEvent(
                            this.settings.getUserID(),
                            this.gameManager.getSessionID(),
                            this.time.getCurrentTime(),
                            currentEncounter.getType(),
                            this.gameManager.getCurrentDifficulty(),
                            run.getStage()));
        } else {
            this.telemetryListener.onNormalEncounterStart(
                    new NormalEncounterStartEvent(
                            this.settings.getUserID(),
                            this.gameManager.getSessionID(),
                            this.time.getCurrentTime(),
                            currentEncounter.getType(),
                            this.gameManager.getCurrentDifficulty(),
                            run.getStage()));
        }

        // Run the encounter until one team loses
        while (true) {
            if (this.attemptEncounter()) {
                return true;
            } else if (Utils.isRunFailed(run)) {
                return false;
            }
        }
    }

    /**
     * Attempts an encounter, returning whether the player won or lost that attempt,
     * and marking it complete if it is won.
     * 
     * @return true if the encounter is completed, false if it is failed.
     */
    private boolean attemptEncounter() {
        EncounterInterface currentEncounter = this.gameManager.getCurrentEncounter();
        GameRunInterface run = this.gameManager.getCurrentRun();
        PlayerInterface player = this.gameManager.getCurrentPlayer();

        player.resetHealth();
        player.resetMagic();
        while (true) {
            // Player turn
            player.gainMagic(this.settings.getMagicRegenRate(this.gameManager.getCurrentDifficulty()));

            EntityInterface target = this.ai.pickTarget(currentEncounter.getEnemies());
            try {
                this.ai.pickAbility(player).execute(player, target);
            } catch (LackingResourceException e) {
                e.printStackTrace();
            }

            if (Utils.isDead(target)) {
                this.telemetryListener.onKillEnemy(new KillEnemyEvent(
                        this.settings.getUserID(),
                        this.gameManager.getSessionID(),
                        this.time.getCurrentTime(),
                        currentEncounter.getType(),
                        this.gameManager.getCurrentDifficulty(),
                        run.getStage(),
                        target.getType()));
            }

            if (Utils.areAllEnemiesDead(currentEncounter)) {
                this.telemetryListener.onGainCoin(new GainCoinEvent(
                        this.settings.getUserID(),
                        this.gameManager.getSessionID(),
                        this.time.getCurrentTime(),
                        currentEncounter.getType(),
                        this.gameManager.getCurrentDifficulty(),
                        run.getStage(),
                        settings.getEncounterPayout(run.getDifficulty())));

                if (Utils.isBossEncounter(run.getStage())) {
                    this.telemetryListener.onBossEncounterComplete(
                            new BossEncounterCompleteEvent(
                                    this.settings.getUserID(),
                                    this.gameManager.getSessionID(),
                                    this.time.getCurrentTime(),
                                    currentEncounter.getType(),
                                    this.gameManager.getCurrentDifficulty(),
                                    run.getStage(),
                                    player.getHealth()));
                } else {
                    this.telemetryListener.onNormalEncounterComplete(
                            new NormalEncounterCompleteEvent(
                                    this.settings.getUserID(),
                                    this.gameManager.getSessionID(),
                                    this.time.getCurrentTime(),
                                    currentEncounter.getType(),
                                    this.gameManager.getCurrentDifficulty(),
                                    run.getStage(),
                                    player.getHealth()));
                }

                player.gainCoins(settings.getEncounterPayout(run.getDifficulty()));
                this.gameManager.completeCurrentEncounter();
                return true;
            }

            // Enemy turn
            for (EntityInterface enemy : currentEncounter.getEnemies()) {
                if (Utils.isDead(enemy)) {
                    continue;
                }
                try {
                    this.ai.pickAbility(enemy).execute(enemy, player);
                } catch (LackingResourceException e) {
                    e.printStackTrace();
                }
            }

            if (Utils.isDead(player)) {
                gameManager.resetFailedEncounter();

                if (Utils.isBossEncounter(run.getStage())) {
                    this.telemetryListener.onBossEncounterFail(
                            new BossEncounterFailEvent(
                                    this.settings.getUserID(),
                                    this.gameManager.getSessionID(),
                                    this.time.getCurrentTime(),
                                    currentEncounter.getType(),
                                    this.gameManager.getCurrentDifficulty(),
                                    run.getStage(),
                                    player.getLives()));
                } else {
                    this.telemetryListener.onNormalEncounterFail(
                            new NormalEncounterFailEvent(
                                    this.settings.getUserID(),
                                    this.gameManager.getSessionID(),
                                    this.time.getCurrentTime(),
                                    currentEncounter.getType(),
                                    this.gameManager.getCurrentDifficulty(),
                                    run.getStage(),
                                    player.getLives()));
                }
                return false;
            }
        }
    }

    private void simulateShop() {
        UpgradeEnum[] upgrades = gameManager.viewShop();
        UpgradeEnum u = ai.pickUpgrade(upgrades, gameManager.getCurrentPlayer().getCoins());
        if (u == null) {
            return;
        }
        try {
            gameManager.purchaseUpgrade(u);
            telemetryListener.onBuyUpgrade(
                    new BuyUpgradeEvent(
                            this.settings.getUserID(),
                            this.gameManager.getSessionID(),
                            this.time.getCurrentTime(),
                            this.gameManager.getCurrentEncounter().getType(),
                            this.gameManager.getCurrentDifficulty(),
                            this.gameManager.getCurrentRun().getStage(),
                            u,
                            u.getPrice()));
        } catch (LackingResourceException e) {
            e.printStackTrace();
        }
    }

    private void endSession() {
        this.deaths = this.gameManager.getCurrentRun().getDeathCount();
        this.coins = this.gameManager.getCurrentPlayer().getCoins();
        this.sessionID = this.gameManager.getSessionID();
        this.stage = this.gameManager.getCurrentRun().getStage();
        GameRunInterface run = gameManager.getCurrentRun();
        if (run != null) {
            telemetryListener.onEndSession(new EndSessionEvent(
                    settings.getUserID(), run.getSessionID(), this.time.getCurrentTime()));
        }
        this.gameManager.endGame();
        this.telemetryListener.resetDestinationFile();
        TimeManagerSingleton.useActualTime();
    }
}
