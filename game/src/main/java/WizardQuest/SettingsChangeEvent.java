package WizardQuest;

public class SettingsChangeEvent extends TelemetryEvent{
    private final Setting setting;
    private final String settingValue;
    /**
     * Constructor for SettingsChangeEvent.
     * 
     * @param source        the object that constructed the telemetry event.
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss.
     * @param setting       setting being changed.
     * @param settingValue  new setting value being set.
     */
    public SettingsChangeEvent(Object source, int userID, int sessionID, String timeStamp, Setting setting, String settingValue) {
        super(source, userID, sessionID, timeStamp, "SettingsChange");
        this.setting = setting;
        this.settingValue = settingValue;
    }
    /**
     * Gets setting.
     * 
     * @return setting being changed in event.
     */
    public Setting getSetting(){
        return this.setting;
    }
    /**
     * Gets setting value.
     * 
     * @return setting value being changed to.
     */
    public String getSettingValue(){
        return this.settingValue;
    }

}
