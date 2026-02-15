package WizardQuest;

import java.time.Instant;

public class SettingsChangeEvent {
    private final SettingsEnum setting;
    private final String settingValue;
    private final Instant timeStamp;
    private final String telemetryName;
    /**
     * Constructor for the settings change telemetry event. Produces a telemetry event storing
     * common data.
     *
     * @param source        The object that constructed the telemetry event.
     * @param timeStamp     The time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss,
     * @param setting       The setting being changed in this event.
     * @param settingValue  The value that the setting is set to in this event.
     */
    public SettingsChangeEvent(Instant timeStamp, SettingsEnum setting, String settingValue) {
        this.timeStamp = timeStamp;
        this.setting = setting;
        this.settingValue = settingValue;
        this.telemetryName = "SettingsChange";
    }

    /**
     * Gets the stored timestamp.
     * 
     * @return the timestamp.
     */
    public Instant getTimestamp() {
        return this.timeStamp;
    }

    /**
     * Gets the name of the telemetry event according to the telemetry
     * specification.
     * 
     * @return the event's name.
     */
    public String getTelemetryName() {
        return this.telemetryName;
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
    public String getSettingValue() {
        return this.settingValue;
    }
}
