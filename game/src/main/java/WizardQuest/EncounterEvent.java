package WizardQuest;

public abstract class EncounterEvent extends TelemetryEvent {
    private final int stageNumber;
    private final EncounterType encounterName;
    private final Difficulty difficulty;

    /**
     * Constructor for the encounter telemetry event. Produces a telemetry event storing
     * common data.
     * 
     * @param source        the object that constructed the telemetry event.
     * @param userID        the ID of the user who is playing the game when the event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about sessions.
     * @param timeStamp     the time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss.
     * @param telemetryName name of the type of encounter event.
     * @param encounterName the name of the encounter a player is fighting.
     * @param stageNumber   the current stage player is attempting.
     * @param difficulty    the difficulty used for the players session.
     */
    public EncounterEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
            Difficulty difficulty, int stageNumber) {
        super(source, userID, sessionID, timeStamp, telemetryName);
        this.encounterName = encounterName;
        this.difficulty = difficulty;
        this.stageNumber = stageNumber;
    }

    /**
     * Gets the name for the encounter this event was generated on.
     * @return the encounter's name.
     */
    public EncounterType getEncounterName(){
        return this.encounterName;
    }

    /**
     * Gets the stage number the player is on when this event was created.
     * @return the stage number stored in the event.
     */
    public int getStageNumber() {
        return this.stageNumber;
    }

    /**
     * Gets the difficulty setting for the run that generated this event.
     * @return the difficulty setting.
     */
    public Difficulty getDifficulty(){
        return this.difficulty;
    }
}
