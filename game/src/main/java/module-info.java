module wizard.quest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires jdk.httpserver;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.sql;
    opens wizardquest.ui to javafx.fxml;
    opens wizardquest.telemetry;
    opens wizardquest.settings to com.fasterxml.jackson.databind;
    opens wizardquest.entity to com.fasterxml.jackson.databind;

    exports wizardquest.ui;
    exports wizardquest.auth;
}