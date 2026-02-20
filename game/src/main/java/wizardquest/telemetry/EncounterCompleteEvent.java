package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public abstract class EncounterCompleteEvent extends EncounterEvent{
    private final int player_HP_remaining;

    /**
     * Constructor for EncounterCompleteEvent.
     *
     * @param userID            the ID of the user who is playing the game when the
     *                          event is
     *                          constructed.
     * @param sessionID         the ID of the session the user is currently playing. See
     *                          TelemetryListenerInterface for information about
     *                          sessions.
     * @param timeStamp         the time the event was constructed.
     * @param telemetryName     name of the type of encounter event.
     * @param encounterName     the name of the encounter a player is fighting.
     * @param difficulty        the difficulty used for the player's session.
     * @param stageNumber       the stage player has completed.
     * @param playerHPRemaining player HP remaining after completion.
     */
    public EncounterCompleteEvent(String userID, int sessionID,
            Instant timeStamp, String telemetryName, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int playerHPRemaining) {
        super(userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
        this.player_HP_remaining = playerHPRemaining;
    }

    /**
     * Gets stored player HP.
     *
     * @return player HP remaining after encounter complete.
     */
    public int getPlayer_HP_remaining(){
        return this.player_HP_remaining;
    }
}
