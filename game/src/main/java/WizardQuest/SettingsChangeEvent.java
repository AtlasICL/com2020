package WizardQuest;

import java.time.Instant;

public class SettingsChangeEvent extends TelemetryEvent {
    private final SettingsEnum setting;
    private final String setting_value;

    /**
     * Constructor for the settings change telemetry event. Produces a telemetry event storing
     * common data.
     *
     * @param userID        The userID for the currently authenticated user.
     * @param source        The object that constructed the telemetry event.
     * @param timeStamp     The time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss,
     * @param setting       The setting being changed in this event.
     * @param settingValue  The value that the setting is set to in this event.
     */
    public SettingsChangeEvent(String userID, Instant timeStamp, SettingsEnum setting, String settingValue) {
        super(userID, timeStamp, "SettingsChange");
        this.setting = setting;
        this.setting_value = settingValue;
    }

    /**
     * Gets the name of the setting being changed
     * 
     * @return the setting's name
     */
    public SettingsEnum getSetting() {
        return this.setting;
    }

    /**
     * Gets the new value of the setting being changed
     * 
     * @return
     */
    public String getSetting_value() {
        return this.setting_value;
    }
}
