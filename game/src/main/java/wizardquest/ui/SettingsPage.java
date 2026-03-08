package wizardquest.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class SettingsPage {

    private final SettingsInterface settings = SettingsSingleton.getInstance(); // Access settings to read and update values
    private final Label output = new Label("");

    public VBox createView(Runnable backAction) {
        Label roleLabel = new Label();

        try {
            roleLabel.setText("Role: " + settings.getUserRole()); // Display the user's role at the top of the settings page
        } catch (AuthenticationException e) {
            roleLabel.setText("Role: UNKNOWN");
        }

        Label telemetryLabel = new Label("Telemetry: OFF"); // Displays current telemetry state
        try {
            telemetryLabel.setText("Telemetry: " + (settings.isTelemetryEnabled() ? "ON" : "OFF"));
        } catch (Exception e) {
            telemetryLabel.setText("Telemetry: OFF");
        }

        Button toggleTelemetryButton = new Button("Toggle Telemetry"); // Button to toggle telemetry on and off
        toggleTelemetryButton.setOnAction(e -> {
            try {
                boolean current = settings.isTelemetryEnabled();
                settings.setTelemetryEnabled(!current);
                telemetryLabel.setText("Telemetry: " + (!current ? "ON" : "OFF"));
            } catch (Exception ex) {
                telemetryLabel.setText("Telemetry: OFF");
            }
        });
        // Disclosure explaining telemetry data collection to the user
        Label telemetryDisclosure = new Label(
            "If telemetry is enabled, the game records events anonymously to help balance difficulty and improve gameplay.\n\n" +
            "This includes:\n" +
            "• timestamp, session id, user id\n" +
            "• encounter start, completion, or fail (stage, difficulty, encounter name)\n" +
            "• player state on completion or fail (health remaining, lives left if relevant)\n" +
            "• coins gained and upgrades purchased\n" +
            "• settings changes (new values)\n" +
            "• enemies defeated\n\n" +
            "Why we collect it:\n" +
            "• to spot difficulty spikes and unfair encounters\n" +
            "• to support balancing changes with evidence"
        );

        telemetryDisclosure.setWrapText(true);
        // Text fields for each editable design parameter (pre-populated with current values)
        TextField easyHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.EASY)));
        TextField easyLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.EASY)));
        TextField easyEnemyDmgField = new TextField(String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.EASY)));

        TextField mediumHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM)));
        TextField mediumLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.MEDIUM)));
        TextField mediumEnemyDmgField = new TextField(String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.MEDIUM)));

        TextField hardHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.HARD)));
        TextField hardLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.HARD)));
        TextField hardEnemyDmgField = new TextField(String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.HARD)));

        // Input validation to ensure only valid values can be entered
        makeIntegerOnly(easyLivesField);
        makeIntegerOnly(mediumLivesField);
        makeIntegerOnly(hardLivesField);

        makeIntegerOnly(easyHpField);
        makeIntegerOnly(mediumHpField);
        makeIntegerOnly(hardHpField);

        makeDecimalOnly(easyEnemyDmgField);
        makeDecimalOnly(mediumEnemyDmgField);
        makeDecimalOnly(hardEnemyDmgField);

        // Fixing widths of text fields for better formatting
        easyHpField.setPrefWidth(60);
        easyLivesField.setPrefWidth(60);
        easyEnemyDmgField.setPrefWidth(60);

        mediumHpField.setPrefWidth(60);
        mediumLivesField.setPrefWidth(60);
        mediumEnemyDmgField.setPrefWidth(60);

        hardHpField.setPrefWidth(60);
        hardLivesField.setPrefWidth(60);
        hardEnemyDmgField.setPrefWidth(60);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // Grid layout for design parameters, improves formatting
        // Headers for each column
        grid.add(new Label("Difficulty"), 0, 0);
        grid.add(new Label("HP"), 1, 0);
        grid.add(new Label("Lives"), 2, 0);
        grid.add(new Label("EnemyDmg"), 3, 0);

        //Easy difficulty parameters
        grid.add(new Label("EASY"), 0, 1);
        grid.add(easyHpField, 1, 1);
        grid.add(easyLivesField, 2, 1);
        grid.add(easyEnemyDmgField, 3, 1);

        //Medium difficulty parameters
        grid.add(new Label("MEDIUM"), 0, 2);
        grid.add(mediumHpField, 1, 2);
        grid.add(mediumLivesField, 2, 2);
        grid.add(mediumEnemyDmgField, 3, 2);

        //Hard difficulty parameters
        grid.add(new Label("HARD"), 0, 3);
        grid.add(hardHpField, 1, 3);
        grid.add(hardLivesField, 2, 3);
        grid.add(hardEnemyDmgField, 3, 3);

        grid.setAlignment(Pos.CENTER_LEFT);

        // Allows for modified values to be written back to settings
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {

                RoleEnum role = settings.getUserRole();
                if (role != RoleEnum.DESIGNER && role != RoleEnum.DEVELOPER) {
                    output.setText("You do not have permission to modify design parameters.");
                    return;
                }

                int easyHp = Integer.parseInt(easyHpField.getText());
                int mediumHp = Integer.parseInt(mediumHpField.getText());
                int hardHp = Integer.parseInt(hardHpField.getText());

                int easyLives = Integer.parseInt(easyLivesField.getText());
                int mediumLives = Integer.parseInt(mediumLivesField.getText());
                int hardLives = Integer.parseInt(hardLivesField.getText());

                float easyEnemyDmg = Float.parseFloat(easyEnemyDmgField.getText());
                float mediumEnemyDmg = Float.parseFloat(mediumEnemyDmgField.getText());
                float hardEnemyDmg = Float.parseFloat(hardEnemyDmgField.getText());

                // Update settings with new values from text fields
                settings.setPlayerMaxHealth(DifficultyEnum.EASY, easyHp);
                settings.setPlayerMaxHealth(DifficultyEnum.MEDIUM, mediumHp);
                settings.setPlayerMaxHealth(DifficultyEnum.HARD, hardHp);

                settings.setStartingLives(DifficultyEnum.EASY, easyLives);
                settings.setStartingLives(DifficultyEnum.MEDIUM, mediumLives);
                settings.setStartingLives(DifficultyEnum.HARD, hardLives);

                settings.setEnemyDamageMultiplier(DifficultyEnum.EASY, easyEnemyDmg);
                settings.setEnemyDamageMultiplier(DifficultyEnum.MEDIUM, mediumEnemyDmg);
                settings.setEnemyDamageMultiplier(DifficultyEnum.HARD, hardEnemyDmg);

                output.setText("Settings updated.");
            } catch (NumberFormatException ex) {
                output.setText("Please enter valid numbers.");
            } catch (Exception ex) {
                output.setText("Could not update settings.");
            }
        });


        // Root layout for settings page
        VBox root = new VBox(14,
             roleLabel,
              grid,
                saveButton,
                output,
                telemetryLabel,
                toggleTelemetryButton, 
                telemetryDisclosure);
        root.setPadding(new Insets(20));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backAction.run());

        root.getChildren().add(backButton);

        return root;
    }

    // Restricts a text field to only allow positive integers (1 and above), used for HP and Lives fields
    private void makeIntegerOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) return change;

            if (!newText.matches("\\d+")) return null;

            int value = Integer.parseInt(newText);
            if (value < 1) return null;

            return change;
        }));
    }

    // Restricts a text field to only allow positive decimals (0.1 and above), used for enemy damage multiplier fields
    private void makeDecimalOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) return change;

            if (!newText.matches("\\d*(\\.\\d*)?")) return null;

            try {
                float value = Float.parseFloat(newText);
                if (value < 0.1f) return null;
            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        }));
    }

}
