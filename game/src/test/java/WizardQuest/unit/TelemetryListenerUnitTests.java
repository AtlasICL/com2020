package WizardQuest.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions.*;

public class TelemetryListenerUnitTests {

    // NormalEncounterStartEvent will be used to test the behaviour of the abstract class TelemetryEvent,
    // of which NormalEncounterStartEvent extends.

    /**
     * If the player is opted into telemetry, then telemetry events should be written to a JSON object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event written to JSON if opted into telemetry")
    void onNormalEncounterStart_telemetryWrittenIfOptedIn() {}

    /**
     * Alternatively, if the player has opted out of telemetry, then telemetry events should not be written to a JSON
     * object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event not written to JSON if opted out of telemetry")
    void onNormalEncounterStart_telemetryNotWrittenIfOptedOut() {}

    /**
     * All telemetry events contain the userID field, which uniquely identifies the user who triggered the event.
     * userID should be a positive integer.
     */
    @Test
    @DisplayName("TelemetryListener - userID field input validated")
    void onNormalEncounterStart_userIDValidated() {}

    /**
     * All telemetry events contain the sessionID field, which uniquely identifies the session in which the event
     * occurred.
     * sessionID should be a positive integer.
     */
    @Test
    @DisplayName("TelemetryListener - sessionID field input validated")
    void onNormalEncounterStart_sessionIDValidated() {}

    /**
     * All telemetry events contain the timeStamp field, which identifies the time at which the event occurred.
     * timeStamp should be a LocalDateTime object that is not in the future, nor extremely far in the past.
     */
    @Test
    @DisplayName("TelemetryListener - timeStamp field input validated")
    void onNormalEncounterStart_timeStampValidated() {}
}
