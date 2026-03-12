package wizardquest.ui;

import java.time.Instant;

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
import wizardquest.settings.SettingsEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.SettingsChangeEvent;
import wizardquest.telemetry.TelemetryListenerInterface;
import wizardquest.telemetry.TelemetryListenerSingleton;

public class SettingsPage {

    // Access settings to read and update values
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final TelemetryListenerInterface telemetryListener = TelemetryListenerSingleton.getInstance();
    private final Label output = new Label("");

    public VBox createView(Runnable backAction) {
        Label roleLabel = new Label();

        try {
            // Display the user's role at the top of the settings page
            roleLabel.setText("Role: " + settings.getUserRole());
        } catch (AuthenticationException e) {
            roleLabel.setText("Role: UNKNOWN");
        }

        // Displays current telemetry state
        Label telemetryLabel = new Label("Telemetry: OFF");
        try {
            telemetryLabel.setText("Telemetry: " + (settings.isTelemetryEnabled() ? "ON" : "OFF"));
        } catch (AuthenticationException e) {
            telemetryLabel.setText("Telemetry: OFF");
        }

        // Button to toggle telemetry on and off
        Button toggleTelemetryButton = new Button("Toggle Telemetry");
        toggleTelemetryButton.setOnAction(e -> {
            try {
                boolean current = settings.isTelemetryEnabled();
                settings.setTelemetryEnabled(!current);
                telemetryListener.onSettingsChange(new SettingsChangeEvent(
                        settings.getUserID(), Instant.now(),
                        SettingsEnum.TELEMETRY_ENABLED,
                        String.valueOf(!current), ""));
                telemetryLabel.setText("Telemetry: " + (!current ? "ON" : "OFF"));
            } catch (AuthenticationException ex) {
                telemetryLabel.setText("Telemetry: OFF");
            }
        });
        // Disclosure explaining telemetry data collection to the user
        Label telemetryDisclosure = new Label(
                """
                        If telemetry is enabled, the game records events anonymously to help balance difficulty and improve gameplay.

                        This includes:
                        \u2022 timestamp, session id, user id
                        \u2022 encounter start, completion, or fail (stage, difficulty, encounter name)
                        \u2022 player state on completion or fail (health remaining, lives left if relevant)
                        \u2022 coins gained and upgrades purchased
                        \u2022 settings changes (new values)
                        \u2022 enemies defeated

                        Why we collect it:
                        \u2022 to spot difficulty spikes and unfair encounters
                        \u2022 to support balancing changes with evidence""");

        telemetryDisclosure.setWrapText(true);

        // Text fields for each editable design parameter (pre-populated with current
        // values)
        TextField easyHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.EASY)));
        TextField easyLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.EASY)));
        TextField easyEnemyDmgField = new TextField(
                String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.EASY)));

        TextField mediumHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM)));
        TextField mediumLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.MEDIUM)));
        TextField mediumEnemyDmgField = new TextField(
                String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.MEDIUM)));

        TextField hardHpField = new TextField(String.valueOf(settings.getPlayerMaxHealth(DifficultyEnum.HARD)));
        TextField hardLivesField = new TextField(String.valueOf(settings.getStartingLives(DifficultyEnum.HARD)));
        TextField hardEnemyDmgField = new TextField(
                String.valueOf(settings.getEnemyDamageMultiplier(DifficultyEnum.HARD)));

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

        // Easy difficulty parameters
        grid.add(new Label("EASY"), 0, 1);
        grid.add(easyHpField, 1, 1);
        grid.add(easyLivesField, 2, 1);
        grid.add(easyEnemyDmgField, 3, 1);

        // Medium difficulty parameters
        grid.add(new Label("MEDIUM"), 0, 2);
        grid.add(mediumHpField, 1, 2);
        grid.add(mediumLivesField, 2, 2);
        grid.add(mediumEnemyDmgField, 3, 2);

        // Hard difficulty parameters
        grid.add(new Label("HARD"), 0, 3);
        grid.add(hardHpField, 1, 3);
        grid.add(hardLivesField, 2, 3);
        grid.add(hardEnemyDmgField, 3, 3);

        grid.setAlignment(Pos.CENTER_LEFT);

        // Allows for modified values to be written back to settings
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                // Checks if the user has the required role (Designer or Developer) before
                // allowing design parameter changes
                RoleEnum role = settings.getUserRole();
                if (role != RoleEnum.DESIGNER && role != RoleEnum.DEVELOPER) {
                    output.setText("You do not have permission to modify design parameters.");
                    return;
                }

                // Reads the difficulty parameters entered in the UI and converts the text
                // values into integers
                int easyHp = Integer.parseInt(easyHpField.getText());
                int mediumHp = Integer.parseInt(mediumHpField.getText());
                int hardHp = Integer.parseInt(hardHpField.getText());

                int easyLives = Integer.parseInt(easyLivesField.getText());
                int mediumLives = Integer.parseInt(mediumLivesField.getText());
                int hardLives = Integer.parseInt(hardLivesField.getText());

                // Reads the difficulty parameters entered in the UI and converts the text
                // values into floats
                float easyEnemyDmg = Float.parseFloat(easyEnemyDmgField.getText());
                float mediumEnemyDmg = Float.parseFloat(mediumEnemyDmgField.getText());
                float hardEnemyDmg = Float.parseFloat(hardEnemyDmgField.getText());

                String userID = settings.getUserID();

                // Update Max Health - only emit telemetry if value changed
                if (easyHp != settings.getPlayerMaxHealth(DifficultyEnum.EASY)) {
                    settings.setPlayerMaxHealth(DifficultyEnum.EASY, easyHp);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.PLAYER_MAX_HEALTH,
                            "EASY: " + easyHp, ""));
                }
                if (mediumHp != settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM)) {
                    settings.setPlayerMaxHealth(DifficultyEnum.MEDIUM, mediumHp);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.PLAYER_MAX_HEALTH,
                            "MEDIUM: " + mediumHp, ""));
                }
                if (hardHp != settings.getPlayerMaxHealth(DifficultyEnum.HARD)) {
                    settings.setPlayerMaxHealth(DifficultyEnum.HARD, hardHp);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.PLAYER_MAX_HEALTH,
                            "HARD: " + hardHp, ""));
                }

                // Update Starting Lives - only emit telemetry if value changed
                if (easyLives != settings.getStartingLives(DifficultyEnum.EASY)) {
                    settings.setStartingLives(DifficultyEnum.EASY, easyLives);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.STARTING_LIVES,
                            "EASY: " + easyLives, ""));
                }
                if (mediumLives != settings.getStartingLives(DifficultyEnum.MEDIUM)) {
                    settings.setStartingLives(DifficultyEnum.MEDIUM, mediumLives);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.STARTING_LIVES,
                            "MEDIUM: " + mediumLives, ""));
                }
                if (hardLives != settings.getStartingLives(DifficultyEnum.HARD)) {
                    settings.setStartingLives(DifficultyEnum.HARD, hardLives);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.STARTING_LIVES,
                            "HARD: " + hardLives, ""));
                }

                // Update Enemy Damage Multiplier - only emit telemetry if value changed
                if (Float.compare(easyEnemyDmg, settings.getEnemyDamageMultiplier(DifficultyEnum.EASY)) != 0) {
                    settings.setEnemyDamageMultiplier(DifficultyEnum.EASY, easyEnemyDmg);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.ENEMY_DAMAGE_MULTIPLIER,
                            "EASY: " + easyEnemyDmg, ""));
                }
                if (Float.compare(mediumEnemyDmg, settings.getEnemyDamageMultiplier(DifficultyEnum.MEDIUM)) != 0) {
                    settings.setEnemyDamageMultiplier(DifficultyEnum.MEDIUM, mediumEnemyDmg);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.ENEMY_DAMAGE_MULTIPLIER,
                            "MEDIUM: " + mediumEnemyDmg, ""));
                }
                if (Float.compare(hardEnemyDmg, settings.getEnemyDamageMultiplier(DifficultyEnum.HARD)) != 0) {
                    settings.setEnemyDamageMultiplier(DifficultyEnum.HARD, hardEnemyDmg);
                    telemetryListener.onSettingsChange(new SettingsChangeEvent(
                            userID, Instant.now(), SettingsEnum.ENEMY_DAMAGE_MULTIPLIER,
                            "HARD: " + hardEnemyDmg, ""));
                }

                output.setText("Settings updated.");
            } catch (NumberFormatException ex) {
                output.setText("Please enter valid numbers.");
            } catch (AuthenticationException ex) {
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
        // Back button returns to main menu
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backAction.run());

        root.getChildren().add(backButton);

        return root;
    }

    // Restricts a text field to only allow positive integers (1 and above), used
    // for HP and Lives fields
    private void makeIntegerOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            // Positive integers only.
            if (!newText.matches("\\d+"))
                return null;

            int value = Integer.parseInt(newText);
            // Minimum = 1
            if (value < 1)
                return null;

            return change;
        }));
    }

    // Restricts a text field to only allow positive decimals (0.1 and above), used
    // for enemy damage multiplier fields
    private void makeDecimalOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }
            // Positive floats only
            if (!newText.matches("\\d*(\\.\\d*)?"))
                return null;

            try {
                float value = Float.parseFloat(newText);
                // Minimum = 0.1
                if (value < 0.1f)
                    return null;
            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        }));
    }

}
