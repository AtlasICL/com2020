package WizardQuest;

public class SettingsChangeEvent extends TelemetryEvent {
    private final String setting;
    private final String settingValue;
    /**
     * Constructor for the settings change telemetry event. Produces a telemetry event storing
     * common data.
     *
     * @param setting           The setting being changed in this event.
     * @param settingValue      The value that the setting is set to in this event.
     */
    public SettingsChangeEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, String setting, String settingValue) {
        super(source, userID, sessionID, timeStamp, telemetryName);
        this.setting = setting;
        this.settingValue = settingValue;
    }

    public String getSetting() {return setting;}

    public String getSettingValue() {return settingValue;}
}
