module wizard.quest {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens wizardquest.ui to javafx.fxml;
    opens wizardquest.telemetry;
    opens wizardquest.settings to com.fasterxml.jackson.databind;
    opens wizardquest.entity to com.fasterxml.jackson.databind;

    exports wizardquest.ui;
}