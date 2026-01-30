package WizardQuest;

/**
 * Provides a single global access point to the telemetry listener for notifying
 * it.
 *
 * NOTE: Some of the ConcreteEvent classes have not yet been created, this is because
 * they are not required for Sprint 1 (e.g., BossEncounterStartEvent)
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
        public TelemetryListener() {}

        @Override
        public void onSessionStart(SessionStartEvent e) {}

        @Override
        public void onNormalEncounterStart(NormalEncounterStartEvent e) {}

        @Override
        public void onNormalEncounterComplete(NormalEncounterCompleteEvent e) {}

        @Override
        public void onNormalEncounterFail(NormalEncounterFailEvent e) {}

        @Override
        public void onBossEncounterStart(BossEncounterStartEvent e) {}

        @Override
        public void onBossEncounterComplete(BossEncounterCompleteEvent e) {}

        @Override
        public void onBossEncounterFail(BossEncounterFailEvent e) {}

        @Override
        public void onGainCoin(GainCoinEvent e) {}

        @Override
        public void onBuyUpgrade(BuyUpgradeEvent e) {}

        @Override
        public void onEndSession(EndSessionEvent e) {}

        @Override
        public void onSettingsChange(SettingsChangeEvent e) {}

        @Override
        public void onKillEnemy(KillEnemyEvent e) {}
    }
}
