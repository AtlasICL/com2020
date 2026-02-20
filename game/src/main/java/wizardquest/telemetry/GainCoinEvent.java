package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public class GainCoinEvent extends EncounterEvent{
    private final int coins_gained;

    /**
     * Constructor for GainCoinEvent.
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
     * @param stageNumber   the stage player is collecting coins on.
     * @param coinsGained   number of coins gained by the player.
     */
    public GainCoinEvent(String userID, int sessionID,
        Instant timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int coinsGained){
        super(userID, sessionID, timeStamp, "GainCoin", encounterName, difficulty, stageNumber);
        this.coins_gained = coinsGained;
    }

    /**
     * Gets the stored number of coins gained.
     *
     * @return number of coins gained.
     */
    public int getCoins_gained(){
        return this.coins_gained;
    }
}
