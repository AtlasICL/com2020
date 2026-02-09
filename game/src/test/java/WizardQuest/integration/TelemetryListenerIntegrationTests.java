package WizardQuest.integration;

import WizardQuest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TelemetryListenerIntegrationTests {

    /**
     * Creates and authenticates a mock user account with the role of Player, which will be used to independently
     * execute each test.
     *
     * @throws AuthenticationException if the username is already in use or is invalid when trying to create an account,
     * or the credentials are invalid when trying to authenticate the user.
     */
    @BeforeEach
    void setUp() throws AuthenticationException {
        SettingsSingleton.getSettings().createNewUser("TestUser", "TestPassword", Role.PLAYER);
        SettingsSingleton.getSettings().authenticateUser("TestUser", "TestPassword");
    }

    /**
     * If the player is opted into telemetry, then telemetry events should be written to a JSON object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event written to JSON if opted into telemetry")
    void onNormalEncounterStart_telemetryWrittenIfOptedIn() throws AuthenticationException {
        // Enable telemetry for the authenticated user.
        SettingsSingleton.getSettings().setTelemetryEnabled(true);
        // Initialise a valid NormalEncounterStartEvent object for the authenticated user.
        NormalEncounterStartEvent testEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(testEvent);
        // Implement logic here
    }

    /**
     * Alternatively, if the player has opted out of telemetry, then telemetry events should not be written to a JSON
     * object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event not written to JSON if opted out of telemetry")
    void onNormalEncounterStart_telemetryNotWrittenIfOptedOut() throws AuthenticationException {
        // Disable telemetry for the authenticated user.
        SettingsSingleton.getSettings().setTelemetryEnabled(false);
        // Initialise a valid NormalEncounterStartEvent object for the authenticated user.
        NormalEncounterStartEvent testEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(testEvent);
        // Implement logic here
    }

    /**
     * A JSON object, once its fields have been populated and validated, is sent to the Python telemetry application.
     */
    @Test
    @DisplayName("TelemetryListener - Sent JSON object is received by telemetry application")
    void onNormalEncounterStart_sentJSONObjectIsReceivedByTelemetryApplication() {}

}
