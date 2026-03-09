package wizardquest.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.LackingResourceException;

public class ShopPage {
    private final GameManagerInterface gameManager;
    private final VBox root;
    private final Label log;
    private final Runnable onLeaveShop;

    public ShopPage(GameManagerInterface gameManager, VBox root, Label log, Runnable onLeaveShop) {
        this.gameManager = gameManager;
        this.root = root;
        this.log = log;
        this.onLeaveShop = onLeaveShop;
    }

    // Renders the shop: lists upgrades, disables button on purchase
    public void show() {
        root.getChildren().clear();
        PlayerInterface player = gameManager.getCurrentPlayer();
        Label heading;
        if (player != null) {
            heading = new Label("SHOP  (Coins: " + player.getCoins() + ")");
        }
        else {
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
