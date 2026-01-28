package WizardQuest;

/**
 * Provides a single global access point to the telemetry listener for notifying
 * it.
 */
public class TelemetryListenerSingleton {
    private static TelemetryListenerInterface telemetryListener = new TelemetryListener();

    private TelemetryListenerSingleton() {
    }

    /**
     * Returns a reference to the telemetry listener. 
     * @return a reference to the telemetry listener. 
     */
    public static TelemetryListenerInterface getTelemetryListener() {
        return telemetryListener;
    }

    /**
     * Internal telemetry listener implementation
     */
    private static class TelemetryListener implements TelemetryListenerInterface {
        public TelemetryListener() {
        }

        /**
         * Called when a user logs in or enables telemetry.
         * 
         * @param e the SessionStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onSessionStart(SessionStartEvent e);

        /**
         * Called when a normal encounter starts.
         * 
         * @param e the NormalEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterStart(NormalEncounterStartEvent e);

        /**
         * Called when a normal encounter is completed.
         * 
         * @param e the NormalEncounterCompleteEvent to be recorded to the JSON
         *          database.
         */
        @Override
        public void onNormalEncounterComplete(NormalEncounterCompleteEvent e);

        /**
         * Called when a normal encounter is failed as the player has died and ran out
         * of lives.
         * 
         * @param e the NormalEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterFail(NormalEncounterFailEvent e);

        /**
         * Called when a normal encounter is retried as the player has died and has
         * lives remaining.
         * 
         * @param e the NormalEncounterRetryEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterRetry(NormalEncounterRetryEvent e);

        /**
         * Called when a boss encounter is started.
         * 
         * @param e the BossEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterStart(BossEncounterStartEvent e);

        /**
         * Called when a boss encounter is completed.
         * 
         * @param e the BossEncounterCompleteEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterComplete(BossEncounterCompleteEvent e);

        /**
         * Called when a boss encounter is failed as the player has died and has no
         * lives remaining.
         * 
         * @param e the BossEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterFail(BossEncounterFailEvent e);

        /**
         * Called when a boss encounter is retried as the player has died and has lives
         * remaining.
         * 
         * @param e the BossEncounterRetryEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterRetry(BossEncounterRetryEvent e);

        /**
         * Called when the player gains coins.
         * 
         * @param e the GainCoinEvent to be recorded to the JSON database.
         */
        @Override
        public void onGainCoin(GainCoinEvent e);

        /**
         * Called when the player buys an upgrade.
         * 
         * @param e the BuyUpgradeEvent to be recorded to the JSON database.
         */
        @Override
        public void onBuyUpgrade(BuyUpgradeEvent e);

        /**
         * Called when the user signs out or disables telemetry.
         * 
         * @param e the EndSessionEvent to be recorded to the JSON database.
         */
        @Override
        public void onEndSession(EndSessionEvent e);

        /**
         * Called when a setting is changed by the user.
         * 
         * @param e the SettingsChangeEvent to be recorded to the JSON database.
         */
        @Override
        public void onSettingsChange(SettingsChangeEvent e);

        /**
         * Called when an enemy is killed.
         * 
         * @param e the KillEnemyEvent to be recorded to the JSON database.
         */
        @Override
        public void onKillEnemy(KillEnemyEvent e);
    }
}
