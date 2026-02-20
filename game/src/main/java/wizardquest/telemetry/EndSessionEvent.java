package wizardquest.telemetry;

import java.time.Instant;

public class EndSessionEvent extends SessionEvent{
    /**
     * Constructor for EndSessionEvent.
     *
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed.
     */
    public EndSessionEvent(String userID, int sessionID, Instant timeStamp){
        super(userID, sessionID, timeStamp, "EndSession");
    }
}
