package WizardQuest;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        private int currentSessionID = -1;
        private int currentUserID = -1;
        private static final ObjectMapper mapper = new ObjectMapper();
        private static final File DESTINATION_FILE  = new File("events.json"); //change to actual filepath
        
        @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"source"})
        abstract static class ignoreSourceMixin {}
        
        static{
            mapper.addMixIn(java.util.EventObject.class, ignoreSourceMixin.class);
            mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        }
        public TelemetryListener() {
        }

        private void isCorrectSession(TelemetryEvent e) throws SessionValidationException{
            if(currentSessionID != e.getSessionID() && !e.getTelemetryName().equals("SessionStart")){
                throw new SessionValidationException("SessionID of event " + e.getTelemetryName() + 
                                                    " " + e.getSessionID() + " not equal to current sessionID of "
                                                     + currentSessionID);
            }
            else if(e.getTelemetryName().equals("SessionStart") && currentSessionID != -1){
                throw new SessionValidationException("SessionStart for session " + e.getSessionID() + 
                                                    " occurs before EndSession of " + currentSessionID);
            }
        }

        private void isCorrectUser(TelemetryEvent e) throws UserValidationException{
            if(currentUserID != e.getUserID()){
                throw new UserValidationException("UserID of event " + e.getTelemetryName() + 
                                                    " " + e.getUserID() + " not equal to current sessionID of "
                                                     + currentUserID);
            }
        }

        private void isCorrectTimeStamp(TelemetryEvent e) throws TimestampValidationException{
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");
                LocalDateTime eventTime = LocalDateTime.parse(e.getTimestamp(), formatter);
                if(eventTime.isAfter(LocalDateTime.now())){
                    throw new TimestampValidationException("Time stamp of event " + e.getTelemetryName() + 
                                                            " " + e.getTimestamp() + " is in the future");
                }
            } catch (java.time.format.DateTimeParseException ex) {
                throw new TimestampValidationException("Time stamp of event " + e.getTelemetryName() + 
                                                        " " + e.getTimestamp() + " is of invalid format");
            }
        }

        /**
         * Called by all event listeners to save to json file
         * 
         * @param e the event to be recorded to the JSON database.
         */
        private void saveEvent(TelemetryEvent e){
            // try {
            //     if (!SettingsSingleton.getSettingsSingleton().isTelemetryEnabled()) {
            //         return; 
            //     }
            // }
            // catch(AuthenticationException ex){
            //     System.err.println("No user authenticated: " + ex.getMessage());
            // }
            
            try{
                String jsonLine = mapper.writeValueAsString(e);
                if(DESTINATION_FILE.length() == 0){
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(DESTINATION_FILE))){
                        writer.write("[\n" + jsonLine + "\n]");
                    }
                } else{
                    try (RandomAccessFile raf = new RandomAccessFile(DESTINATION_FILE, "rw")){
                        raf.seek(raf.length()-1);
                        String jsonToAppend = ",\n" + jsonLine + "\n]";
                        raf.write(jsonToAppend.getBytes());
                    }
                }
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
    public static void main(String[] args){
        TelemetryListenerInterface listener = getTelemetryListener();
        Object mockSource = new Object();
        SessionStartEvent testStartEvent = new SessionStartEvent(
            mockSource, 123, 1, 
            "2026/02/02/14/30/00", Difficulty.NORMAL
        );
        listener.onSessionStart(testStartEvent);
        EndSessionEvent testEndEvent = new EndSessionEvent(mockSource, 123, 1, "2026/02/02/14/30/01");
        listener.onEndSession(testEndEvent);
    }
}