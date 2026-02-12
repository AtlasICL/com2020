package WizardQuest.integration;

import WizardQuest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    void setUp() throws AuthenticationException, IOException {
        // Initialise temporary JSON files for a test user's login to be stored in.
        File TEMP_LOGINS_FILE = tempDir.resolve("logins_file.json").toFile();
        TEMP_LOGINS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_LOGINS_FILE)) {
            fw.write("{}");
        }
        SettingsSingleton.getInstance().setLoginsDestinationFile(TEMP_LOGINS_FILE);

        // Initialise and create temporary JSON files for a test user's settings to be stored in.
        File TEMP_SETTINGS_FILE = tempDir.resolve("settings_file.json").toFile();
        TEMP_SETTINGS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_SETTINGS_FILE)) {
            fw.write("{}");
        }
        SettingsSingleton.getInstance().setSettingsDestinationFile(TEMP_SETTINGS_FILE);

        // Initialise a temporary JSON file to test the reading and writing of event objects.
        TEMP_DESTINATION_FILE = tempDir.resolve("events.json").toFile();
        TEMP_DESTINATION_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_DESTINATION_FILE)) {
            fw.write("{}");
        }
        TelemetryListenerSingleton.getInstance().setDestinationFile(TEMP_DESTINATION_FILE);

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
        // Enable telemetry for the authenticated user.
        SettingsSingleton.getInstance().setTelemetryEnabled(true);
        // Since telemetry is enabled, the event should be written to events.json.
        // Therefore, the length of the file should increase from the listener method being invoked.
        long lengthBeforeWrite = TEMP_DESTINATION_FILE.length();
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(this.testEvent);
        long lengthAfterWrite = TEMP_DESTINATION_FILE.length();
        assertTrue(lengthBeforeWrite < lengthAfterWrite);
    }

    /**
     * Alternatively, if the player has opted out of telemetry, then telemetry events should not be written to a JSON
     * object as they occur.
     */
    @Test
    @DisplayName("TelemetryListener - Telemetry Event not written to JSON if opted out of telemetry")
    void onNormalEncounterStart_telemetryNotWrittenIfOptedOut() throws Exception {
        // Disable telemetry for the authenticated user.
        SettingsSingleton.getInstance().setTelemetryEnabled(false);
        // Since telemetry is disabled, the event should not be written to events.json.
        // Therefore, the length of the file should not change from the listener method being invoked.
        long lengthBefore =  TEMP_DESTINATION_FILE.length();
        TelemetryListenerSingleton.getInstance().onNormalEncounterStart(this.testEvent);
        long lengthAfter = TEMP_DESTINATION_FILE.length();
        assertEquals(lengthBefore, lengthAfter);
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
