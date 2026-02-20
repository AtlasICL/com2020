package wizardquest.telemetry;

import java.io.File;

/**
 * Interface for the telemetry listener. Provides methods to deal with telemetry
 * events, writing them to the JSON store of telemetry events. When a method is
 * called the listener should verify telemetry is enabled before writing to the
 * database.
 *
 * NOTE: Some of the ConcreteEvent classes have not yet been created, this is
 * because
 * they are not required for Sprint 1 (e.g., BossEncounterStartEvent)
 */
public interface TelemetryListenerInterface {
    /**
     * Called when a user logs in or enables telemetry.
     * 
     * @param e the SessionStartEvent to be recorded to the JSON database.
     */
    public void onStartSession(StartSessionEvent e);

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

    /**
     * Allows JUnit tests to write to a temporary JSON file rather than events.json,
     * mitigating any risk of test data corrupting the real JSON file.
     *
     * @param file the temporary JSON file to be written to.
     */
    public void setDestinationFile(File file);

    /**
     * Resets the filepath to the real JSON file, events.json, after running a JUnit
     * test.
     */
    public void resetDestinationFile();
}
