package WizardQuest;

public abstract class EncounterCompleteEvent extends EncounterEvent {
    private final int playerHPRemaining;
    /**
     * Constructor for the encounter complete telemetry event. Produces a telemetry event storing
     * common data.
     * @param playerHPRemaining     A player's health points at the end of an encounter.
     */
    public EncounterCompleteEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
                               int stageNumber, Difficulty difficulty, int playerHPRemaining) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, stageNumber, difficulty);
        this.playerHPRemaining = playerHPRemaining;
    }

    /**
     * Gets the remaining health points of the player when this event was created.
     * @return the remaining player HP stored in the event.
     */
    public int getPlayerHPRemaining() {return playerHPRemaining;}
}
