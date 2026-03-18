module wizard.quest {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens wizardquest.ui to javafx.fxml;
    opens wizardquest.telemetry;

    exports wizardquest.ui;
}
