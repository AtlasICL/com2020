package wizardquest.ui;

import java.time.Instant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.settings.DifficultyEnum;
import wizardquest.telemetry.BuyUpgradeEvent;
import wizardquest.telemetry.TelemetryListenerInterface;

public class ShopPage {

    private final GameManagerInterface gameManager;
    private final VBox root;
    private final Label log;
    private final TelemetryListenerInterface telemetryListener;
    private final String userID;
    private final int sessionID;
    private final EncounterEnum completedEncType;
    private final DifficultyEnum difficulty;
    private final int completedStage;
    private final Runnable onLeaveShop;

    private static final String PANEL_STYLE =
            "-fx-background-color: #2b2d31;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #5865F2;" +
            "-fx-border-radius: 10;";

    private static final String TITLE_STYLE =
            "-fx-font-family: 'Trebuchet MS';" +
            "-fx-font-size: 24px;" +
            "-fx-text-fill: #9a7cff;" +
            "-fx-font-weight: bold;";

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

    public ShopPage(GameManagerInterface gameManager, VBox root, Label log,
        TelemetryListenerInterface telemetryListener, String userID,
        int sessionID, EncounterEnum completedEncType,
        DifficultyEnum difficulty, int completedStage, Runnable onLeaveShop) {

        this.gameManager = gameManager;
        this.root = root;
        this.log = log;
        this.telemetryListener = telemetryListener;
        this.userID = userID;
        this.sessionID = sessionID;
        this.completedEncType = completedEncType;
        this.difficulty = difficulty;
        this.completedStage = completedStage;
        this.onLeaveShop = onLeaveShop;
    }

    // Renders the shop: lists upgrades, disables button on purchase
    public void show() {
        root.getChildren().clear();
        root.setAlignment(Pos.CENTER);   // centers the whole shop layout
        root.setStyle("-fx-background-color: #1e1f22;");

        PlayerInterface player = gameManager.getCurrentPlayer();

        Label heading;
        if (player != null) {
            heading = new Label("SHOP  (Coins: " + player.getCoins() + ")");
        } else {
            heading = new Label("SHOP  (Coins: 0)");
        }
        heading.setStyle(HEADING_STYLE);
        log.setStyle(SECONDARY_TEXT_STYLE);
        log.setWrapText(true);
        UpgradeEnum[] upgrades = gameManager.viewShop();

        VBox items = new VBox(10);
        items.setAlignment(Pos.CENTER);   // centers the shop buttons

        if (upgrades != null) {
            for (UpgradeEnum u : upgrades) {

                if (u == null) {
                    continue;
                }

                Button b = new Button(u.getDisplayName() + " (cost: " + u.getPrice() + ")");
                b.setPrefWidth(500);   // increased width to fit all abilities
                b.setStyle(PRIMARY_BUTTON_STYLE);

                b.setOnAction(e -> {
                    try {
                        gameManager.purchaseUpgrade(u);
                        telemetryListener.onBuyUpgrade(new BuyUpgradeEvent(
                                userID, sessionID, Instant.now(),
                                completedEncType, difficulty, completedStage,
                                u, u.getPrice()));
                        log.setText("Bought " + u.getDisplayName());
                        b.setDisable(true);
                        b.setText(u.getDisplayName() + " (bought)");
                        b.setStyle(SECONDARY_BUTTON_STYLE);
                    } catch (LackingResourceException ex) {
                        log.setText("Not enough coins");
                    }

                    PlayerInterface p = gameManager.getCurrentPlayer();
                    if (p != null) {
                        heading.setText("SHOP   (Coins: " + p.getCoins() + ")");
                    }
                });

                items.getChildren().add(b);
            }
        }

        Button leave = new Button("Leave Shop");
        leave.setOnAction(e -> onLeaveShop.run());
        leave.setStyle(SECONDARY_BUTTON_STYLE);

        VBox panel = new VBox(18, heading, items, log, leave);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(24));
        panel.setMaxWidth(600);
        panel.setStyle(PANEL_STYLE);

        root.getChildren().add(panel);
    }
}
