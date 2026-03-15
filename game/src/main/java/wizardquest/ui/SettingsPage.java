package wizardquest.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import wizardquest.auth.AuthenticationException;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class SettingsPage {

    // Access settings to read and update values
    private final SettingsInterface settings = SettingsSingleton.getInstance();
    private final Label output = new Label("");

    // STYLING

    private static final String PANEL_STYLE =
        "-fx-background-color: #2b2d31;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #5865F2;" +
        "-fx-border-radius: 10;";

    private static final String HEADING_STYLE =
            "-fx-font-family: 'Trebuchet MS';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #5865F2;" +
            "-fx-font-weight: bold;";

    private static final String TEXT_STYLE =
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #f2f3f5;";

    private static final String SECONDARY_TEXT_STYLE =
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #b5bac1;";

    private static final String PRIMARY_BUTTON_STYLE =
            "-fx-background-color: #5865F2;" +
            "-fx-text-fill: #f2f3f5;" +
            "-fx-font-family: 'Segoe UI';" +
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
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #5865F2;" +
            "-fx-padding: 8 16 8 16;" +
            "-fx-cursor: hand;";

    private String fieldStyle = 
                    "-fx-background-color: #1e1f22;" +
                        "-fx-text-fill: #ae00ff;" +
                        "-fx-border-color: #5865F2;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;";


    public VBox createView(Runnable backAction) {
        Label roleLabel = new Label();
        roleLabel.setStyle(HEADING_STYLE);

        try {
            // Display the user's role at the top of the settings page
            roleLabel.setText("Role: " + settings.getUserRole());
        } catch (AuthenticationException e) {
            roleLabel.setText("Role: UNKNOWN");
        }

        // Displays current telemetry state
        Label telemetryLabel = new Label("Telemetry: OFF");
        telemetryLabel.setStyle(TEXT_STYLE);
        try {
            telemetryLabel.setText("Telemetry: " + (settings.isTelemetryEnabled() ? "ON" : "OFF"));
        } catch (AuthenticationException e) {
            telemetryLabel.setText("Telemetry: OFF");
        }

        // Button to toggle telemetry on and off
        Button toggleTelemetryButton = new Button("Toggle Telemetry");
        toggleTelemetryButton.setStyle(SECONDARY_BUTTON_STYLE);
        toggleTelemetryButton.setOnAction(e -> {
            try {
                boolean current = settings.isTelemetryEnabled();
                settings.setTelemetryEnabled(!current);
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
                \u2022 coins gained, upgrades purchased and enemies defeated
                \u2022 settings changes (new values)
                
                Why we collect it:
                \u2022 to spot difficulty spikes and unfair encounters
                \u2022 to support balancing changes with evidence""");

        telemetryDisclosure.setWrapText(true);
        telemetryDisclosure.setStyle(SECONDARY_TEXT_STYLE);


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

        TextField easyEnemyHpMultField = new TextField(String.valueOf(settings.getEnemyMaxHealthMultiplier(DifficultyEnum.EASY)));
        TextField mediumEnemyHpMultField = new TextField(String.valueOf(settings.getEnemyMaxHealthMultiplier(DifficultyEnum.MEDIUM)));
        TextField hardEnemyHpMultField = new TextField(String.valueOf(settings.getEnemyMaxHealthMultiplier(DifficultyEnum.HARD)));

        TextField easyMaxMagicField = new TextField(String.valueOf(settings.getMaxMagic(DifficultyEnum.EASY)));
        TextField mediumMaxMagicField = new TextField(String.valueOf(settings.getMaxMagic(DifficultyEnum.MEDIUM)));
        TextField hardMaxMagicField = new TextField(String.valueOf(settings.getMaxMagic(DifficultyEnum.HARD)));

        TextField easyRegenRateField = new TextField(String.valueOf(settings.getMagicRegenRate(DifficultyEnum.EASY)));
        TextField mediumRegenRateField = new TextField(String.valueOf(settings.getMagicRegenRate(DifficultyEnum.MEDIUM)));
        TextField hardRegenRateField = new TextField(String.valueOf(settings.getMagicRegenRate(DifficultyEnum.HARD)));

        TextField easyShopItemCountField = new TextField(String.valueOf(settings.getShopItemCount(DifficultyEnum.EASY)));
        TextField mediumShopItemCountField = new TextField(String.valueOf(settings.getShopItemCount(DifficultyEnum.MEDIUM)));
        TextField hardShopItemCountField = new TextField(String.valueOf(settings.getShopItemCount(DifficultyEnum.HARD)));


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

        easyEnemyHpMultField.setPrefWidth(70);
        mediumEnemyHpMultField.setPrefWidth(70);
        hardEnemyHpMultField.setPrefWidth(70);

        easyMaxMagicField.setPrefWidth(70);
        mediumMaxMagicField.setPrefWidth(70);
        hardMaxMagicField.setPrefWidth(70);

        easyRegenRateField.setPrefWidth(70);
        mediumRegenRateField.setPrefWidth(70);
        hardRegenRateField.setPrefWidth(70);

        easyShopItemCountField.setPrefWidth(70);
        mediumShopItemCountField.setPrefWidth(70);
        hardShopItemCountField.setPrefWidth(70);

        // Setting styling for each field
        easyHpField.setStyle(fieldStyle);
        easyLivesField.setStyle(fieldStyle);
        easyEnemyDmgField.setStyle(fieldStyle);

        mediumHpField.setStyle(fieldStyle);
        mediumLivesField.setStyle(fieldStyle);
        mediumEnemyDmgField.setStyle(fieldStyle);

        hardHpField.setStyle(fieldStyle);
        hardLivesField.setStyle(fieldStyle);
        hardEnemyDmgField.setStyle(fieldStyle);

        easyEnemyHpMultField.setStyle(fieldStyle);
        mediumEnemyHpMultField.setStyle(fieldStyle);
        hardEnemyHpMultField.setStyle(fieldStyle);

        easyMaxMagicField.setStyle(fieldStyle);
        mediumMaxMagicField.setStyle(fieldStyle);
        hardMaxMagicField.setStyle(fieldStyle);

        easyRegenRateField.setStyle(fieldStyle);
        mediumRegenRateField.setStyle(fieldStyle);
        hardRegenRateField.setStyle(fieldStyle);

        easyShopItemCountField.setStyle(fieldStyle);
        mediumShopItemCountField.setStyle(fieldStyle);
        hardShopItemCountField.setStyle(fieldStyle);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Pre-defining grid column widths
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(90);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(60);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(60);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setMinWidth(95);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setMinWidth(110);

        ColumnConstraints col5 = new ColumnConstraints();
        col5.setMinWidth(85);

        ColumnConstraints col6 = new ColumnConstraints();
        col6.setMinWidth(90);

        ColumnConstraints col7 = new ColumnConstraints();
        col7.setMinWidth(90);

        grid.getColumnConstraints().addAll(
            col0, col1, col2, col3, col4, col5, col6, col7
        );

        // Headers
        Label difficultyHeader = new Label("Difficulty");
        Label hpHeader = new Label("HP");
        Label livesHeader = new Label("Lives");
        Label enemyDmgHeader = new Label("EnemyDmg");
        Label enemyHpMultHeader = new Label("EnemyHp x");
        Label maxMagicHeader = new Label("MaxMagic");
        Label regenRateHeader = new Label("RegenRate");
        Label shopCountHeader = new Label("ShopCount");

        // Grid heading styling
        difficultyHeader.setStyle(HEADING_STYLE);
        hpHeader.setStyle(HEADING_STYLE);
        livesHeader.setStyle(HEADING_STYLE);
        enemyDmgHeader.setStyle(HEADING_STYLE);
        enemyHpMultHeader.setStyle(HEADING_STYLE);
        maxMagicHeader.setStyle(HEADING_STYLE);
        regenRateHeader.setStyle(HEADING_STYLE);
        shopCountHeader.setStyle(HEADING_STYLE);

        // Grid layout
        grid.add(difficultyHeader, 0, 0);
        grid.add(hpHeader, 1, 0);
        grid.add(livesHeader, 2, 0);
        grid.add(enemyDmgHeader, 3, 0);
        grid.add(enemyHpMultHeader, 4, 0);
        grid.add(maxMagicHeader, 5, 0);
        grid.add(regenRateHeader, 6, 0);
        grid.add(shopCountHeader, 7, 0);

        // Row labels
        Label easyLabel = new Label("EASY");
        Label mediumLabel = new Label("MEDIUM");
        Label hardLabel = new Label("HARD");

        easyLabel.setStyle(TEXT_STYLE);
        mediumLabel.setStyle(TEXT_STYLE);
        hardLabel.setStyle(TEXT_STYLE);

        // Easy difficulty parameters
        grid.add(easyLabel, 0, 1);
        grid.add(easyHpField, 1, 1);
        grid.add(easyLivesField, 2, 1);
        grid.add(easyEnemyDmgField, 3, 1);
        grid.add(easyEnemyHpMultField, 4, 1);
        grid.add(easyMaxMagicField, 5, 1);
        grid.add(easyRegenRateField, 6, 1);
        grid.add(easyShopItemCountField, 7, 1);

        // Medium difficulty parameters
        grid.add(mediumLabel, 0, 2);
        grid.add(mediumHpField, 1, 2);
        grid.add(mediumLivesField, 2, 2);
        grid.add(mediumEnemyDmgField, 3, 2);
        grid.add(mediumEnemyHpMultField, 4, 2);
        grid.add(mediumMaxMagicField, 5, 2);
        grid.add(mediumRegenRateField, 6, 2);
        grid.add(mediumShopItemCountField, 7, 2);

        // Hard difficulty parameters
        grid.add(hardLabel, 0, 3);
        grid.add(hardHpField, 1, 3);
        grid.add(hardLivesField, 2, 3);
        grid.add(hardEnemyDmgField, 3, 3);
        grid.add(hardEnemyHpMultField, 4, 3);
        grid.add(hardMaxMagicField, 5, 3);
        grid.add(hardRegenRateField, 6, 3);
        grid.add(hardShopItemCountField, 7, 3);

        grid.setAlignment(Pos.CENTER);

        Label justificationLabel = new Label("Justification for changes:");
        justificationLabel.setStyle(TEXT_STYLE);
        TextArea justificationBox = new TextArea();
        justificationBox.setPromptText("OPTIONAL: Explain why these settings are being changed...");
        justificationBox.setWrapText(true);
        justificationBox.setPrefRowCount(3);
        justificationBox.setMaxWidth(400);
        justificationBox.setStyle(fieldStyle);

        // Allows for modified values to be written back to settings
        Button saveButton = new Button("Save");
        saveButton.setStyle(PRIMARY_BUTTON_STYLE);
        saveButton.setOnAction(e -> {
            try {
                // Checks if the user has the required role (Designer or Developer) before allowing design parameter changes
                RoleEnum role = settings.getUserRole();
                if (role != RoleEnum.DESIGNER && role != RoleEnum.DEVELOPER) {
                    output.setText("You do not have permission to modify design parameters.");
                    output.setStyle(TEXT_STYLE);
                    return;
                }      

                // Reads the difficulty parameters entered in the UI and converts the text values into integers
                int easyHp = Integer.parseInt(easyHpField.getText());
                int mediumHp = Integer.parseInt(mediumHpField.getText());
                int hardHp = Integer.parseInt(hardHpField.getText());

                int easyLives = Integer.parseInt(easyLivesField.getText());
                int mediumLives = Integer.parseInt(mediumLivesField.getText());
                int hardLives = Integer.parseInt(hardLivesField.getText());

                // Reads the difficulty parameters entered in the UI and converts the text values into floats
                float easyEnemyDmg = Float.parseFloat(easyEnemyDmgField.getText());
                float mediumEnemyDmg = Float.parseFloat(mediumEnemyDmgField.getText());
                float hardEnemyDmg = Float.parseFloat(hardEnemyDmgField.getText());

                float easyEnemyHpMult = Float.parseFloat(easyEnemyHpMultField.getText());
                float mediumEnemyHpMult = Float.parseFloat(mediumEnemyHpMultField.getText());
                float hardEnemyHpMult = Float.parseFloat(hardEnemyHpMultField.getText());

                int easyMaxMagic = Integer.parseInt(easyMaxMagicField.getText());
                int mediumMaxMagic = Integer.parseInt(mediumMaxMagicField.getText());
                int hardMaxMagic = Integer.parseInt(hardMaxMagicField.getText());

                int easyRegenRate = Integer.parseInt(easyRegenRateField.getText());
                int mediumRegenRate = Integer.parseInt(mediumRegenRateField.getText());
                int hardRegenRate = Integer.parseInt(hardRegenRateField.getText());

                int easyShopItemCount = Integer.parseInt(easyShopItemCountField.getText());
                int mediumShopItemCount = Integer.parseInt(mediumShopItemCountField.getText());
                int hardShopItemCount = Integer.parseInt(hardShopItemCountField.getText());

                // Update Max Health with new values from text fields
                settings.setPlayerMaxHealth(DifficultyEnum.EASY, easyHp);
                settings.setPlayerMaxHealth(DifficultyEnum.MEDIUM, mediumHp);
                settings.setPlayerMaxHealth(DifficultyEnum.HARD, hardHp);

                // Update Starting Lives with new values from text fields
                settings.setStartingLives(DifficultyEnum.EASY, easyLives);
                settings.setStartingLives(DifficultyEnum.MEDIUM, mediumLives);
                settings.setStartingLives(DifficultyEnum.HARD, hardLives);

                // Update Enemy Damage Multiplier with new values from text fields
                settings.setEnemyDamageMultiplier(DifficultyEnum.EASY, easyEnemyDmg);
                settings.setEnemyDamageMultiplier(DifficultyEnum.MEDIUM, mediumEnemyDmg);
                settings.setEnemyDamageMultiplier(DifficultyEnum.HARD, hardEnemyDmg);

                settings.setEnemyMaxHealthMultiplier(DifficultyEnum.EASY, easyEnemyHpMult);
                settings.setEnemyMaxHealthMultiplier(DifficultyEnum.MEDIUM, mediumEnemyHpMult);
                settings.setEnemyMaxHealthMultiplier(DifficultyEnum.HARD, hardEnemyHpMult);

                settings.setMaxMagic(DifficultyEnum.EASY, easyMaxMagic);
                settings.setMaxMagic(DifficultyEnum.MEDIUM, mediumMaxMagic);
                settings.setMaxMagic(DifficultyEnum.HARD, hardMaxMagic);

                settings.setMagicRegenRate(DifficultyEnum.EASY, easyRegenRate);
                settings.setMagicRegenRate(DifficultyEnum.MEDIUM, mediumRegenRate);
                settings.setMagicRegenRate(DifficultyEnum.HARD, hardRegenRate);

                settings.setShopItemCount(DifficultyEnum.EASY, easyShopItemCount);
                settings.setShopItemCount(DifficultyEnum.MEDIUM, mediumShopItemCount);
                settings.setShopItemCount(DifficultyEnum.HARD, hardShopItemCount);

                output.setText("Settings updated.");
                justificationBox.clear();
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
                output,
                telemetryLabel,
                toggleTelemetryButton,
                telemetryDisclosure,
                justificationLabel,
                justificationBox,
                saveButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle(PANEL_STYLE);
        root.setMaxWidth(1200);
        // Back button returns to main menu
        Button backButton = new Button("Back");
        backButton.setStyle(SECONDARY_BUTTON_STYLE);
        backButton.setOnAction(e -> backAction.run());

        root.getChildren().add(backButton);

        VBox wrapper = new VBox(root);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle("-fx-background-color: #1e1f22;");

        return wrapper;
    }

    // Restricts a text field to only allow positive integers (1 and above), used for HP and Lives fields
    private void makeIntegerOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            // Positive integers only.
            if (!newText.matches("\\d+")) return null;

            int value = Integer.parseInt(newText);
            // Minimum = 1
            if (value < 1) return null;

            return change;
        }));
    }

    // Restricts a text field to only allow positive decimals (0.1 and above), used for enemy damage multiplier fields
    private void makeDecimalOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }
            // Positive floats only
            if (!newText.matches("\\d*(\\.\\d*)?")) return null;

            try {
                float value = Float.parseFloat(newText);
                // Minimum = 0.1
                if (value < 0.1f) return null;
            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        }));
    }

}
