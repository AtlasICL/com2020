package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.settings.DifficultyEnum;

public class StartSessionEvent extends SessionEvent {

    private final DifficultyEnum difficulty;

    /**
     * Constructor for StartSessionEvent.
     * 
     * @param source     the object that constructed the telemetry event.
     * @param userID     the ID of the user who is playing the game when the
     *                   event is
     *                   constructed.
     * @param sessionID  the ID of the session the user is currently playing. See
     *                   TelemetryListenerInterface for information about
     *                   sessions.
     * @param timeStamp  the time the event was constructed in the format
     *                   yyyy/mm/dd/hh/mm/ss.
     * @param difficulty the difficulty used for the player's session.
     */
    public StartSessionEvent(String userID, int sessionID, Instant timeStamp, DifficultyEnum difficulty) {
        super(userID, sessionID, timeStamp, "StartSession");
        this.difficulty = difficulty;
    }

    /**
     * Gets the difficulty of the session.
     * 
     * @return the difficulty of the session.
     */
    public DifficultyEnum getDifficulty() {
        return this.difficulty;
    }
}
