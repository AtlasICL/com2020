package WizardQuest;

public class BuyUpgradeEvent extends EncounterEvent{
    private final UpgradeEnum upgradeBought;
    private final int coinsSpent;
    /**
     * Constructor for BuyUpgradeEvent.
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
     * @param upgradeBought the upgrade a player has purchased.
     * @param coinsSpent    the number of coins spent by the player on an upgrade.
     */
    public BuyUpgradeEvent(Object source, int userID, int sessionID, 
        String timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, UpgradeEnum upgradeBought, int coinsSpent){
        super(source, userID, sessionID, timeStamp, "BuyUpgrade", encounterName, difficulty, stageNumber);
        this.upgradeBought = upgradeBought; 
        this.coinsSpent = coinsSpent;    
    }
    /**
     * Gets the stored upgrade bought.
     * 
     * @return upgrade a player has purchased.
     */
    public UpgradeEnum getUpgradeBought() {
        return upgradeBought;
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
