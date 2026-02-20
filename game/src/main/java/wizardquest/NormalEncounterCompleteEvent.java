package wizardquest;

import java.time.Instant;

public class NormalEncounterCompleteEvent extends EncounterCompleteEvent{
    /**
     * Constructor for NormalEncounterCompleteEvent.
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
    public NormalEncounterCompleteEvent(String userID, int sessionID,
            Instant timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int playerHPRemaining){
        super(userID, sessionID, timeStamp, "NormalEncounterComplete",
            encounterName, difficulty, stageNumber, playerHPRemaining);
    }
}
