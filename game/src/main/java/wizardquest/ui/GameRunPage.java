/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wizardquest.ui;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.EntityAIInterface;
import wizardquest.entity.EntityAISingleton;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.EncounterInterface;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.gamemanager.GameRunInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class GameRunPage extends Application {

    private final GameManagerInterface gameManager = GameManagerSingleton.getInstance();
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final EntityAIInterface ai = EntityAISingleton.getInstance();
    private VBox root;
    @Override
    public void start(Stage stage) {
        root = new VBox(8);
        root.setPadding(new Insets(12));
        showMainMenu();
        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle("WizardQuest");
        stage.show();
    }
    private void showMainMenu() {
        root.getChildren().clear();
        Label title = new Label("WIZARD QUEST");

        Button startBtn = new Button("Start New Game");
        startBtn.setOnAction(e -> showDifficultySelect());

        Button settingsBtn = new Button("Settings");
        settingsBtn.setOnAction(e -> showSettings());

        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(e -> System.exit(0));

        root.getChildren().addAll(title, startBtn, settingsBtn, quitBtn);
    }

    private void showSettings() {
        root.getChildren().clear();
        Label heading = new Label("SETTINGS");

        StringBuilder sb = new StringBuilder();
        for (DifficultyEnum d : DifficultyEnum.values()) {
            sb.append(d).append(": HP=").append(settings.getPlayerMaxHealth(d))
                    .append(" Lives=").append(settings.getStartingLives(d))
                    .append(" EnemyDmg=").append(settings.getEnemyDamageMultiplier(d))
                    .append("\n");
        }
        Label info = new Label(sb.toString());
        Button back = new Button("Back");
        back.setOnAction(e -> showMainMenu());
        root.getChildren().addAll(heading, info, back);
    }

    private void showDifficultySelect() {
        root.getChildren().clear();
        Label heading = new Label("SELECT DIFFICULTY");

        VBox buttons = new VBox(4);
        for (DifficultyEnum d : DifficultyEnum.values()) {
            Button b = new Button(d + "  (Lives: " + settings.getStartingLives(d) + ")");
            b.setOnAction(e -> {
                gameManager.startNewGame(d);
                nextEncounter();
            });
            buttons.getChildren().add(b);
        }

        Button back = new Button("Back");
        back.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(heading, buttons, back);
    }

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
        player.resetHealth();
        player.resetMagic();
        showEncounter(encounter);
    }

    private void showEndScreen(){
        root.getChildren().clear();
        Label h = new Label("RUN COMPLETE");


        root.getChildren().add(h);
    }
    private void showEncounter(EncounterInterface encounter) {
        root.getChildren().clear();
        PlayerInterface player = gameManager.getCurrentPlayer();
        GameRunInterface run = gameManager.getCurrentRun();
        if (player == null) {
            gameManager.endGame();
            showEndScreen();
            return;
        }
        int stage;
        if (run!=null)
            stage = run.getStage();
        else
            stage = 1;

        Label h = new Label("STAGE " + stage + " — " + encounter.getType().getDisplayName());

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
