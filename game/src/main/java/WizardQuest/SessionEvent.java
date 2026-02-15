package WizardQuest;

import java.time.Instant;

public class SessionEvent extends TelemetryEvent {
    private final int sessionID;

    public SessionEvent(String userID, int sessionID, Instant timeStamp, String event) {
        super(userID, timeStamp, event);
        this.sessionID = sessionID;
    }

    /**
     * Gets the stored session ID.
     * 
     * @return the session ID.
     */
    public int getSessionID() {
        return this.sessionID;
    }
}
