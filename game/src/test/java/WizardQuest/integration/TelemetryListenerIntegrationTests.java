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
     * A JSON object, once its fields have been populated and validated, is sent to the Python telemetry application.
     */
    @Test
    @DisplayName("TelemetryListener - Sent JSON object is received by telemetry application")
    void onNormalEncounterStart_sentJSONObjectIsReceivedByTelemetryApplication() {}

}
