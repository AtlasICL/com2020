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

public class SimulatedGameRun implements GameRunInterface {

    private static final int COINS_GAINED = 10;

    private final EncounterInterface[] phase1NormalEncounters;
    private final EncounterInterface[] phase2NormalEncounters;
    private final EncounterInterface[] phase3NormalEncounters;
    private final EncounterInterface phase1Boss;
    private final EncounterInterface phase2Boss;
    private final EncounterInterface phase3Boss;
    private final EncounterInterface finalBoss;
    private final UpgradeEnum[] shopUpgrades;
    private PlayerInterface player;
    private int currentStage;
    private final DifficultyEnum difficulty;
    private final LocalDateTime startTime;
    private Instant simTime;
    private final Random random;
    private int deathCount;
    private final int sessionID;
    private final GameManagerInterface gameManager;
    private final TelemetryListenerInterface telemetryListener;
    private final SettingsInterface settings;
    private final TimeManagerInterface timeManager;
    private final EntityAIInterface ai;


    public SimulatedGameRun(DifficultyEnum difficulty, int sessionID, String filepath) {
        this.difficulty = difficulty;
        this.sessionID = sessionID;
        this.currentStage = 1;
        this.startTime = LocalDateTime.now();
        this.simTime = Instant.now();
        this.random = new Random();
        this.deathCount = 0;
        this.player = new Player(difficulty);
        this.gameManager = GameManagerSingleton.getInstance();
        this.telemetryListener = TelemetryListenerSingleton.getInstance();
        this.settings = SettingsSingleton.getInstance();
        this.timeManager = TimeManagerSingleton.getInstance();
        this.ai = EntityAISingleton.getInstance();

        phase1NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.GOBLIN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.FISHMAN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.PYROMANCER_ENCOUNTER, difficulty)
        };
        phase2NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.GOBLIN_DUO_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.GOBLIN_FISHMAN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.ARMOURED_GOBLIN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.PYROMANCER_FISHMAN_ENCOUNTER, difficulty)
        };
        phase3NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER, difficulty)
        };
        phase1Boss = new Encounter(EncounterEnum.EVIL_WIZARD_ENCOUNTER, difficulty);
        phase2Boss = new Encounter(EncounterEnum.GHOST_ENCOUNTER, difficulty);
        phase3Boss = new Encounter(EncounterEnum.BLACK_KNIGHT_ENCOUNTER, difficulty);
        finalBoss = new Encounter(EncounterEnum.DRAGON_ENCOUNTER, difficulty);
        this.shopUpgrades = new UpgradeEnum[] {
            UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE,
            UpgradeEnum.FIRE_DAMAGE_RESISTANCE,
            UpgradeEnum.WATER_DAMAGE_RESISTANCE,
            UpgradeEnum.THUNDER_DAMAGE_RESISTANCE,
            UpgradeEnum.IMPROVED_PHYSICAL_DAMAGE,
            UpgradeEnum.IMPROVED_FIRE_DAMAGE,
            UpgradeEnum.IMPROVED_WATER_DAMAGE,
            UpgradeEnum.IMPROVED_THUNDER_DAMAGE,
            UpgradeEnum.SLASH_UNLOCK,
            UpgradeEnum.ABSOLUTE_PULSE_UNLOCK,
            UpgradeEnum.WATER_JET_UNLOCK,
            UpgradeEnum.FIRE_BALL_UNLOCK,
            UpgradeEnum.THUNDER_STORM_UNLOCK
        };
        telemetryListener.setDestinationFile(new File(filepath));

        DifficultyEnum d = getDifficulty();
        
        telemetryListener.onStartSession(
                new StartSessionEvent(
                        settings.getUserID(), 
                        gameManager.getSessionID(),
                        TimeManagerSingleton.getInstance().getCurrentTime(),
                        d));
        
        gameManager.startNewGame(d);

        GameRunInterface lastRun = null;
        PlayerInterface lastPlayer = null;

        EncounterInterface currentEncounter = gameManager.pickEncounter();

        while (gameManager.isGameRunning()) {

            try {
                lastRun = gameManager.getCurrentRun();
            } catch (Exception ignored) {}

            try {
                lastPlayer = gameManager.getCurrentPlayer();
            } catch (Exception ignored) {}

            boolean encounterWon = simulateEncounter(currentEncounter, isBossEncounterEncounter(currentEncounter.getType()));

            if (!encounterWon) {
                continue;
            }

            GameRunInterface run = gameManager.getCurrentRun();

            if (run != null && run.getStage() > 10) {
                endSimulation(lastRun, lastPlayer);
                return;
            }

            simulateShop();

            gameManager.advanceToNextLevel();

            currentEncounter = gameManager.pickEncounter();
        }

        endSimulation(lastRun, lastPlayer);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private boolean simulateEncounter(EncounterInterface encounter, boolean isBossEncounter) {

        GameRunInterface run = gameManager.getCurrentRun();

        if (isBossEncounter) {
            telemetryListener.onBossEncounterStart(
                    new BossEncounterStartEvent(
                            settings.getUserID(), gameManager.getSessionID(),
                            TimeManagerSingleton.getInstance().getCurrentTime(),
                            encounter.getType(),
                            gameManager.getCurrentDifficulty(),
                            run != null ? run.getStage() : 1));
        } else {
            telemetryListener.onNormalEncounterStart(
                    new NormalEncounterStartEvent(
                            settings.getUserID(), gameManager.getSessionID(),
                            TimeManagerSingleton.getInstance().getCurrentTime(),
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
                                    timeManager.getCurrentTime(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getLives()));
                } else {
                    telemetryListener.onNormalEncounterFail(
                            new NormalEncounterFailEvent(
                                    settings.getUserID(), gameManager.getSessionID(),
                                    timeManager.getCurrentTime(),
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
                                settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                target.getType()));
            }

            if (allEnemiesDead(enemies)) {
                if (isBossEncounter) {
                    telemetryListener.onBossEncounterComplete(
                            new BossEncounterCompleteEvent(
                                    settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getHealth()));
                } else {
                    telemetryListener.onNormalEncounterComplete(
                            new NormalEncounterCompleteEvent(
                                    settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    player.getHealth()));
                }
                gameManager.completeCurrentEncounter();
                player.gainCoins(COINS_GAINED);
                telemetryListener.onGainCoin(
                        new GainCoinEvent(
                                settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
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
                                TimeManagerSingleton.getInstance().getCurrentTime(),
                                EncounterEnum.GOBLIN_ENCOUNTER,
                                gameManager.getCurrentDifficulty(),
                                1,
                                u,
                                u.getPrice())
                        );
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

    private boolean isBossEncounterEncounter(EncounterEnum encounterType) {
        return encounterType == EncounterEnum.EVIL_WIZARD_ENCOUNTER ||
               encounterType == EncounterEnum.GHOST_ENCOUNTER ||
               encounterType == EncounterEnum.BLACK_KNIGHT_ENCOUNTER ||
               encounterType == EncounterEnum.DRAGON_ENCOUNTER;
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
                        TimeManagerSingleton.getInstance().getCurrentTime()));
    }
}
