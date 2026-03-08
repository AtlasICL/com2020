package wizardquest.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.Authenticator;
import wizardquest.entity.EntityAIInterface;
import wizardquest.entity.EntityAISingleton;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.EncounterInterface;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.gamemanager.GameRunInterface;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class GameRunPage extends Application {
    
    // Access to core game systems through their singleton interfaces
    private final GameManagerInterface gameManager = GameManagerSingleton.getInstance();
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final EntityAIInterface ai = EntityAISingleton.getInstance();
    private VBox root; // Root container for entire UI

    @Override
    public void start(Stage stage) {
        root = new VBox(8); // Main container for swapping between different frames
        root.setPadding(new Insets(12));
        showLoginPage(); // First screen shown when the game launches
        stage.setScene(new Scene(root, 800, 700));
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

        Button loginBtn = new Button("Login with SSO");

        loginBtn.setOnAction(e -> {
            try {
                // Launch OAuth authentication through the Python authenticator
                Authenticator auth = new Authenticator();
                AuthenticationResult result = auth.login();

                // Load authenticated user settings into the settings system
                settings.loginWithResult(result);

                // Continue to main menu after successful login
                showMainMenu();
            } catch (AuthenticationException ex) {
                // Display login error to the user
                Label error = new Label("Login failed: " + ex.getMessage());
                root.getChildren().add(error);
            }
        });

        root.getChildren().addAll(title, loginBtn);
    }

    /**
     * Displays the main menu of the game.
     * Allows the player, designer or developer to start a run, open settings, or quit.
     */
    private void showMainMenu() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);

        Label title = new Label("WIZARD QUEST");

        Button startBtn = new Button("Start New Game");
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

        root.getChildren().addAll(title, startBtn, settingsBtn, quitBtn);
    }

    // Player selects game difficulty
    private void showDifficultySelect() {
        root.getChildren().clear();
        Label heading = new Label("SELECT DIFFICULTY");

        VBox buttons = new VBox(4);
        // Button for each difficulty
        for (DifficultyEnum d : DifficultyEnum.values()) {
            Button b = new Button(d + "  (Lives: " + settings.getStartingLives(d) + ")");
            b.setOnAction(e -> {
                // Initialise a new game run
                gameManager.startNewGame(d);
                // Move to encounter 1
                nextEncounter();
            });
            buttons.getChildren().add(b);
        }

        Button back = new Button("Back");
        back.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(heading, buttons, back);
    }

    // Advance game to the next encounter
    private void nextEncounter() {
        if (!gameManager.isGameRunning()) {
            showEndScreen();
            return;
        }
        EncounterInterface encounter = gameManager.pickEncounter();
        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player == null || encounter == null) {
            gameManager.endGame();
            showEndScreen();
            return;
        }

        //Reset health and magic before starting a new encounter
        player.resetHealth();
        player.resetMagic();
        showEncounter(encounter);
    }

    // Displays the end screen
    private void showEndScreen(){
        root.getChildren().clear();
        Label h = new Label("RUN COMPLETE");
        root.getChildren().add(h);
    }

    // Displays current encounter and player stats
    private void showEncounter(EncounterInterface encounter) {
        root.getChildren().clear();
        PlayerInterface player = gameManager.getCurrentPlayer();
        GameRunInterface run = gameManager.getCurrentRun();
        if (player == null) {
            gameManager.endGame();
            showEndScreen();
            return;
        }

        //Determines current stage
        int stage;
        if (run != null) {
            stage = run.getStage();
        } 
        else {
            stage = 1;
        }

        Label h = new Label("STAGE " + stage + " — " + encounter.getType().getDisplayName());

        // Display of basic player stats
        Label playerStats = new Label(
                "HP: " + player.getHealth() + "/" + player.getMaxHealth() +
                        "Magic:" + player.getMagic() + "/" + player.getMaxMagic() +
                        "Lives: " + player.getLives() +
                        "Coins: " + player.getCoins()
        );
        // TODO (KK): need to show enemy list and ability buttons

        root.getChildren().addAll(h, playerStats);
    }
}
