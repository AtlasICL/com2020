package wizardquest.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import wizardquest.auth.AuthenticationException;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class RoleManagementPage {

    // Access settings singleton, updates user roles
    private final SettingsInterface settings = SettingsSingleton.getInstance();

    // Displays success/error message
    private final Label output = new Label("");

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

    private static final String INPUT_STYLE =
            "-fx-background-color: #1e1f22;" +
            "-fx-text-fill: #f2f3f5;" +
            "-fx-prompt-text-fill: #b5bac1;" +
            "-fx-font-family: 'JetBrains Mono';" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #5865F2;" +
            "-fx-padding: 8 10 8 10;";

    public VBox createView(Runnable backAction) {

        Label heading = new Label("MANAGE USER ROLES");
        heading.setStyle(TITLE_STYLE);

        Label userIdLabel = new Label("User ID:");
        userIdLabel.setStyle(TEXT_STYLE);

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter user ID");
        userIdField.setStyle(INPUT_STYLE);
        userIdField.setMaxWidth(280);

        Label roleLabel = new Label("Assign Role:");
        roleLabel.setStyle(HEADING_STYLE);

        Button playerBtn = new Button("Set PLAYER");
        playerBtn.setStyle(PRIMARY_BUTTON_STYLE);
        playerBtn.setPrefWidth(220);

        Button designerBtn = new Button("Set DESIGNER");
        designerBtn.setStyle(PRIMARY_BUTTON_STYLE);
        designerBtn.setPrefWidth(220);

        Button developerBtn = new Button("Set DEVELOPER");
        developerBtn.setStyle(DANGER_BUTTON_STYLE);
        developerBtn.setPrefWidth(220);

        playerBtn.setOnAction(e -> updateRole(userIdField.getText(), RoleEnum.PLAYER));
        designerBtn.setOnAction(e -> updateRole(userIdField.getText(), RoleEnum.DESIGNER));
        developerBtn.setOnAction(e -> updateRole(userIdField.getText(), RoleEnum.DEVELOPER));

        Button backBtn = new Button("Back");
        backBtn.setStyle(SECONDARY_BUTTON_STYLE);
        backBtn.setPrefWidth(180);
        backBtn.setOnAction(e -> backAction.run());

        output.setStyle(SECONDARY_TEXT_STYLE);
        output.setWrapText(true);
        output.setMaxWidth(320);
        output.setAlignment(Pos.CENTER);

        VBox root = new VBox(12,
                heading,
                userIdLabel,
                userIdField,
                roleLabel,
                playerBtn,
                designerBtn,
                developerBtn,
                output,
                backBtn
        );

        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(420);
        root.setStyle(PANEL_STYLE);

        return root;
    }

    // Updates the role of a given user.
    private void updateRole(String userId, RoleEnum role) {

        if (userId == null || userId.trim().isEmpty()) {
            output.setText("Please enter a valid user ID.");
            return;
        }

        String trimmedUserId = userId.trim();

        if (!userExists(trimmedUserId)) {
            output.setText("User ID not found.");
            return;
        }

        try {
            settings.setUserRole(trimmedUserId, role);
            output.setText("Role updated: " + trimmedUserId + " -> " + role);

        } catch (AuthenticationException e) {
            output.setText("Only developers can change user roles.");
        }
    }


     // Checks whether the user exists in telemetry/logins_file.json.
    private boolean userExists(String userId) {
        Path loginFile = Path.of("..", "telemetry", "logins_file.json");

        if (!Files.exists(loginFile)) {
            output.setText("Could not find telemetry/logins_file.json.");
            return false;
        }

        try {
            String content = Files.readString(loginFile);
            return content.contains("\"" + userId + "\"");

        } catch (IOException e) {
            output.setText("Failed to read logins_file.json.");
            return false;
        }
    }
}