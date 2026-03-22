package wizardquest.ui;

import java.time.Instant;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import wizardquest.abilities.AbilityEnum;
import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.Authenticator;
import wizardquest.auth.RoleEnum;
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
import wizardquest.gamemanager.SimulatedGameRun;
import wizardquest.gamemanager.Utils;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.BossEncounterCompleteEvent;
import wizardquest.telemetry.BossEncounterFailEvent;
import wizardquest.telemetry.BossEncounterStartEvent;
import wizardquest.telemetry.EndSessionEvent;
import wizardquest.telemetry.GainCoinEvent;
import wizardquest.telemetry.KillEnemyEvent;
import wizardquest.telemetry.NormalEncounterCompleteEvent;
import wizardquest.telemetry.NormalEncounterFailEvent;
import wizardquest.telemetry.NormalEncounterStartEvent;
import wizardquest.telemetry.StartSessionEvent;
import wizardquest.telemetry.TelemetryListenerInterface;
import wizardquest.telemetry.TelemetryListenerSingleton;

public class GameRunPage extends Application {

    // Access to core game systems through their singleton interfaces
    private final GameManagerInterface gameManager = GameManagerSingleton.getInstance();
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final EntityAIInterface ai = EntityAISingleton.getInstance();
    private final TelemetryListenerInterface telemetryListener = TelemetryListenerSingleton.getInstance();

    // Styling

    private static final String PANEL_STYLE =
        "-fx-background-color: #2b2d31;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #5865F2;" +
        "-fx-border-radius: 10;";

    private static final String TITLE_STYLE =
        "-fx-font-family: 'JetBrains Mono';" +
        "-fx-font-size: 26px;" +
        "-fx-text-fill: #9a7cff;" +
        "-fx-font-weight: bold;";

    private static final String HEADING_STYLE =
            "-fx-font-family: 'JetBrains Mono';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #5865F2;" +
            "-fx-font-weight: bold;";

    private static final String TEXT_STYLE =
            "-fx-font-family: 'JetBrains Mono';" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #f2f3f5;";

    private static final String SECONDARY_TEXT_STYLE =
            "-fx-font-family: 'JetBrains Mono';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #b5bac1;";

    private static final String PRIMARY_BUTTON_STYLE =
        "-fx-background-color: #5865F2;" +
        "-fx-text-fill: #f2f3f5;" +
        "-fx-font-family: 'JetBrains Mono';" +
        "-fx-font-size: 14px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;" +
        "-fx-border-radius: 8;" +
        "-fx-border-color: #7c84f7;" +
        "-fx-padding: 8 16 8 16;" +
        "-fx-cursor: hand;";

private static final String SECONDARY_BUTTON_STYLE =
        "-fx-background-color: #404249;" +
        "-fx-text-fill: #f2f3f5;" +
        "-fx-font-family: 'JetBrains Mono';" +
        "-fx-font-size: 14px;" +
        "-fx-background-radius: 8;" +
        "-fx-border-radius: 8;" +
        "-fx-border-color: #5865F2;" +
        "-fx-padding: 8 16 8 16;" +
        "-fx-cursor: hand;";

private static final String DANGER_BUTTON_STYLE =
        "-fx-background-color: #ed4245;" +
        "-fx-text-fill: #f2f3f5;" +
        "-fx-font-family: 'JetBrains Mono';" +
        "-fx-font-size: 14px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;" +
        "-fx-border-radius: 8;" +
        "-fx-border-color: #ff6b6b;" +
        "-fx-padding: 8 16 8 16;" +
        "-fx-cursor: hand;";

    private VBox root;
    private final Label log = new Label("");

    private final ProgressBar hpBar = new ProgressBar();
    private final ProgressBar magicBar = new ProgressBar();
    private ProgressBar[] enemyHpBars;

    private EncounterInterface currentEncounter;

    @Override
    public void start(Stage stage) {
        FontLoader.loadFonts();
        // Main container for swapping between different frames
        root = new VBox(8);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #1e1f22;");
        // First screen shown when the game launches
        showLoginPage();
        
        stage.setScene(new Scene(root, 1600, 900));
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.setTitle("WizardQuest");
        stage.show();
    }

    /**
     * Displays the login screen where the user authenticates via SSO. Once
     * authenticated, the user is taken to the main menu.
     */
    private void showLoginPage() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1f22;");

        Label title = new Label("WizardQuest");
        title.setFont(Font.font("JetBrains Mono", 26));
        Label status = new Label("");

        title.setStyle(TITLE_STYLE);
        Button loginBtn = new Button("Login with SSO");
        loginBtn.setStyle(PRIMARY_BUTTON_STYLE);

        loginBtn.setOnAction(e -> {
            // loginBtn.setDisable(true);
            status.setText("Opening browser...");
            status.setStyle(SECONDARY_TEXT_STYLE);
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
     * Displays the main menu of the game. Allows the player, designer or
     * developer to start a run, open settings, or quit.
     */
    private void showMainMenu() {
        // Removes all existing UI elements from the root container
        root.getChildren().clear();
        // Aligns page to the center
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1f22;");

        log.setText("");
        Label title = new Label("WIZARD QUEST");
        title.setStyle(TITLE_STYLE);
        Label telemetryNote = new Label("To view the telemetry disclosure, please open Settings.");
        telemetryNote.setStyle(SECONDARY_TEXT_STYLE);
        // Opens the select difficulty page in same root container
        Button startBtn = new Button("Start New Game");
        // Opens the select difficulty page in same root container
        startBtn.setOnAction(e -> showDifficultySelect());
        Button settingsBtn = new Button("Settings");
        Button simBtn = new Button("Run Simulations");
        simBtn.setOnAction(e -> {
            for (DifficultyEnum d : DifficultyEnum.values()) {
                gameManager.startNewGame(d);
                new SimulatedGameRun(
                        this.gameManager.getCurrentDifficulty(),
                        "../event_logs/simulation_events.json");
                gameManager.endGame();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Simulations");
            alert.setContentText("Simulations have been executed.");
            alert.showAndWait();
        });

        Button quitBtn = new Button("Quit");
        quitBtn.setStyle(DANGER_BUTTON_STYLE);
        quitBtn.setOnAction(e -> System.exit(0));

        try {
            RoleEnum role = settings.getUserRole();

            if (role == RoleEnum.DEVELOPER) {
                Button rolesBtn = new Button("Manage Roles");
                rolesBtn.setStyle(PRIMARY_BUTTON_STYLE);
                rolesBtn.setOnAction(e -> showRoleManagementPage());

                root.getChildren().addAll(title, startBtn, simBtn, settingsBtn, rolesBtn, quitBtn, telemetryNote);
            } else {
                root.getChildren().addAll(title, startBtn, simBtn, settingsBtn, quitBtn, telemetryNote);
            }
        } catch (AuthenticationException e) {
            root.getChildren().addAll(title, startBtn, simBtn, settingsBtn, quitBtn, telemetryNote);
        }

        

        startBtn.setStyle(PRIMARY_BUTTON_STYLE);
        simBtn.setStyle(PRIMARY_BUTTON_STYLE);
        settingsBtn.setStyle(PRIMARY_BUTTON_STYLE);
        // Opens the settings page inside the same root container
        settingsBtn.setOnAction(e -> {
            root.getChildren().clear();
            root.setAlignment(Pos.CENTER_LEFT);

            SettingsPage settingsPage = new SettingsPage();
            VBox settingsView = settingsPage.createView(this::showMainMenu);

            root.getChildren().add(settingsView);
        });

    }

    private void showRoleManagementPage() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1f22;");

        RoleManagementPage rolePage = new RoleManagementPage();
        VBox roleView = rolePage.createView(this::showMainMenu);

        root.getChildren().add(roleView);
    }

    // Player selects game difficulty
    private void showDifficultySelect() {
        // Removes all existing UI elements from the root container
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1f22;");


        Label heading = new Label("SELECT DIFFICULTY");
        heading.setStyle(HEADING_STYLE);

        VBox buttons = new VBox(4);
        buttons.setAlignment(Pos.CENTER);

        // Button for each difficulty
        for (DifficultyEnum d : DifficultyEnum.values()) {
            int bestStage = 0;
            try {
                bestStage = settings.getMaxStageReached(d);
            } catch (AuthenticationException ex) {
                bestStage = 0;
            }
            // Displays lives available and best stage reached by the current user
            Button b = new Button(
                    d +
                    "  (Lives: " + settings.getStartingLives(d) + ")" +
                    "  [Best Stage: " + bestStage + "]"
            );
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
            b.setStyle(PRIMARY_BUTTON_STYLE);
            buttons.getChildren().add(b);
        }
        // Back button returns to main menu
        Button back = new Button("Back");
        back.setStyle(SECONDARY_BUTTON_STYLE);
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

        currentEncounter = gameManager.pickEncounter();
        PlayerInterface player = gameManager.getCurrentPlayer();

        if (player == null || currentEncounter == null) {
            showEndScreen();
            return;
        }

        EntityInterface[] enemies = currentEncounter.getEnemies();
        enemyHpBars = new ProgressBar[enemies.length];

        // Loops through each enemy HP bar
        for (int i = 0; i < enemies.length; i++) {
            EntityInterface enemy = enemies[i];
            if (enemy != null) {
                enemyHpBars[i] = new ProgressBar((double) enemy.getHealth() / enemy.getMaxHealth());
                enemyHpBars[i].setPrefWidth(250);
                enemyHpBars[i].setStyle("-fx-accent: red;");
            }
        }

        emitEncounterStartEvent();

        player.resetHealth();
        player.resetMagic();
        player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));

        hpBar.setProgress((double) player.getHealth() / player.getMaxHealth());
        magicBar.setProgress((double) player.getMagic() / player.getMaxMagic());

        showEncounter();
    }

    private void showBattleScreen(VBox middleBox) {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1f22;");

        PlayerInterface player = gameManager.getCurrentPlayer();
        VBox abilityList = new VBox(3);
        // Shows player abilities
        Label abilitiesHeading = new Label("Abilities:");
        abilitiesHeading.setStyle(HEADING_STYLE);
        abilityList.getChildren().add(abilitiesHeading);

        //Returns display name for each abilitiy (excluding resistance)
        for (AbilityEnum ability : player.getAbilities()) {
            Label abilityLabel = new Label(ability.getDisplayName());
            abilityLabel.setStyle(TEXT_STYLE);
            abilityList.getChildren().add(abilityLabel);
        }

        GameRunInterface run = gameManager.getCurrentRun();

        if (!gameManager.isGameRunning() || player == null || currentEncounter == null) {
            showEndScreen();
            return;
        }

        // Determines current stage
        int stage = run != null ? run.getStage() : 1;

        String encounterName = currentEncounter.getType().getDisplayName();
        // Checks for boss encounter
        if (isBossEncounter(currentEncounter.getType())) {
            encounterName = "BOSS ENCOUNTER - " + encounterName;
        }

        Label heading = new Label("STAGE " + stage + " = " + encounterName);
        heading.setStyle(HEADING_STYLE);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setAlignment(Pos.CENTER);

        // Displays player stats
        Label statsText = new Label(
                "Lives: " + player.getLives()
                + "  Coins: " + player.getCoins()
        );
        statsText.setStyle(TEXT_STYLE);

        Label hpLabel = new Label("Health: " + player.getHealth() + "/" + player.getMaxHealth());
        hpLabel.setStyle(TEXT_STYLE);
        // HP bar styling
        hpBar.setPrefWidth(250);
        hpBar.setStyle("-fx-accent: green;"+
            "-fx-control-inner-background: #1e1f22;" +
            "-fx-background-color: #1e1f22;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-border-color: #5865F2;"
        );
        animateProgressBar(hpBar, (double) player.getHealth() / player.getMaxHealth());

        Label magicLabel = new Label("Magic: " + player.getMagic() + "/" + player.getMaxMagic());
        magicLabel.setStyle(TEXT_STYLE);

        // Magic bar stylings
        magicBar.setPrefWidth(250);
        magicBar.setStyle("-fx-accent: lightblue;"+
            "-fx-control-inner-background: #1e1f22;" +
            "-fx-background-color: #1e1f22;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-border-color: #5865F2;"
        );
        animateProgressBar(magicBar, (double) player.getMagic() / player.getMaxMagic());

        VBox playerStats = new VBox(4, statsText, hpLabel, hpBar, magicLabel, magicBar, abilityList);
        playerStats.setPadding(new Insets(12));
        playerStats.setStyle(PANEL_STYLE);

        VBox enemyList = new VBox(6);

        Label enemiesHeading = new Label("ENEMIES:");
        enemiesHeading.setStyle(HEADING_STYLE);
        enemyList.getChildren().add(enemiesHeading);

        enemyList.setPadding(new Insets(12));
        enemyList.setStyle(PANEL_STYLE);

        // Loops through enemy list and returns them
        EntityInterface[] enemies = currentEncounter.getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            EntityInterface enemy = enemies[i];

            if (enemy == null || enemy.getHealth() <= 0) {
                continue;
            }

            Label enemyLabel = new Label(
                    enemy.getType().getDisplayName()
                    + "  Health: " + enemy.getHealth() + "/" + enemy.getMaxHealth()
            );
            enemyLabel.setStyle(TEXT_STYLE);

            ProgressBar enemyHpBar = enemyHpBars[i];
            enemyHpBar.setPrefWidth(250);
            enemyHpBar.setStyle(
            "-fx-accent: red;" +
            "-fx-control-inner-background: #1e1f22;" +
            "-fx-background-color: #1e1f22;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;"
        );
            animateProgressBar(enemyHpBar, (double) enemy.getHealth() / enemy.getMaxHealth());

            enemyList.getChildren().addAll(enemyLabel, enemyHpBar);
        }
        // Sets styling for playerStats
        playerStats.setMinWidth(400);
        playerStats.setPrefWidth(400);
        playerStats.setMaxWidth(400);
        // Sets styling for enemyList
        enemyList.setMinWidth(400);
        enemyList.setPrefWidth(400);
        enemyList.setMaxWidth(400);
        // Sets styling for middleBox
        middleBox.setMinWidth(320);
        middleBox.setPrefWidth(320);
        middleBox.setMaxWidth(320);
        middleBox.setMinHeight(550);
        middleBox.setPrefHeight(550);
        middleBox.setMaxHeight(550);
        middleBox.setAlignment(Pos.TOP_CENTER);
        middleBox.setPadding(new Insets(12));
        middleBox.setStyle(PANEL_STYLE);

        // Ensures that log fits across different aspect ratios
        log.setStyle(SECONDARY_TEXT_STYLE);
        log.setWrapText(true);
        log.setAlignment(Pos.CENTER);
        log.setMinHeight(45);
        log.setPrefHeight(45);
        log.setMaxHeight(45);
        log.setMinWidth(500);
        log.setPrefWidth(500);
        log.setMaxWidth(500);

        HBox mainContent = new HBox(40);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().addAll(playerStats, middleBox, enemyList);

        Button quitRun = new Button("Quit Run");
        quitRun.setOnAction(e -> showEndScreen());
        quitRun.setStyle(DANGER_BUTTON_STYLE);

        root.getChildren().addAll(heading, mainContent, log, quitRun);
    }

    // Displays current encounter and player stats.
    private void showEncounter() {
        PlayerInterface player = gameManager.getCurrentPlayer();

        if (!gameManager.isGameRunning() || player == null) {
            showEndScreen();
            return;
        }

        // If the player died last turn, deducts 1 life, resets the enemies, or end if
        // no more lives.
        if (player.getHealth() <= 0) {
            gameManager.resetFailedEncounter();
            // Checks if player has run out of lives
            if (player.getLives() == 0) {
                log.setText("You died. No lives remaining.");
                showEndScreen();
                return;
            }

            log.setText("You died. You lost 1 life. Lives remaining: " + player.getLives());
            player.resetHealth();
            player.resetMagic();
            player.gainMagic(Math.min(player.getMagicRegenRate(), player.getMaxMagic() - player.getMagic()));
        }

        VBox abilityBox = new VBox(4);
        abilityBox.setMinWidth(320);
        abilityBox.setPrefWidth(320);
        abilityBox.setMaxWidth(320);
        abilityBox.setAlignment(Pos.TOP_CENTER);
        Label chooseAbilityLabel = new Label("CHOOSE AN ABILITY:");
        chooseAbilityLabel.setStyle(HEADING_STYLE);
        chooseAbilityLabel.setAlignment(Pos.TOP_CENTER);
        abilityBox.getChildren().add(chooseAbilityLabel);
        // Loops through abilities and displays their Name/Damage/Cost
        for (AbilityEnum ability : player.getAbilities()) {
            Button ab = new Button(
                    ability.getDisplayName() + "\n" +
                    "Damage: " + ability.getBaseDamage() + "   Cost: " + ability.getMagicCost()
                );
            ab.setOnAction(e -> showTargetSelection(ability));
            abilityBox.getChildren().add(ab);
            ab.setStyle(PRIMARY_BUTTON_STYLE);
            ab.setWrapText(true);
            ab.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            ab.setAlignment(Pos.TOP_CENTER);
            ab.setPrefWidth(300);
            ab.setMinHeight(70);
            ab.setStyle(PRIMARY_BUTTON_STYLE +
            "-fx-line-spacing: 4px;");
            
        }
        
        
        showBattleScreen(abilityBox);
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
            showEncounter();
            return;
        }

        VBox targetBox = new VBox(4);
        targetBox.setAlignment(Pos.CENTER);

        Label chooseTargetLabel = new Label("CHOOSE A TARGET:");
        chooseTargetLabel.setStyle(HEADING_STYLE);

        Label usingLabel = new Label(
                "Using: " + ability.getDisplayName()
                + " (Damage:" + ability.getBaseDamage()
                + " Cost:" + ability.getMagicCost() + ")"
        );

        usingLabel.setStyle(SECONDARY_TEXT_STYLE);

        targetBox.getChildren().addAll(
                chooseTargetLabel,
                usingLabel
        );

        EntityInterface[] enemies = currentEncounter.getEnemies();
        for (EntityInterface enemy : enemies) {
            if (enemy == null || enemy.getHealth() <= 0) {
                continue;
            }

            Button tb = new Button(
                    enemy.getType().getDisplayName()
                    + "\nHealth: " + enemy.getHealth() + "/" + enemy.getMaxHealth()
            );
            tb.setOnAction(e -> doPlayerTurn(ability, enemy));
            targetBox.getChildren().add(tb);
            tb.setStyle(PRIMARY_BUTTON_STYLE);
            tb.setPrefWidth(300);
            tb.setMinHeight(70);
        }

        Button chooseAnother = new Button("Choose Another Ability");
        chooseAnother.setOnAction(e -> showEncounter());
        targetBox.getChildren().add(chooseAnother);
        chooseAnother.setStyle(SECONDARY_BUTTON_STYLE);

        log.setText("");
        showBattleScreen(targetBox);
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
                .append(" for ").append(dmg).append(" Damage.\n");

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
            if (enemy == null || enemy.getHealth() <= 0) {
                continue;
            }
            AbilityEnum a = ai.pickAbility(enemy);
            if (a == null) {
                continue;
            }
            int pBefore = player.getHealth();
            try {
                a.execute(enemy, player);
            } catch (LackingResourceException ignored) {
            }
            int taken = pBefore - player.getHealth();
            msg.append(enemy.getType().getDisplayName()).append(" hit you for ").append(taken).append(" Damage.\n");
        }

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

                    EntityInterface[] enemies = currentEncounter.getEnemies();
                    enemyHpBars = new ProgressBar[enemies.length];

                    for (int i = 0; i < enemies.length; i++) {
                        EntityInterface enemy = enemies[i];
                        if (enemy != null) {
                            enemyHpBars[i] = new ProgressBar((double) enemy.getHealth() / enemy.getMaxHealth());
                            enemyHpBars[i].setPrefWidth(250);
                            enemyHpBars[i].setStyle("-fx-accent: red;");
                        }
                    }

                    emitEncounterStartEvent();
                    showEncounter();
                });
        shopPage.show();
    }

    // Displays the end screen, shows the run summary
    private void showEndScreen() {
        root.getChildren().clear();
        root.setStyle("-fx-background-color: #1e1f22;");

        GameRunInterface run = gameManager.getCurrentRun();
        PlayerInterface player = gameManager.getCurrentPlayer();

        Label heading = new Label("RUN COMPLETE");
        heading.setStyle(HEADING_STYLE);
        StringBuilder info = new StringBuilder();
        if (run != null) {
            info.append("Stage reached: ").append(run.getStage())
                    .append("  Deaths: ").append(run.getDeathCount());
            
        }
        if (player != null) {
            info.append("  Coins: ").append(player.getCoins());
        }
        Label stats = new Label(info.toString());
        stats.setStyle(SECONDARY_TEXT_STYLE);


        if (run != null) {
            try {
                DifficultyEnum difficulty = gameManager.getCurrentDifficulty();
                if (difficulty != null) {
                    int reachedStage = Math.min(run.getStage(), 10);
                    settings.setMaxStageReached(difficulty, reachedStage);
                }
            } catch (AuthenticationException ignored) {
            }
        }

        if (run != null) {
            telemetryListener.onEndSession(new EndSessionEvent(
                    settings.getUserID(), run.getSessionID(), Instant.now()));
        }
        gameManager.endGame();

        Button back = new Button("Main Menu");
        back.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(heading, stats, back);
        back.setStyle(SECONDARY_BUTTON_STYLE);
        back.setPrefWidth(180);
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

    private void animateProgressBar(ProgressBar bar, double targetProgress) {
        targetProgress = Math.max(0, Math.min(1, targetProgress));

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(bar.progressProperty(), targetProgress)
                )
        );
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
