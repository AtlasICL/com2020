package wizardquest.ui;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressBar;
import wizardquest.abilities.AbilityEnum;
import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.Authenticator;
import wizardquest.entity.EntityAIInterface;
import wizardquest.entity.EntityAISingleton;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.gamemanager.EncounterInterface;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.gamemanager.GameRunInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.gamemanager.Utils;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.*;

import java.time.Instant;

public class GameRunPage extends Application {

    // Access to core game systems through their singleton interfaces
    private final GameManagerInterface gameManager = GameManagerSingleton.getInstance();
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final EntityAIInterface ai = EntityAISingleton.getInstance();
    private final TelemetryListenerInterface telemetryListener = TelemetryListenerSingleton.getInstance();


    private VBox root;
    private final Label log = new Label("");

    private EncounterInterface currentEncounter;

    @Override
    public void start(Stage stage) {
        // Main container for swapping between different frames
        root = new VBox(8);
        root.setPadding(new Insets(12));
        // First screen shown when the game launches
        showLoginPage();
        stage.setScene(new Scene(root, 1280, 720));
        stage.setTitle("WizardQuest");
        stage.show();
    }

    /**
     * Displays the login screen where the user authenticates via SSO.
     * Once authenticated, the user is taken to the main menu.
     */
    private void showLoginPage() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);

        Label title = new Label("WizardQuest");
        Label status = new Label("");

        Button loginBtn = new Button("Login with SSO");

        loginBtn.setOnAction(e -> {
            // loginBtn.setDisable(true);
            status.setText("Opening browser...");
            Task<AuthenticationResult> authenticationTask = new Task<>() {
                @Override
                protected AuthenticationResult call() throws AuthenticationException{
                    Authenticator auth = new Authenticator();
                    return auth.login();
                }
            };

            authenticationTask.setOnSucceeded(event ->{
                try{
                    settings.loginWithResult(authenticationTask.getValue());
                    showMainMenu();
                } catch (AuthenticationException ex){
                    // Display login error to the user
                    Label error = new Label(
                            "Login failed.\n\n" +
                            "Your environment variables may not be configured correctly.\n" +
                            "Please check the README file and make sure all required\n" +
                            "authentication variables are set before launching the game."
                    );
                    error.setWrapText(true);
                    root.getChildren().add(error);
                    loginBtn.setDisable(false);
                }
            });

            authenticationTask.setOnFailed(event -> {
                // Display login error to the user
                Label error = new Label(
                        "Login failed.\n\n" +
                                "Your environment variables may not be configured correctly.\n" +
                                "Please check the README file and make sure all required\n" +
                                "authentication variables are set before launching the game.");
                error.setWrapText(true);
                root.getChildren().add(error);
                loginBtn.setDisable(false);
            });
            Thread t = new Thread(authenticationTask, "sso-login-thread");
            t.setDaemon(true);
            t.start();
        });

        root.getChildren().addAll(title, loginBtn, status);
    }

    /**
     * Displays the main menu of the game.
     * Allows the player, designer or developer to start a run, open settings, or
     * quit.
     */
    private void showMainMenu() {
        // Removes all existing UI elements from the root container
        root.getChildren().clear();
        // Aligns page to the center
        log.setText("");
        Label title = new Label("WIZARD QUEST");
        // Opens the select difficulty page in same root container
        Button startBtn = new Button("Start New Game");
        // Opens the select difficulty page in same root container
        startBtn.setOnAction(e -> showDifficultySelect());
        Button settingsBtn = new Button("Settings");
        // Opens the settings page inside the same root container
        settingsBtn.setOnAction(e -> {
            root.getChildren().clear();
            root.setAlignment(Pos.CENTER_LEFT);

            SettingsPage settingsPage = new SettingsPage();
            VBox settingsView = settingsPage.createView(this::showMainMenu);

            root.getChildren().add(settingsView);
        });

        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(e -> System.exit(0));
        // Adds the title and buttons to the root layout so they appear in the UI
        root.getChildren().addAll(title, startBtn, settingsBtn, quitBtn);
    }

    // Player selects game difficulty
    private void showDifficultySelect() {
        // Removes all existing UI elements from the root container
        root.getChildren().clear();
        Label heading = new Label("SELECT DIFFICULTY");

        VBox buttons = new VBox(4);
        // Button for each difficulty
        for (DifficultyEnum d : DifficultyEnum.values()) {
            Button b = new Button(d + "  (Lives: " + settings.getStartingLives(d) + ")");
            b.setOnAction(e -> {
                // Initialise a new game run
                gameManager.startNewGame(d);
                GameRunInterface run = gameManager.getCurrentRun();
                if (run != null) {
                    telemetryListener.onStartSession(new StartSessionEvent(
                            settings.getUserID(), run.getSessionID(), Instant.now(), d));
                }
                // Move to encounter 1
                nextEncounter();
            });
            buttons.getChildren().add(b);
        }
        // Back button returns to main menu
        Button back = new Button("Back");
        back.setOnAction(e -> showMainMenu());
        // Adds the headings and buttons to the root layout so they appear in the UI/
        root.getChildren().addAll(heading, buttons, back);
    }

    // Advance game to the next encounter
    private void nextEncounter() {
        if (!gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }
        /**
         * Gets the next encounter and current player.
         * If either is missing the game ends and the end screen is shown.
         */
        currentEncounter = gameManager.pickEncounter();
        PlayerInterface player = gameManager.getCurrentPlayer();

        if (player == null || currentEncounter == null) {
            showEndScreen();
            return;
        }

        emitEncounterStartEvent();

        // Reset health and magic before starting a new encounter.
        player.resetHealth();
        player.resetMagic();
        player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));
        showEncounter();
    }

    // Displays current encounter and player stats.
    private void showEncounter() {
        root.getChildren().clear();
        root.setAlignment(Pos.TOP_CENTER);
        PlayerInterface player = gameManager.getCurrentPlayer();
        GameRunInterface run = gameManager.getCurrentRun();

        if (!gameManager.isGameRunning() || player == null) {
            showEndScreen();
            return;
        }

        // If the player died last turn, deducts 1 life, resets the enemies, or end if
        // no more lives.
        if (player.getHealth() <= 0) {
            gameManager.resetFailedEncounter();
            emitEncounterFailEvent(player.getLives());
            if (player.getLives() == 0) {
                showEndScreen();
                return;
            }
            player.resetHealth();
            player.resetMagic();
            player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));

        }
        // Determines current stage
        int stage = run != null ? run.getStage() : 1;
        Label heading = new Label("STAGE " + stage + " = " + currentEncounter.getType().getDisplayName());
        heading.setMaxWidth(Double.MAX_VALUE);

        // Centers heading
        heading.setAlignment(Pos.CENTER);

        Label statsText = new Label(
                "Lives: " + player.getLives() +
                "  Coins: " + player.getCoins()
        );

        Label hpLabel = new Label("HP: " + player.getHealth() + "/" + player.getMaxHealth());
        // HP Bar
        ProgressBar hpBar = new ProgressBar(
                (double) player.getHealth() / player.getMaxHealth()
        );
        hpBar.setPrefWidth(250);
        hpBar.setStyle("-fx-accent: green;");

        Label magicLabel = new Label("Magic: " + player.getMagic() + "/" + player.getMaxMagic());
        // Magic Bar
        ProgressBar magicBar = new ProgressBar(
                (double) player.getMagic() / player.getMaxMagic()
        );
        magicBar.setPrefWidth(250);
        magicBar.setStyle("-fx-accent: lightblue;");

        VBox playerStats = new VBox(4, statsText, hpLabel, hpBar, magicLabel, magicBar);

        VBox enemyList = new VBox(6);
        enemyList.getChildren().add(new Label("ENEMIES:"));

        EntityInterface[] enemies = currentEncounter.getEnemies();
        for (EntityInterface enemy : enemies) {
            if (enemy == null || enemy.getHealth() <= 0) {
                continue;
            }

            Label enemyLabel = new Label(
                    enemy.getType().getDisplayName() +
                    "  HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth()
            );

            ProgressBar enemyHpBar = new ProgressBar(
                    (double) enemy.getHealth() / enemy.getMaxHealth()
            );
            enemyHpBar.setPrefWidth(250);
            enemyHpBar.setStyle("-fx-accent: red;");

            enemyList.getChildren().addAll(enemyLabel, enemyHpBar);
        }

        // Ability VBox
        VBox abilityBox = new VBox(4);
        abilityBox.setAlignment(Pos.TOP_CENTER);
        abilityBox.getChildren().add(new Label("CHOOSE AN ABILITY:"));
        for (AbilityEnum ability : player.getAbilities()) {
            Button ab = new Button(ability.getDisplayName() + " (dmg:" + ability.getBaseDamage() + " cost:"
                    + ability.getMagicCost() + ")");
            ab.setOnAction(e -> showTargetSelection(ability));
            abilityBox.getChildren().add(ab);
        }

        playerStats.setMinWidth(280);
        abilityBox.setMinWidth(220);
        enemyList.setMinWidth(280);

        // HBox alignment
        HBox mainContent = new HBox(40);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(playerStats, abilityBox, enemyList);

        Button quitRun = new Button("Quit Run");
        quitRun.setOnAction(e -> showEndScreen());

        root.getChildren().addAll(heading, mainContent, log, quitRun);
    }

    // Shows alive enemies as clickable targets after the player picks an ability.
    private void showTargetSelection(AbilityEnum ability) {
        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player == null || !gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }

        if (player.getMagic() < ability.getMagicCost()) {
            log.setText("Not enough magic for " + ability.getDisplayName());
            return;
        }

        root.getChildren().clear();
        GameRunInterface run = gameManager.getCurrentRun();
        int stage = run != null ? run.getStage() : 1;
        Label heading = new Label("STAGE " + stage + " = " + currentEncounter.getType().getDisplayName());

        Label info = new Label("Using: " + ability.getDisplayName()
                + "  (dmg:" + ability.getBaseDamage()
                + " cost:" + ability.getMagicCost() + ")");

        VBox targetBox = new VBox(4);
        targetBox.getChildren().add(new Label("CHOOSE A TARGET:"));
        EntityInterface[] enemies = currentEncounter.getEnemies();
        for (EntityInterface enemy : enemies) {
            if (enemy == null || enemy.getHealth() <= 0)
                continue;
            Button tb = new Button(enemy.getType().getDisplayName()
                    + "  HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
            tb.setOnAction(e -> doPlayerTurn(ability, enemy));
            targetBox.getChildren().add(tb);
        }

        Button back = new Button("Back");
        back.setOnAction(e -> showEncounter());
        log.setText("");
        root.getChildren().addAll(heading, info, targetBox, log, back);
    }

    // Player attacks, then each enemy retalliates.
    private void doPlayerTurn(AbilityEnum ability, EntityInterface target) {
        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player == null || !gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }

        int hpBefore = target.getHealth();
        try {
            ability.execute(player, target);
        } catch (LackingResourceException ex) {
            log.setText("Not enough magic.");
            showEncounter();
            return;
        }
        int dmg = hpBefore - target.getHealth();

        // Record kill event if the target was killed
        if (target.getHealth() <= 0) {
            GameRunInterface killRun = gameManager.getCurrentRun();
            if (killRun != null) {
                telemetryListener.onKillEnemy(new KillEnemyEvent(
                        settings.getUserID(), killRun.getSessionID(), Instant.now(),
                        currentEncounter.getType(), killRun.getDifficulty(),
                        killRun.getStage(), target.getType()));
            }
        }

        StringBuilder msg = new StringBuilder();
        msg.append("You used ").append(ability.getDisplayName())
                .append(" on ").append(target.getType().getDisplayName())
                .append(" for ").append(dmg).append(" dmg.\n");

        EntityInterface[] enemies = currentEncounter.getEnemies();

        // If all enemies are dead, you win the encounter, get awarded coins and
        // advance.
        if (allDead(enemies)) {
            gameManager.completeCurrentEncounter();
            emitEncounterCompleteEvent(player.getHealth());
            player.gainCoins(Utils.COINS_GAINED);
            GameRunInterface coinRun = gameManager.getCurrentRun();
            if (coinRun != null) {
                telemetryListener.onGainCoin(new GainCoinEvent(
                        settings.getUserID(), coinRun.getSessionID(), Instant.now(),
                        currentEncounter.getType(), coinRun.getDifficulty(),
                        coinRun.getStage(), Utils.COINS_GAINED));
            }
            msg.append("Encounter won! +").append(Utils.COINS_GAINED).append(" coins.\n");
            log.setText(msg.toString());
            onEncounterWon();
            return;
        }
        // each alive enemy attacks the player
        for (EntityInterface enemy : enemies) {
            if (enemy == null || enemy.getHealth() <= 0)
                continue;
            AbilityEnum a = ai.pickAbility(enemy);
            if (a == null)
                continue;
            int pBefore = player.getHealth();
            try {
                a.execute(enemy, player);
            } catch (LackingResourceException ignored) {
            }
            int taken = pBefore - player.getHealth();
            msg.append(enemy.getType().getDisplayName()).append(" hit you for ").append(taken).append(" dmg.\n");
        }

        // Regeneration is capped at maximum.
        player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));
        log.setText(msg.toString());
        showEncounter();
    }

    // Advances stage, pre-picks the next encounter, then opens shop(or ends game).
    private void onEncounterWon() {
        if (!gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }
        GameRunInterface run = gameManager.getCurrentRun();
        if (run == null) {
            showEndScreen();
            return;
        }

        // if player finishes final stage
        if (run.getStage() >= 10) {
            showEndScreen();
            return;
        }

        // Save completed encounter info for shop telemetry
        EncounterEnum completedEncType = currentEncounter.getType();
        int completedStage = run.getStage();

        gameManager.advanceToNextLevel();
        if (!gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }

        EncounterInterface nextEnc = gameManager.pickEncounter();
        if (nextEnc == null) {
            showEndScreen();
            return;
        }

        currentEncounter = nextEnc;
        showShop(completedEncType, completedStage);
    }

    // Delegates to ShopPage, on leave resets the player and starts the next
    // encounter
    private void showShop(EncounterEnum completedEncType, int completedStage) {
        if (!gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }
        GameRunInterface shopRun = gameManager.getCurrentRun();
        ShopPage shopPage = new ShopPage(gameManager, root, log,
                telemetryListener, settings.getUserID(),
                shopRun != null ? shopRun.getSessionID() : 0,
                completedEncType,
                shopRun != null ? shopRun.getDifficulty() : DifficultyEnum.EASY,
                completedStage, () -> {
                    if (!gameManager.isGameRunning()) {
                        showEndScreen();
                        return;
                    }
                    PlayerInterface player = gameManager.getCurrentPlayer();
                    if (player == null || currentEncounter == null) {
                        showEndScreen();
                        return;
                    }
                    // Reset health and magic before starting a new encounter
                    player.resetHealth();
                    player.resetMagic();
                    player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));
                    emitEncounterStartEvent();
                    showEncounter();
                });
        shopPage.show();
    }

    // Displays the end screen, shows the run summary
    private void showEndScreen() {
        root.getChildren().clear();
        GameRunInterface run = gameManager.getCurrentRun();
        PlayerInterface player = gameManager.getCurrentPlayer();

        Label heading = new Label("RUN COMPLETE");
        StringBuilder info = new StringBuilder();
        if (run != null) {
            info.append("Stage reached: ").append(run.getStage())
                    .append("  Deaths: ").append(run.getDeathCount());
        }
        if (player != null) {
            info.append("  Coins: ").append(player.getCoins());
        }
        Label stats = new Label(info.toString());

        if (run != null) {
            telemetryListener.onEndSession(new EndSessionEvent(
                    settings.getUserID(), run.getSessionID(), Instant.now()));
        }
        gameManager.endGame();

        Button back = new Button("Main Menu");
        back.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(heading, stats, back);
    }

    // Return true if every enemy is null or has 0 HP
    private boolean allDead(EntityInterface[] enemies) {
        for (EntityInterface e : enemies) {
            if (e != null && e.getHealth() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isBossEncounter(EncounterEnum type) {
        return type == EncounterEnum.EVIL_WIZARD_ENCOUNTER
                || type == EncounterEnum.GHOST_ENCOUNTER
                || type == EncounterEnum.BLACK_KNIGHT_ENCOUNTER
                || type == EncounterEnum.DRAGON_ENCOUNTER;
    }

    private void emitEncounterStartEvent() {
        GameRunInterface run = gameManager.getCurrentRun();
        if (run == null || currentEncounter == null)
            return;
        String userID = settings.getUserID();
        int sessionID = run.getSessionID();
        EncounterEnum type = currentEncounter.getType();
        DifficultyEnum diff = run.getDifficulty();
        int stage = run.getStage();
        if (isBossEncounter(type)) {
            telemetryListener.onBossEncounterStart(new BossEncounterStartEvent(
                    userID, sessionID, Instant.now(), type, diff, stage));
        } else {
            telemetryListener.onNormalEncounterStart(new NormalEncounterStartEvent(
                    userID, sessionID, Instant.now(), type, diff, stage));
        }
    }

    private void emitEncounterCompleteEvent(int playerHPRemaining) {
        GameRunInterface run = gameManager.getCurrentRun();
        if (run == null || currentEncounter == null)
            return;
        String userID = settings.getUserID();
        int sessionID = run.getSessionID();
        EncounterEnum type = currentEncounter.getType();
        DifficultyEnum diff = run.getDifficulty();
        int stage = run.getStage();
        if (isBossEncounter(type)) {
            telemetryListener.onBossEncounterComplete(new BossEncounterCompleteEvent(
                    userID, sessionID, Instant.now(), type, diff, stage, playerHPRemaining));
        } else {
            telemetryListener.onNormalEncounterComplete(new NormalEncounterCompleteEvent(
                    userID, sessionID, Instant.now(), type, diff, stage, playerHPRemaining));
        }
    }

    private void emitEncounterFailEvent(int livesLeft) {
        GameRunInterface run = gameManager.getCurrentRun();
        if (run == null || currentEncounter == null)
            return;
        String userID = settings.getUserID();
        int sessionID = run.getSessionID();
        EncounterEnum type = currentEncounter.getType();
        DifficultyEnum diff = run.getDifficulty();
        int stage = run.getStage();
        if (isBossEncounter(type)) {
            telemetryListener.onBossEncounterFail(new BossEncounterFailEvent(
                    userID, sessionID, Instant.now(), type, diff, stage, livesLeft));
        } else {
            telemetryListener.onNormalEncounterFail(new NormalEncounterFailEvent(
                    userID, sessionID, Instant.now(), type, diff, stage, livesLeft));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
