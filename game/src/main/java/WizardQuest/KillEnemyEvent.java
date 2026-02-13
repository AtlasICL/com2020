package WizardQuest;

public class KillEnemyEvent extends EncounterEvent{
    private final EntityEnum enemyType;
    /**
     * Constructor for KillEnemyEvent.
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
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the stage player has completed.
     * @param enemyType     type of enemy encounter.
     */
    public KillEnemyEvent(Object source, int userID, int sessionID, 
            String timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, 
            EntityEnum enemyType){
                super(source, userID, sessionID, timeStamp, "KillEnemy", encounterName, difficulty, stageNumber);
                this.enemyType = enemyType;
    }
    /**
     * Gets stored enemy type killed.
     * 
     * @return enemy type killed.
     */
    public EntityEnum getEnemyType(){
        return this.enemyType;
    }
}
