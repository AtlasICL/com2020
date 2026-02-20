package wizardquest.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.RoleEnum;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.gamemanager.TimeManagerSingleton;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.EndSessionEvent;
import wizardquest.telemetry.NormalEncounterStartEvent;
import wizardquest.telemetry.StartSessionEvent;
import wizardquest.telemetry.TelemetryListenerSingleton;

public class TelemetryListenerUnitTests {

    private PrintStream defaultError;
    private ByteArrayOutputStream exError;

    @TempDir
    private Path tempDir;

    /**
     * Creates and authenticates a mock user account with the role of Player, which
     * will be used to independently execute each test.
     *
     * @throws AuthenticationException if the username is already in use or is
     *                                 invalid when trying to create an account,
     *                                 or the credentials are invalid when trying to
     *                                 authenticate the user.
     */
    @BeforeEach
    void setUp() throws AuthenticationException, IOException {
        // Initialise and create temporary JSON files for a test user's login to be
        // stored in.
        File TEMP_LOGINS_FILE = tempDir.resolve("logins_file.json").toFile();
        TEMP_LOGINS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_LOGINS_FILE)) {
            fw.write("{}");
        }
        SettingsSingleton.getInstance().setLoginsDestinationFile(TEMP_LOGINS_FILE);

        // Initialise and create temporary JSON files for a test user's settings to be
        // stored in.
        File TEMP_SETTINGS_FILE = tempDir.resolve("settings_file.json").toFile();
        TEMP_SETTINGS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_SETTINGS_FILE)) {
            fw.write("{}");
        }
        SettingsSingleton.getInstance().setSettingsDestinationFile(TEMP_SETTINGS_FILE);

        // Initialise a temporary JSON file to test the reading and writing of event
        // objects.
        File TEMP_DESTINATION_FILE = tempDir.resolve("events.json").toFile();
        TEMP_DESTINATION_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_DESTINATION_FILE)) {
            fw.write("{}");
        }
        TelemetryListenerSingleton.getInstance().setDestinationFile(TEMP_DESTINATION_FILE);

        // Log in as a test user.
        SettingsSingleton.getInstance().loginWithResult(
                new AuthenticationResult("TestUser", "TestUserID", RoleEnum.PLAYER));

        // Start a session for the test user, which will allow our test event to be
        // invoked in each test.
        GameManagerSingleton.getInstance().startNewGame(DifficultyEnum.MEDIUM);
        StartSessionEvent startSession = new StartSessionEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now(),
                DifficultyEnum.MEDIUM);
        TelemetryListenerSingleton.getInstance().onStartSession(startSession);

        // Set up a buffer to capture the content of error messages when exceptions are
        // handled.
        defaultError = System.err;
        exError = new ByteArrayOutputStream();
        System.setErr(new PrintStream(exError));
    }

    /**
     * All telemetry events contain the userID field, which uniquely identifies the
     * user who triggered the event.
     * The userID in the event field should match the userID in the settings of the
     * authenticated user.
     */
    @Test
    @DisplayName("TelemetryListener - userID field input validated")
    void onNormalEncounterStart_userIDValidated() {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated
        // user.
        // The userID field will be incremented by 1, creating a mismatch between the
        // value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(
                null,
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now(),
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        // As a result of this mismatch, UserValidationException should be thrown when
        // an event occurs.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent);
        assertTrue(this.exError.toString().contains("UserID of event " + invalidTestEvent.getEvent() +
                " " + invalidTestEvent.getUserID() + " not equal to current sessionID of "));
        // Reset the exception output stream.
        exError.reset();

        // Initialise a valid NormalEncounterStartEvent object for the authenticated
        // user.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now(),
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        // Now the value of userID in the authenticated user's settings matches the
        // value in the event field.
        // Therefore, no exception should be thrown and handled when the method is
        // called.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(validTestEvent);
        assertTrue(this.exError.toString().isEmpty());
    }

    /**
     * All telemetry events contain the sessionID field, which uniquely identifies
     * the session in which the event
     * occurred.
     * The sessionID in the event field should match the sessionID in the settings
     * of the authenticated user.
     * A SessionEndEvent cannot occur before a SessionStartEvent has happened.
     * Likewise, a SessionStartEvent cannot occur before a SessionEndEvent to end
     * the user's prior session has happened.
     */
    @Test
    @DisplayName("TelemetryListener - sessionID field input validated")
    void onNormalEncounterStart_sessionIDValidated() {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated
        // user.
        // The sessionID field will be incremented by 1, creating a mismatch between the
        // value in the
        // authenticated user's settings and the value in the event field.
        NormalEncounterStartEvent invalidTestEvent = new NormalEncounterStartEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID() + 1,
                Instant.now(),
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        // As a result of this mismatch, UserValidationException should be thrown when
        // an event occurs.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent);
        assertTrue(this.exError.toString()
                .contains("SessionID of event " + invalidTestEvent.getEvent() +
                        " " + invalidTestEvent.getSessionID()
                        + " not equal to current sessionID of "));
        // Reset the exception output stream.
        exError.reset();

        // Initialise a StartSessionEvent object for the authenticated user.
        // This should be rejected, as a session is currently running for the user.
        StartSessionEvent testStartEvent = new StartSessionEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now(),
                DifficultyEnum.MEDIUM);
        // This should result in SessionValidationException being thrown as a session is
        // already running.
        TelemetryListenerSingleton.getInstance().onStartSession(testStartEvent);
        assertTrue(this.exError.toString()
                .contains("StartSession for session " + testStartEvent.getSessionID() +
                        " occurs before EndSession of "));
        // Reset the exception output stream.
        exError.reset();

        // Initialise TWO EndSessionEvent objects for the authenticated user.
        // The first one should work as normal, since a session is currently running and
        // can be ended.
        // The second one should fail, as there is no session currently running now.
        EndSessionEvent testEndEvent1 = new EndSessionEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now());
        EndSessionEvent testEndEvent2 = new EndSessionEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now());
        // This first event should be accepted, no exception should be thrown.
        TelemetryListenerSingleton.getInstance().onEndSession(testEndEvent1);
        assertTrue(this.exError.toString().isEmpty());
        // This second event should result in SessionValidationException being thrown.
        TelemetryListenerSingleton.getInstance().onEndSession(testEndEvent2);
        assertTrue(this.exError.toString().contains("EndSession for session " + testEndEvent2.getSessionID() +
                " occurs before its StartSession"));
        // Reset the exception output stream.
        exError.reset();

    }

    /**
     * All telemetry events contain the timeStamp field, which identifies the time
     * at which the event occurred.
     * It should not be a time in the future, nor should it be before the time stamp
     * of the last event that occurred before this one.
     */
    @Test
    @DisplayName("TelemetryListener - timeStamp field input validated")
    void onNormalEncounterStart_timeStampValidated() {
        // Initialise an invalid NormalEncounterStartEvent object for the authenticated
        // user.
        // The timestamp field will be of a time that is in the future.
        NormalEncounterStartEvent invalidTestEvent2 = new NormalEncounterStartEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.parse("3000-01-01T15:00:00Z"), // 1st January 3000 at 15:00:00
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        // Since the time is in the future, TimestampValidationException should be
        // thrown when an event occurs.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent2);
        assertTrue(this.exError.toString()
                .contains("Time stamp of event " + invalidTestEvent2.getEvent() +
                        " " + invalidTestEvent2.getTimestamp() + " is in the future"));
        // Reset the exception output stream.
        exError.reset();

        // Initialise TWO NormalEncounterStartEvent objects for the authenticated user.
        // The first one will contain a valid timestamp, and so should be accepted.
        // The second one will contain a timestamp earlier than that of the first one.
        NormalEncounterStartEvent validTestEvent = new NormalEncounterStartEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                TimeManagerSingleton.getInstance().getCurrentTime(),
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        NormalEncounterStartEvent invalidTestEvent3 = new NormalEncounterStartEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.parse("2026-01-01T15:00:00Z"), // 1st January 2026 at 15:00:00
                EncounterEnum.GOBLIN_ENCOUNTER,
                DifficultyEnum.MEDIUM,
                1);
        // The timestamp for the first event is valid, so calling onNormalEncounterStart
        // should cause no issues.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(validTestEvent);
        assertTrue(this.exError.toString().isEmpty());
        // The timestamp for the second event is earlier than the timestamp of the most
        // recent event.
        // Therefore, TimestampValidationException should be thrown when the second
        // event occurs.
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(invalidTestEvent3);
        assertTrue(this.exError.toString()
                .contains("Time stamp of event " + invalidTestEvent3.getEvent() +
                        " " + invalidTestEvent3.getTimestamp() + " is not current"));
        // Reset the exception output stream.
        exError.reset();
    }

    /**
     * Create and invoke an EndSessionEvent, to ensure that sessionIDs are kept
     * clean between each test.
     * Resets the telemetry listener's filepath to the filepath used for production.
     * Also restores the system error buffer to its original value from before any
     * tests were run.
     */
    @AfterEach
    void cleanUp() {
        EndSessionEvent endSessionEvent = new EndSessionEvent(
                SettingsSingleton.getInstance().getUserID(),
                GameManagerSingleton.getInstance().getSessionID(),
                Instant.now());
        TelemetryListenerSingleton.getInstance().onEndSession(endSessionEvent);
        System.setErr(this.defaultError);
        SettingsSingleton.getInstance().resetLoginsDestinationFile();
        SettingsSingleton.getInstance().resetSettingsDestinationFile();
        TelemetryListenerSingleton.getInstance().resetDestinationFile();
    }
}
