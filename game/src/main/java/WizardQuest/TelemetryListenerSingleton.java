package WizardQuest;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides a single global access point to the telemetry listener for notifying
 * it.
 */
public class TelemetryListenerSingleton {
    private static TelemetryListenerInterface telemetryListener = new TelemetryListener();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File DESTINATION_FILE  = new File("events.json"); //change to actual filepath
    
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"source"})
    abstract static class ignoreSourceMixin {}
    
    static{
        mapper.addMixIn(java.util.EventObject.class, ignoreSourceMixin.class);
        mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
    }

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
         * Called by all event listeners to save to json file
         * 
         * @param e the event to be recorded to the JSON database.
         */
        private void saveEvent(Object e){
            try {
                if (!SettingsSingleton.getInstance().isTelemetryEnabled()) {
                    return; 
                }
            }
            catch(AuthenticationException ex){
                System.err.println("No user authenticated: " + ex.getMessage());
            }
            
            
            try{
                mapper.writeValue(DESTINATION_FILE, e);
            } catch (IOException ex){
                System.err.println("Error writing to JSON: " + ex.getMessage());
            }
        }

        /**
         * Called when a user logs in or enables telemetry.
         * 
         * @param e the SessionStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onSessionStart(SessionStartEvent e){
            saveEvent(e);
        }

        /**
         * Called when a normal encounter starts.
         * 
         * @param e the NormalEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterStart(NormalEncounterStartEvent e){
            saveEvent(e);
        }

        /**
         * Called when a normal encounter is completed.
         * 
         * @param e the NormalEncounterCompleteEvent to be recorded to the JSON
         *          database.
         */
        @Override
        public void onNormalEncounterComplete(NormalEncounterCompleteEvent e){
            saveEvent(e);
        }

        /**
         * Called when a normal encounter is failed as the player has died and ran out
         * of lives.
         * 
         * @param e the NormalEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterFail(NormalEncounterFailEvent e){
            saveEvent(e);
        }

        /**
         * Called when a boss encounter is started.
         * 
         * @param e the BossEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterStart(BossEncounterStartEvent e){
            saveEvent(e);
        }

        /**
         * Called when a boss encounter is completed.
         * 
         * @param e the BossEncounterCompleteEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterComplete(BossEncounterCompleteEvent e){
            saveEvent(e);
        }

        /**
         * Called when a boss encounter is failed as the player has died and has no
         * lives remaining.
         * 
         * @param e the BossEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterFail(BossEncounterFailEvent e){
            saveEvent(e);
        }

        /**
         * Called when the player gains coins.
         * 
         * @param e the GainCoinEvent to be recorded to the JSON database.
         */
        @Override
        public void onGainCoin(GainCoinEvent e){
            saveEvent(e);
        }

        /**
         * Called when the player buys an upgrade.
         * 
         * @param e the BuyUpgradeEvent to be recorded to the JSON database.
         */
        @Override
        public void onBuyUpgrade(BuyUpgradeEvent e){
            saveEvent(e);
        }

        /**
         * Called when the user signs out or disables telemetry.
         * 
         * @param e the EndSessionEvent to be recorded to the JSON database.
         */
        @Override
        public void onEndSession(EndSessionEvent e){
            saveEvent(e);
        }

        /**
         * Called when a setting is changed by the user.
         * 
         * @param e the SettingsChangeEvent to be recorded to the JSON database.
         */
        @Override
        public void onSettingsChange(SettingsChangeEvent e){
            saveEvent(e);
        }

        /**
         * Called when an enemy is killed.
         * 
         * @param e the KillEnemyEvent to be recorded to the JSON database.
         */
        @Override
        public void onKillEnemy(KillEnemyEvent e){
            saveEvent(e);
        }
    }  
}