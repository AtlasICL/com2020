package wizardquest.ui;

import java.time.Instant;

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
        PlayerInterface player = gameManager.getCurrentPlayer();
        Label heading;
        if (player != null) {
            heading = new Label("SHOP  (Coins: " + player.getCoins() + ")");
        } else {
            heading = new Label("SHOP  (Coins: " + 0 + ")");
        }

        UpgradeEnum[] upgrades = gameManager.viewShop();
        VBox items = new VBox(4);

        if (upgrades != null) {
            for (UpgradeEnum u : upgrades) {

                if (u == null) {
                    continue;
                }

                Button b = new Button(u.getDisplayName() + " (cost: " + u.getPrice() + ")");
                b.setOnAction(e -> {
                    try {
                        gameManager.purchaseUpgrade(u);
                        telemetryListener.onBuyUpgrade(new BuyUpgradeEvent(
                                userID, sessionID, Instant.now(),
                                completedEncType, difficulty, completedStage,
                                u, u.getPrice()));
                        log.setText("Bought " + u.getDisplayName());
                        b.setDisable(true); // Greys out the button after the purchase.
                        b.setText(u.getDisplayName() + " (bought)");
                    } catch (LackingResourceException ex) {
                        log.setText("Not enough coins");
                    }

                    // Updates the coin display
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
        root.getChildren().addAll(heading, items, log, leave);
    }
}
