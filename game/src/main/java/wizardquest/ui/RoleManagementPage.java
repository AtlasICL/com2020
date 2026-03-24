package wizardquest.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
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

    private static final String TABLE_STYLE =
            "-fx-background-color: #1e1f22;" +
            "-fx-control-inner-background: #3a3d45;" +
            "-fx-table-cell-border-color: #404249;" +
            "-fx-border-color: #5865F2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'JetBrains Mono';";

    private static final String COMBO_STYLE =
            "-fx-background-color: #f2f3f5;" +
            "-fx-text-fill: #f2f3f5;" +
            "-fx-font-family: 'JetBrains Mono';" +
            "-fx-border-color: #5865F2;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;";

    public VBox createView(Runnable backAction) {

        Label heading = new Label("MANAGE USER ROLES");
        heading.setStyle(TITLE_STYLE);

        Label info = new Label("Change roles using the dropdown next to each user.");
        info.setStyle(TEXT_STYLE);

        TableView<UserRoleRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No users found."));
        table.setStyle(TABLE_STYLE + "-fx-text-fill: #f2f3f5;");
        table.setFixedCellSize(42);
        table.setPrefHeight(380);
        table.setStyle(TABLE_STYLE + "-fx-text-fill: #f2f3f5;");
        // User ID column
        TableColumn<UserRoleRow, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(data -> data.getValue().userIdProperty());
        // Role Column
        TableColumn<UserRoleRow, RoleEnum> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> data.getValue().roleProperty());

        roleColumn.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<RoleEnum> comboBox =
                    new ComboBox<>(FXCollections.observableArrayList(RoleEnum.values()));

            {
                comboBox.setStyle(COMBO_STYLE + "-fx-text-fill: #f2f3f5;");
                comboBox.setMaxWidth(Double.MAX_VALUE);
                // Listener for when roles are changed
                comboBox.valueProperty().addListener((obs, oldRole, newRole) -> {
                    UserRoleRow row = getTableRow() == null ? null : getTableRow().getItem();

                    if (row == null || newRole == null || oldRole == newRole) {
                        return;
                    }

                    try {
                        settings.setUserRole(row.getUserId(), newRole);
                        row.setRole(newRole);
                        output.setText("Role updated: " + row.getUserId() + " -> " + newRole);
                    } catch (AuthenticationException e) {
                        comboBox.setValue(oldRole);
                        output.setText("Only developers can change user roles.");
                    }
                });
            }

            @Override
            protected void updateItem(RoleEnum role, boolean empty) {
                super.updateItem(role, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                UserRoleRow row = getTableRow().getItem();
                comboBox.setValue(role);
                comboBox.setDisable(row.getUserId().equals(settings.getUserID()));
                setGraphic(comboBox);
            }
        });

        table.getColumns().addAll(userIdColumn, roleColumn);
        table.setItems(FXCollections.observableArrayList(loadUsers()));

        output.setStyle(SECONDARY_TEXT_STYLE);
        output.setWrapText(true);
        output.setMaxWidth(400);
        output.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(SECONDARY_BUTTON_STYLE);
        refreshBtn.setPrefWidth(180);
        refreshBtn.setOnAction(e -> {
            table.setItems(FXCollections.observableArrayList(loadUsers()));
            output.setText("User list refreshed.");
        });

        Button backBtn = new Button("Back");
        backBtn.setStyle(SECONDARY_BUTTON_STYLE);
        backBtn.setPrefWidth(180);
        backBtn.setOnAction(e -> backAction.run());

        VBox root = new VBox(12,
                heading,
                info,
                table,
                output,
                refreshBtn,
                backBtn
        );

        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(520);
        root.setStyle(PANEL_STYLE);

        VBox.setVgrow(table, Priority.ALWAYS);

        return root;
    }

     // Checks whether the user exists in telemetry/logins_file.json.
    private List<UserRoleRow> loadUsers() {
        List<UserRoleRow> users = new ArrayList<>();
        Path loginFile = Path.of("..", "telemetry", "logins_file.json");

        if (!Files.exists(loginFile)) {
            output.setText("Could not find telemetry/logins_file.json.");
            return users;
        }

        try {
            String content = Files.readString(loginFile);
            // Defining the pattern in each row of logins_file.json
            Pattern pairPattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pairPattern.matcher(content);

            while (matcher.find()) {
                String userId = matcher.group(1);
                String roleText = matcher.group(2).toUpperCase();

                RoleEnum role;
                try {
                    role = RoleEnum.valueOf(roleText);
                } catch (IllegalArgumentException e) {
                    role = RoleEnum.PLAYER;
                }

                users.add(new UserRoleRow(userId, role));
            }

            output.setText("Found " + users.size() + " users.");

        } catch (IOException e) {
            output.setText("Failed to read logins_file.json.");
        }

        return users;
    }

    private RoleEnum getRoleSafely(String userId) {
    Path loginFile = Path.of("..", "telemetry", "logins_file.json");

    if (!Files.exists(loginFile)) {
        return RoleEnum.PLAYER;
    }

    try {
        String content = Files.readString(loginFile);
        // Defining the pattern in each row of logins_file.json
        Pattern pairPattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pairPattern.matcher(content);

        // Ensuring that all rows in logins_file.json are valid
        while (matcher.find()) {
            String foundUserId = matcher.group(1);
            String roleText = matcher.group(2).toUpperCase();

            if (userId.equals(foundUserId)) {
                return RoleEnum.valueOf(roleText);
            }
        }

    } catch (Exception e) {
        return RoleEnum.PLAYER;
    }

    return RoleEnum.PLAYER;
}

    private String extractField(String text, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private static class UserRoleRow {
        private final StringProperty userId;
        private final ObjectProperty<RoleEnum> role;

        public UserRoleRow(String userId, RoleEnum role) {
            this.userId = new SimpleStringProperty(userId);
            this.role = new SimpleObjectProperty<>(role);
        }

        public String getUserId() {
            return userId.get();
        }

        public StringProperty userIdProperty() {
            return userId;
        }

        public RoleEnum getRole() {
            return role.get();
        }

        public void setRole(RoleEnum role) {
            this.role.set(role);
        }

        public ObjectProperty<RoleEnum> roleProperty() {
            return role;
        }
    }
}