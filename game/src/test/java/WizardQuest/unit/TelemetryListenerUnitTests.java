package WizardQuest.unit;

import WizardQuest.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class TelemetryListenerUnitTests {

    /*
    * KNOWN ERRORS:
    * - ValidationException swallowed in TelemetryListenerSingleton, so JUnit does not detect it
    * - Issue with RoleEnum not being recognised
    * - CleanUp method - may need to invoke EndSessionEvent?
     */

    @TempDir
    private Path tempDir;

    /**
     * Creates and authenticates a mock user account with the role of Player, which will be used to independently
     * execute each test.
     *
     * @throws AuthenticationException if the username is already in use or is invalid when trying to create an account,
     * or the credentials are invalid when trying to authenticate the user.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Initialise temporary JSON files for a test user's login and settings to be stored in.
        File TEMP_LOGINS_FILE = tempDir.resolve("logins_file.json").toFile();
        SettingsSingleton.getInstance().setLoginsDestinationFile(TEMP_LOGINS_FILE);
        File TEMP_SETTINGS_FILE = tempDir.resolve("settings_file.json").toFile();
        SettingsSingleton.getInstance().setSettingsDestinationFile(TEMP_SETTINGS_FILE);
        // Create and authenticate a test user.
        SettingsSingleton.getInstance().createNewUser("TestUser", "TestPassword", RoleEnum.PLAYER);
        SettingsSingleton.getInstance().authenticateUser("TestUser", "TestPassword");
        // Start a session for the test user, which will allow our test event to be invoked in each test.
        StartSessionEvent startSession = new StartSessionEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                DifficultyEnum.MEDIUM);
        TelemetryListenerSingleton.getInstance().onStartSession(startSession);
    }

    /**
     * All telemetry events contain the userID field, which uniquely identifies the user who triggered the event.
     * The userID in the event field should match the userID in the settings of the authenticated user.
     */
    @Test
    @DisplayName("TelemetryListener - userID field input validated")
    void onNormalEncounterStart_userIDValidated() throws Exception {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The userID field will be incremented by 1, creating a mismatch between the value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID() + 1,
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // As a result of this mismatch, UserValidationException should be thrown when an event occurs.
        assertThrows(UserValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent);
        });

        // Initialise a valid NormalEncounterStartEvent object for the authenticated user.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // Now the value of userID in the authenticated user's settings matches the value in the event field.
        // Therefore, UserValidationException should not be thrown when an event occurs.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(validTestEvent);
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
    void onNormalEncounterStart_sessionIDValidated() throws Exception {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The sessionID field will be incremented by 1, creating a mismatch between the value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID() + 1,
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // As a result of this mismatch, UserValidationException should be thrown when an event occurs.
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent);
        });

        // Initialise a StartSessionEvent object for the authenticated user.
        // This should be rejected, as a session is currently running for the user.
        StartSessionEvent testStartEvent = new StartSessionEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                DifficultyEnum.MEDIUM);
        // This should result in SessionValidationException being thrown as a session is already running.
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onStartSession(testStartEvent);
        });

        // Initialise TWO EndSessionEvent objects for the authenticated user.
        // The first one should work as normal, since a session is currently running and can be ended.
        // The second one should fail, as there is no session currently running now.
        EndSessionEvent testEndEvent1 = new EndSessionEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()));
        EndSessionEvent testEndEvent2 = new EndSessionEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()));
        // This ordering of events should result in SessionValidationException being thrown only for the second event.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getInstance().onEndSession(testEndEvent1);
        });
        assertThrows(SessionValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onEndSession(testEndEvent2);
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
    void onNormalEncounterStart_timeStampValidated() throws Exception {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The timestamp field will be of a format that is not parseable to a LocalDateTime object.
        NormalEncounterStartEvent invalidTestEvent1 = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                "Invalid TimeStamp Example",
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // Since the value cannot be parsed, TimestampValidationException should be thrown when an event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent1);
        });

        // Initialise an invalid NormalEncounterStartEvent object for the authenticated user.
        // The timestamp field will be of a time that is in the future.
        NormalEncounterStartEvent invalidTestEvent2 = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                "3000/01/01/15/00/00", // 1st January 3000 at 15:00:00
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // Since the time is in the future, TimestampValidationException should be thrown when an event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent2);
        });

        // Initialise TWO NormalEncounterStartEvent objects for the authenticated user.
        // The first one will contain a valid timestamp, and so should be accepted.
        // The second one will contain a timestamp earlier than that of the first one.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()), // 1st January 2026 at 15:00:00
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        NormalEncounterStartEvent invalidTestEvent3 = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                "2025/01/01/15/00/00", // 1st January 2025 at 15:00:00 - one year earlier
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
        // The timestamp for the first event is valid, so calling onNormalEncounterStart should cause no issues.
        assertDoesNotThrow(() -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(validTestEvent);
        });
        // The timestamp for the second event is earlier than the timestamp of the most recent event.
        // Therefore, TimestampValidationException should be thrown when the second event occurs.
        assertThrows(TimestampValidationException.class, () -> {
            TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent3);
        });
    }

    @AfterEach
    void cleanUp() {
        SettingsSingleton.getInstance().resetLoginsDestinationFile();
        SettingsSingleton.getInstance().resetSettingsDestinationFile();
    }

}
