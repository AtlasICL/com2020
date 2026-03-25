package wizardquest.integration.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class SettingsIntegrationTests {

    @TempDir
    private Path tempDir;

    private SettingsInterface settings;

    /**
     * Instantiates a Settings object, and sets temporary file paths for login and
     * settings files, using JUnit's @TempDir.
     */
    @BeforeEach
    void setUp() throws IOException {
        settings = SettingsSingleton.getInstance();
        // Initialise temporary JSON files for a test user's login to be stored in.
        File TEMP_LOGINS_FILE = tempDir.resolve("logins_file.json").toFile();
        TEMP_LOGINS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_LOGINS_FILE)) {
            fw.write("{}");
        }
        settings.setLoginsDestinationFile(TEMP_LOGINS_FILE);

        // Initialise and create temporary JSON files for a test user's settings to be
        // stored in.
        File TEMP_SETTINGS_FILE = tempDir.resolve("settings_file.json").toFile();
        TEMP_SETTINGS_FILE.createNewFile();
        try (FileWriter fw = new FileWriter(TEMP_SETTINGS_FILE)) {
            fw.write("{}");
        }
        settings.setSettingsDestinationFile(TEMP_SETTINGS_FILE);
    }

    /**
     * Only developers have permission to change the role of all users.
     * Players and designers should not be able to do this.
     *
     * @throws AuthenticationException if a player or designer tries to change
     * the role of any user.
     */
    @Test
    @DisplayName("Settings - Only developers may change a user's role")
    void setUserRole_onlyDeveloperCanChangeRole() throws AuthenticationException {
        // Authenticate a user with the role of Player.
        // An exception should be thrown when they try and change a role.
        settings.loginWithResult(new AuthenticationResult(
                "Test Player", "1", RoleEnum.PLAYER));
        assertThrows(AuthenticationException.class , () ->
                settings.setUserRole("1", RoleEnum.DEVELOPER));
        // Authenticate a user with the role of Designer.
        // An exception should be thrown when they try and change a role.
        settings.loginWithResult(new AuthenticationResult(
                "Test Designer", "2", RoleEnum.DESIGNER));
        assertThrows(AuthenticationException.class , () ->
                settings.setUserRole("2", RoleEnum.DEVELOPER));
        // Authenticate a user with the role of Developer.
        // No exception should be thrown when they try and change a role, this
        // should be allowed.
        settings.loginWithResult(new AuthenticationResult(
                "Test Developer", "3", RoleEnum.DEVELOPER));
        assertDoesNotThrow(() ->
                settings.setUserRole("2", RoleEnum.PLAYER));
    }

    /**
     * Only developers and designers have permission to modify design parameters.
     * Players should not be able to do this.
     *
     * @throws AuthenticationException if a player tries to change the role of
     * any user.
     */
    @Test
    @DisplayName("Settings - Only developers and designers may modify design parameters")
    void saveDesignParameters_playersCannotChangeParameters() throws AuthenticationException {
        // Authenticate a user with the role of Player.
        // An exception should be thrown when they try and modify a design parameter.
        int initialSettingValue = settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM);
        settings.loginWithResult(new AuthenticationResult(
                "Test Player", "1", RoleEnum.PLAYER));
        assertThrows(AuthenticationException.class , () ->
                settings.setPlayerMaxHealth(DifficultyEnum.MEDIUM, 200));
        // This should not have written anything to file - the value after invoking the above
        // method should be the same as before.
        assertEquals(initialSettingValue, settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM));
        // Authenticate a user with the role of Designer.
        // No exception should be thrown when they try and modify a design parameter, this
        // should be allowed.
        settings.loginWithResult(new AuthenticationResult(
                "Test Designer", "2", RoleEnum.DESIGNER));
        assertDoesNotThrow(() ->
                settings.setPlayerMaxHealth(
                        DifficultyEnum.MEDIUM,
                        settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM) + 10));
        // This should have written to file.
        assertEquals(initialSettingValue + 10, settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM));
        // Authenticate a user with the role of Developer.
        // No exception should be thrown when they try and modify a design parameter, this
        // should be allowed.
        initialSettingValue = settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM);
        settings.loginWithResult(new AuthenticationResult(
                "Test Developer", "3", RoleEnum.DEVELOPER));
        assertDoesNotThrow(() ->
                settings.setPlayerMaxHealth(
                        DifficultyEnum.MEDIUM,
                        settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM) + 10));
        // This should have written to file.
        assertEquals(initialSettingValue + 10, settings.getPlayerMaxHealth(DifficultyEnum.MEDIUM));
    }

    /**
     * Developers, designers and players all have permission to toggle telemetry.
     * All users should be able to do this
     *
     * @throws AuthenticationException if an invoking user is not authenticated.
     */
    @Test
    @DisplayName("Settings - Any user may toggle their own telemetry")
    void setTelemetryEnabled_anyUserMayChange() throws AuthenticationException {
        // Authenticate a user with the role of Player.
        // No exception should be thrown when they try and toggle their telemetry, this
        // should be allowed.
        settings.loginWithResult(new AuthenticationResult(
                "Test Player", "1", RoleEnum.PLAYER));
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(false));
        // This should have been written to file.
        // Check its current state, and then toggle back to true and check again to confirm.
        assertFalse(settings.isTelemetryEnabled());
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(true));
        assertTrue(settings.isTelemetryEnabled());
        // Authenticate a user with the role of Designer.
        // No exception should be thrown when they try and toggle their telemetry, this
        // should be allowed.
        settings.loginWithResult(new AuthenticationResult(
                "Test Designer", "2", RoleEnum.DESIGNER));
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(false));
        assertFalse(settings.isTelemetryEnabled());
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(true));
        assertTrue(settings.isTelemetryEnabled());
        // Authenticate a user with the role of Developer.
        // No exception should be thrown when they try and toggle their telemetry, this
        // should be allowed.
        settings.loginWithResult(new AuthenticationResult(
                "Test Developer", "3", RoleEnum.DEVELOPER));
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(false));
        assertFalse(settings.isTelemetryEnabled());
        assertDoesNotThrow(() ->
                settings.setTelemetryEnabled(true));
        assertTrue(settings.isTelemetryEnabled());
    }

    /**
     * Resets the settings and login file paths to the filepath used for production.
     */
    @AfterEach
    void cleanUp() {
        settings.resetSettingsDestinationFile();
        settings.resetLoginsDestinationFile();
    }
}
