package WizardQuest;

public abstract class EncounterFailEvent extends EncounterEvent {
    private final int livesLeft;
    /**
     * Constructor for the encounter fail telemetry event. Produces a telemetry event storing
     * common data.
     * @param livesLeft     A player's remaining lives upon failing an encounter.
     */
    public EncounterFailEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
                                  int stageNumber, Difficulty difficulty, int livesLeft) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, stageNumber, difficulty);
        this.livesLeft = livesLeft;
    }

    /**
     * Gets the remaining lives of the player when this event was created.
     * @return the player's total lives left stored in the event.
     */
    public int getLivesLeft() {return livesLeft;}
}
