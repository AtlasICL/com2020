package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public class BossEncounterCompleteEvent extends EncounterCompleteEvent{
    /**
     * Constructor for BossEncounterCompleteEvent.
     *
     * @param userID            the ID of the user who is playing the game when the
     *                          event is
     *                          constructed.
     * @param sessionID         the ID of the session the user is currently playing. See
     *                          TelemetryListenerInterface for information about
     *                          sessions.
     * @param timeStamp         the time the event was constructed.
     * @param encounterName     the name of the encounter a player is fighting.
     * @param difficulty        the difficulty used for the player's session.
     * @param stageNumber       the stage player has completed.
     * @param playerHPRemaining player HP remaining after completion.
     */
    public BossEncounterCompleteEvent(String userID, int sessionID,
            Instant timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int playerHPRemaining){
        super(userID, sessionID, timeStamp, "BossEncounterComplete",
            encounterName, difficulty, stageNumber, playerHPRemaining);
    }
}
