package WizardQuest.unit;

import WizardQuest.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TelemetryListenerUnitTests {

    /**
     * Creates a test user account with the role of Player, which will be used to independently execute each test.
     * Also creates a mock event NormalEncounterStartEvent, which will be used to test the behaviour of the abstract
     * class TelemetryEvent, of which it extends.
     *
     * @throws AuthenticationException if the username is already in use or is invalid when trying to create an account,
     * or the credentials are invalid when trying to authenticate the user.
     */
    @BeforeEach
    void setUp() throws AuthenticationException {
        SettingsSingleton.getSettings().createNewUser("TestUser", "TestPassword", Role.PLAYER);
        SettingsSingleton.getSettings().authenticateUser("TestUser", "TestPassword");
        NormalEncounterStartEvent testEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerSingleton.getTimeManager().convertDateTime(LocalDateTime.now()), // Fix this
                "NormalEncounterStart",
                "EncounterName",
                1,
                Difficulty.MEDIUM);
    }

    /**
     * If the player is opted into telemetry, then telemetry events should be written to a JSON object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event written to JSON if opted into telemetry")
    void onNormalEncounterStart_telemetryWrittenIfOptedIn() {
    }

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
