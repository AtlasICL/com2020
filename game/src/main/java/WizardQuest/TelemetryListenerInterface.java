package WizardQuest;

/**
 * Interface for the telemetry listener. Provides methods to deal with telemetry
 * events, writing them to the JSON store of telemetry events. When a method is
 * called the listener should verify telemetry is enabled before writing to the
 * database.
 */
public interface TelemetryListenerInterface {
    /**
     * Called when a user logs in or enables telemetry. 
     * 
     * @param e the SessionStartEvent to be recorded to the JSON database.
     */
    public void onSessionStart(SessionStartEvent e);

    /**
     * Called when a normal encounter starts.
     * 
     * @param e the NormalEncounterStartEvent to be recorded to the JSON database.
     */
    public void onNormalEncounterStart(NormalEncounterStartEvent e);

    /**
     * Called when a normal encounter is completed.
     * 
     * @param e the NormalEncounterCompleteEvent to be recorded to the JSON
     *          database.
     */
    public void onNormalEncounterComplete(NormalEncounterCompleteEvent e);

    /**
     * Called when a normal encounter is failed as the player has died and ran out
     * of lives.
     * 
     * @param e the NormalEncounterFailEvent to be recorded to the JSON database.
     */
    public void onNormalEncounterFail(NormalEncounterFailEvent e);

    /**
     * Called when a normal encounter is retried as the player has died and has
     * lives remaining.
     * 
     * @param e the NormalEncounterRetryEvent to be recorded to the JSON database.
     */
    public void onNormalEncounterRetry(NormalEncounterRetryEvent e);

    /**
     * Called when a boss encounter is started.
     * 
     * @param e the BossEncounterStartEvent to be recorded to the JSON database.
     */
    public void onBossEncounterStart(BossEncounterStartEvent e);

    /**
     * Called when a boss encounter is completed.
     * 
     * @param e the BossEncounterCompleteEvent to be recorded to the JSON database.
     */
    public void onBossEncounterComplete(BossEncounterCompleteEvent e);

    /**
     * Called when a boss encounter is failed as the player has died and has no
     * lives remaining.
     * 
     * @param e the BossEncounterFailEvent to be recorded to the JSON database.
     */
    public void onBossEncounterFail(BossEncounterFailEvent e);

    /**
     * Called when a boss encounter is retried as the player has died and has lives
     * remaining.
     * 
     * @param e the BossEncounterRetryEvent to be recorded to the JSON database.
     */
    public void onBossEncounterRetry(BossEncounterRetryEvent e);

    /**
     * Called when the player gains coins.
     * 
     * @param e the GainCoinEvent to be recorded to the JSON database.
     */
    public void onGainCoin(GainCoinEvent e);

    /**
     * Called when the player buys an upgrade. 
     * 
     * @param e the BuyUpgradeEvent to be recorded to the JSON database.
     */
    public void onBuyUpgrade(BuyUpgradeEvent e);

    /**
     * Called when the user signs out or disables telemetry.
     * 
     * @param e the EndSessionEvent to be recorded to the JSON database.
     */
    public void onEndSession(EndSessionEvent e);

    /**
     * Called when a setting is changed by the user.
     * 
     * @param e the SettingsChangeEvent to be recorded to the JSON database.
     */
    public void onSettingsChange(SettingsChangeEvent e);

    /**
     * Called when an enemy is killed. 
     * 
     * @param e the KillEnemyEvent to be recorded to the JSON database.
     */
    public void onKillEnemy(KillEnemyEvent e);
}
