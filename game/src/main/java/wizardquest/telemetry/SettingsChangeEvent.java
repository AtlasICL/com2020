package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.settings.SettingsEnum;

/**
 * SettingsChangeEvent - event for when a player changes setting in menu
 */
public class SettingsChangeEvent extends TelemetryEvent {
    private final SettingsEnum setting;
    private final String settingValue;
    private final String settingChangeJustification;

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
        this.settingValue = settingValue;
        this.settingChangeJustification = justification;
    }

    /**
     * Gets the name of the setting being changed
     * 
     * @return The setting's name.
     */
    public SettingsEnum getSetting() {
        return this.setting;
    }

    /**
     * Gets the new value of the setting being changed
     * 
     * @return The new value of the setting.
     */
    public String getSettingValue() {
        return this.settingValue;
    }

    /**
     * Gets the justification 
     * 
     * @return The justification for the settings change.
     */
    public String getSettingsChangeJustification() {
        return this.settingChangeJustification;
    }

}
