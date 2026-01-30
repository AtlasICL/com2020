package WizardQuest;

public class SessionStartEvent extends TelemetryEvent {
    /**
     * Constructor for the session start telemetry event. Produces a telemetry event storing
     * common data.
     */
    public SessionStartEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName) {
        super(source, userID, sessionID, timeStamp, telemetryName);
    }
}
