package wizardquest.telemetry;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class TelemetryEvent {
    private final String userID;
    private final Instant timeStamp;
    private final String event;

    /**
     * Constructor for the base telemetry event. Produces a telemetry event storing
     * common data.
     * 
     * @param source    the object that constructed the telemetry event.
     * @param userID    the ID of the user who is playing the game when the
     *                  event is
     *                  constructed.
     * @param sessionID the ID of the session the user is currently playing. See
     *                  TelemetryListenerInterface for information about
     *                  sessions.
     * @param timeStamp the time the event was constructed in the format
     *                  yyyy/mm/dd/hh/mm/ss
     * @param event     the name of the telemetry event according to the JSON
     *                  telemetry specification.
     */
    public TelemetryEvent(String userID, Instant timeStamp, String event) {
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.event = event;
    }

    /**
     * Gets the stored user ID.
     * 
     * @return the user's ID.
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Gets the stored timestamp.
     * 
     * @return the timestamp.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd/HH/mm/ss", timezone = "UTC")
    public Instant getTimestamp() {
        return this.timeStamp;
    }

    /**
     * Gets the name of the telemetry event according to the telemetry
     * specification.
     * 
     * @return the event's name.
     */
    public String getEvent() {
        return this.event;
    }
}
