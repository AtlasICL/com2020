package WizardQuest.unit;

import WizardQuest.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TelemetryListenerUnitTests {

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
     * All telemetry events contain the userID field, which uniquely identifies the user who triggered the event.
     * The userID in the event field should match the userID in the settings of the authenticated user.
     */
    @Test
    @DisplayName("TelemetryListener - userID field input validated")
    void onNormalEncounterStart_userIDValidated() throws AuthenticationException {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The userID field will be incremented by 1, creating a mismatch between the value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID() + 1,
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // As a result of this mismatch, UserValidationException should be thrown when an event occurs.
        assertThrows(UserValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent);
        });

        // Initialise a valid NormalEncounterStartEvent object for the authenticated user.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // Now the value of userID in the authenticated user's settings matches the value in the event field.
        // Therefore, UserValidationException should not be thrown when an event occurs.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(validTestEvent);
        });
    }

    /**
     * All telemetry events contain the sessionID field, which uniquely identifies the session in which the event
     * occurred.
     * The sessionID in the event field should match the sessionID in the settings of the authenticated user.
     * A SessionEndEvent cannot occur before a SessionStartEvent has happened.
     * Likewise, a SessionStartEvent cannot occur before a SessionEndEvent to end the user's prior session has happened.
     */
    @Test
    @DisplayName("TelemetryListener - sessionID field input validated")
    void onNormalEncounterStart_sessionIDValidated() throws AuthenticationException {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The sessionID field will be incremented by 1, creating a mismatch between the value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID() + 1,
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // As a result of this mismatch, UserValidationException should be thrown when an event occurs.
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent);
        });

        // Initialise a valid EndSessionEvent object for the authenticated user.
        // Although each field value is valid, this event can not logically occur before a SessionStartEvent.
        EndSessionEvent testEndEvent = new EndSessionEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "EndSession"
                );
        // This ordering of events should result in SessionValidationException being thrown.
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onEndSession(testEndEvent);
        });

        // Initialise TWO StartSessionEvent objects for the authenticated user.
        // The first one should be accepted as normal, as no session is currently running for the user.
        // The second one should be rejected, as a session is currently running for the user.
        SessionStartEvent testStartEvent1 = new SessionStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "SessionStart"
                );
        SessionStartEvent testStartEvent2 = new SessionStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                "SessionStart"
        );
        // No session is currently running for the user, so calling onSessionStart should cause no issues.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getTelemetryListener().onSessionStart(testStartEvent1);
        });
        // Now that a session has been started, and onEndSession has not been called since,
        // calling onSessionStart again should result in SessionValidationException being thrown.
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onSessionStart(testStartEvent2);
        });
    }

    /**
     * All telemetry events contain the timeStamp field, which identifies the time at which the event occurred.
     * timeStamp should be parsable to a LocalDateTime object.
     * It should not be a time in the future, nor should it be before the time stamp of the last event that
     * occurred before this one.
     */
    @Test
    @DisplayName("TelemetryListener - timeStamp field input validated")
    void onNormalEncounterStart_timeStampValidated() throws AuthenticationException {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The timestamp field will be of a format that is not parseable to a LocalDateTime object.
        NormalEncounterStartEvent invalidTestEvent1 = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                "Invalid TimeStamp Example",
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // Since the value cannot be parsed, TimestampValidationException should be thrown when an event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent1);
        });

        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The timestamp field will be of a time that is in the future.
        NormalEncounterStartEvent invalidTestEvent2 = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                "3000/01/01/15/00/00", // 1st January 3000 at 15:00:00
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // Since the time is in the future, TimestampValidationException should be thrown when an event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent2);
        });

        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The timestamp field will be of a time that is in the future.
        NormalEncounterStartEvent invalidTestEvent3 = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                "3000/01/01/15/00/00", // 1st January 3000 at 15:00:00
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // Since the time is in the future, TimestampValidationException should be thrown when an event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent3);
        });

        // Initialise TWO NormalEncounterStartEvent objects for the authenticated user.
        // The first one will contain a valid timestamp, and so should be accepted.
        // The second one will contain a timestamp earlier than that of the first one.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                "2026/01/01/15/00/00", // 1st January 2026 at 15:00:00
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        NormalEncounterStartEvent invalidTestEvent4 = new NormalEncounterStartEvent(null,
                SettingsSingleton.getSettings().getUserID(),
                SettingsSingleton.getSettings().getSessionID(),
                "2025/01/01/15/00/00", // 1st January 2025 at 15:00:00 - one year earlier
                "NormalEncounterStart",
                EncounterType.ENCOUNTERTYPE_1,
                1,
                Difficulty.MEDIUM);
        // The timestamp for the first event is valid, so calling onNormalEncounterStart should cause no issues.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(validTestEvent);
        });
        // The timestamp for the second event is earlier than the timestamp of the most recent event.
        // Therefore, TimestampValidationException should be thrown when the second event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getTelemetryListener().onNormalEncounterStart(invalidTestEvent4);
        });
    }
}
