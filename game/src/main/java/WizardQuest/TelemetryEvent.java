package WizardQuest;

import java.util.EventObject;

public abstract class TelemetryEvent extends EventObject {
    private final int userID;
    private final int sessionID;
    private final String timesTamp;

    /**
     * Constructor for the base telemetry event. Produces a telemetry event storing
     * common data.
     * 
     * @param source    the object that constructed the telemetry event.
     * @param userID    the ID of the user who is playing the game when the event is
     *                  constructed.
     * @param sessionID the ID of the session the user is currently playing. See
     *                  TelemetryListenerInterface for information about sessions.
     * @param timeStamp the time the event was constructed in the format
     *                  yyy/mm/dd/hh/mm/ss
     */
    public TelemetryEvent(Object source, int userID, int sessionID, String timeStamp) {
        super(source);
        this.userID = userID;
        this.sessionID = sessionID;
        this.timesTamp = timeStamp;
    }

    /**
     * Gets the stored user ID.
     * @return the user's ID.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets the stored session ID.
     * @return the session ID.
     */
    public int getSessionID() {
        return this.sessionID;
    }

    /**
     * Gets the stored timestamp.
     * @return the timestamp.
     */
    public String getTimestamp() {
        return this.timesTamp;
    }
}
