package WizardQuest;

public class EndSessionEvent extends TelemetryEvent{
    /**
     * Constructor for EndSessionEvent.
     * 
     * @param source        the object that constructed the telemetry event.
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss.
     * @param difficulty    the difficulty used for the players session.
     */
    public EndSessionEvent(Object source, int userID, int sessionID, String timeStamp){
        super(source, userID, sessionID, timeStamp, "EndSession");
    }
}
