package WizardQuest;

public class EndSessionEvent extends TelemetryEvent {
    /**
     * Constructor for the end session telemetry event. Produces a telemetry event storing
     * common data.
     */
    public EndSessionEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName) {
        super(source, userID, sessionID, timeStamp, telemetryName);
    }
}
