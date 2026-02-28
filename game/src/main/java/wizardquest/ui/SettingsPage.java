package wizardquest.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class SettingsPage extends Application {

    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final Label output = new Label("");

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Load Settings");
        btn.setOnAction(e -> {
            DifficultyEnum d = DifficultyEnum.EASY;
            output.setText(
                "Max Health: " + settings.getPlayerMaxHealth(d) + "\n" +
                "Starting Lives: " + settings.getStartingLives(d)
            );
        });

        VBox root = new VBox(10, btn, output);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root));
        stage.setTitle("Settings Page");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}