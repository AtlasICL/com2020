package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public class BuyUpgradeEvent extends EncounterEvent {
    private final UpgradeEnum upgrade_bought;
    private final int coins_spent;

    /**
     * Constructor for BuyUpgradeEvent.
     *
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed.
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the stage player has completed.
     * @param upgradeBought the upgrade a player has purchased.
     * @param coinsSpent    the number of coins spent by the player on an upgrade.
     */
    public BuyUpgradeEvent(String userID, int sessionID,
            Instant timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber,
            UpgradeEnum upgradeBought, int coinsSpent) {
        super(userID, sessionID, timeStamp, "BuyUpgrade", encounterName, difficulty, stageNumber);
        this.upgrade_bought = upgradeBought;
        this.coins_spent = coinsSpent;
    }

    /**
     * Gets the stored upgrade bought.
     *
     * @return upgrade a player has purchased.
     */
    public UpgradeEnum getUpgrade_bought() {
        return upgrade_bought;
    }

    /**
     * Gets the stored number of coins spent.
     *
     * @return coins spent on an upgrade.
     */
    public int getCoins_spent() {
        return this.coins_spent;
    }
}
