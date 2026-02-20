package wizardquest;

import java.time.Instant;

public abstract class EncounterFailEvent extends EncounterEvent{
    private final int lives_left;

    /**
     * Constructor for EncounterFailEvent.
     *
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed.
     * @param telemetryName name of the type of encounter event.
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the stage player has failed.
     * @param livesLeft     player lives remaining after completion.
     */
    public EncounterFailEvent(String userID, int sessionID,
            Instant timeStamp, String telemetryName, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int livesLeft){
        super(userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
        this.lives_left = livesLeft;
    }

    /**
     * Gets stored lives left.
     *
     * @return lives left after encounter failure.
     */
    public int getLives_left(){
        return this.lives_left;
    }
}
