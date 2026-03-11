package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.settings.SettingsEnum;

/**
 * SettingsChangeEvent - event for when a player changes setting in menu
 */
public class SettingsChangeEvent extends TelemetryEvent {
    private final SettingsEnum setting;
    private final String setting_value;
    private final String setting_change_justification;

    /**
     * Constructor for the settings change telemetry event. Produces a telemetry
     * event storing
     * common data.
     *
     * @param userID        The userID for the currently authenticated user.
     * @param source        The object that constructed the telemetry event.
     * @param timeStamp     The timestamp of the event.
     * @param setting       The setting being changed in this event.
     * @param settingValue  The value that the setting is set to in this event.
     * @param justification The justification for the setting change. Allowed to be
     *                      empty.
     */
    public SettingsChangeEvent(String userID, Instant timeStamp, SettingsEnum setting, String settingValue,
            String justification) {
        super(userID, timeStamp, "SettingsChange");
        this.setting = setting;
        this.setting_value = settingValue;
        this.setting_change_justification = justification;
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
