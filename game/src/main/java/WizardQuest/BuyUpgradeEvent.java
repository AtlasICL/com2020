package WizardQuest;

public class BuyUpgradeEvent extends TelemetryEvent{
    private final String difficulty;
    private final int stageNumber;
    private final int coinsSpent;
    /**
     * Constructor for BuyUpgradeEvent.
     * 
     * @param source      the object that constructed the telemetry event.
     * @param userID      the ID of the user who is playing the game when the
     *                    event is
     *                    constructed.
     * @param sessionID   the ID of the session the user is currently playing. See
     *                    TelemetryListenerInterface for information about
     *                    sessions.
     * @param timeStamp   the time the event was constructed in the format
     *                    yyyy/mm/dd/hh/mm/ss
     * @param difficulty  the difficulty used for the players session.
     * @param stageNumber the stage player has completed.
     * @param coinsSpent  the number of coins spent by the player on an upgrade.
     */
    //SUBJECT TO CHANGE
    public BuyUpgradeEvent(Object source, int userID, int sessionID, 
        String timestamp, String difficulty, int stageNumber, int coinsSpent){
        super(source, userID, sessionID, timestamp, "BuyUpgrade"); 
        this.difficulty = difficulty;
        this.stageNumber = stageNumber;
        this.coinsSpent = coinsSpent;    
    }
    /**
     * Gets the stored difficulty.
     * 
     * @return session difficulty.
     */
    public String GetDifficulty(){
        return this.difficulty;
    }
    /**
     * Gets the stored stage number.
     * 
     * @return current stage number.
     */
    public int GetStageNumber(){
        return this.stageNumber;
    }
    /**
     * Gets the stored number of coins spent.
     * 
     * @return coins spent on an upgrade.
     */
    public int getCoinsSpent(){
        return this.coinsSpent;
    }
}
