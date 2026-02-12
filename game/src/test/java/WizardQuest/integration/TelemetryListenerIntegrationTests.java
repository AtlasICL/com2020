package WizardQuest.integration;

import WizardQuest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class TelemetryListenerIntegrationTests {

    private NormalEncounterStartEvent testEvent;
    private File TEMP_DESTINATION_FILE;

    @TempDir
    private Path tempDir;

    /**
     * Creates and authenticates a mock user account with the role of Player, which will be used to independently
     * execute each test.
     * Also sets a temporary filepath as the telemetry listener's destination filepath, using JUnit's @TempDir.
     * Creates a valid test telemetry event to be used for each integration test.
     *
     * @throws AuthenticationException if the username is already in use or is invalid when trying to create an account,
     * or the credentials are invalid when trying to authenticate the user.
     */
    @BeforeEach
    void setUp() throws AuthenticationException {
        // Initialise temporary JSON files for a test user's login and settings to be stored in.
        File TEMP_LOGINS_FILE = tempDir.resolve("logins_file.json").toFile();
        SettingsSingleton.getInstance().setLoginsDestinationFile(TEMP_LOGINS_FILE);
        File TEMP_SETTINGS_FILE = tempDir.resolve("settings_file.json").toFile();
        SettingsSingleton.getInstance().setSettingsDestinationFile(TEMP_SETTINGS_FILE);
        // Create and authenticate a test user.
        SettingsSingleton.getInstance().createNewUser("TestUser", "TestPassword", RoleEnum.PLAYER);
        SettingsSingleton.getInstance().authenticateUser("TestUser", "TestPassword");
        // Initialise a temporary JSON file to test the reading and writing of event objects.
        TEMP_DESTINATION_FILE = tempDir.resolve("events.json").toFile();
        TelemetryListenerSingleton.getInstance().setDestinationFile(TEMP_DESTINATION_FILE);
        // Start a session for the test user, which will allow our test event to be invoked in each test.
        StartSessionEvent startSession = new StartSessionEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                DifficultyEnum.MEDIUM);
        TelemetryListenerSingleton.getInstance().onStartSession(startSession);
        this.testEvent = new NormalEncounterStartEvent(new Object(),
                SettingsSingleton.getInstance().getUserID(),
                SettingsSingleton.getInstance().getSessionID(),
                TimeManagerInterface.convertDateTime(LocalDateTime.now()),
                EncounterEnum.ENCOUNTERTYPE_1,
                DifficultyEnum.MEDIUM,
                1);
    }

    /**
     * If the player is opted into telemetry, then telemetry events should be written to a JSON object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event written to JSON if opted into telemetry")
    void onNormalEncounterStart_telemetryWrittenIfOptedIn() throws Exception {
        // Enable telemetry for the authenticated user and invoke the relevant listener method.
        SettingsSingleton.getInstance().setTelemetryEnabled(true);
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(this.testEvent);
        // Since telemetry is enabled, the file should now be created according to the file path.
        // The file should be non-empty, it should contain the details of this telemetry event.
        assertTrue(TEMP_DESTINATION_FILE.exists());
        assertTrue(TEMP_DESTINATION_FILE.length() > 0);
    }

    /**
     * Alternatively, if the player has opted out of telemetry, then telemetry events should not be written to a JSON
     * object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event not written to JSON if opted out of telemetry")
    void onNormalEncounterStart_telemetryNotWrittenIfOptedOut() throws Exception {
        // Disable telemetry for the authenticated user and invoke the relevant listener method.
        SettingsSingleton.getInstance().setTelemetryEnabled(false);
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(this.testEvent);
        // Since telemetry is disabled, the file should not be created at all.
        assertFalse(TEMP_DESTINATION_FILE.exists());
    }

    /**
     * Resets the telemetry listener's filepath to the filepath used for production.
     */
    @AfterEach
    void cleanUp() {
        SettingsSingleton.getInstance().resetLoginsDestinationFile();
        SettingsSingleton.getInstance().resetSettingsDestinationFile();
        TelemetryListenerSingleton.getInstance().resetDestinationFile();
    }
}
