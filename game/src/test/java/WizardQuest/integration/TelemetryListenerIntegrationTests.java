package WizardQuest.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions.*;

public class TelemetryListenerIntegrationTests {

    // NormalEncounterStartEvent will be used to test the behaviour of the abstract class TelemetryEvent,
    // of which NormalEncounterStartEvent extends.

    /**
     * A JSON object, once its fields have been populated and validated, is sent to the Python telemetry application.
     */
    @Test
    @DisplayName("TelemetryListener - Sent JSON object is received by telemetry application")
    void onNormalEncounterStart_sentJSONObjectIsReceivedByTelemetryApplication() {}

}
