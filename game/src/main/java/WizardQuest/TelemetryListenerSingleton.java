package WizardQuest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides a single global access point to the telemetry listener for notifying
 * it.
 */
public class TelemetryListenerSingleton {
    private static final TelemetryListenerInterface telemetryListener = new TelemetryListener();
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
        private LocalDateTime mostRecentTimeStamp;
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");
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
        /**
         * Checks if session related to an event is valid. Checks for session ending when no session is established,
         * checks for events sessionID not corresponding with the current session being ran 
         * and new session starts occurring before the current session has ended.
         * 
         * @param e event being checked.
         * @throws SessionValidationException exception used to specify invalid sessions.
         */
        private void isCorrectSession(TelemetryEvent e) throws SessionValidationException{
            if(e.getTelemetryName().equals("EndSession") && currentSessionID == -1){
                throw new SessionValidationException("SessionEnd for session " + e.getSessionID() + 
                                                    " occurs before it's StartSession");
            }
            else if(currentSessionID != e.getSessionID() && !e.getTelemetryName().equals("SessionStart")){
                throw new SessionValidationException("SessionID of event " + e.getTelemetryName() + 
                                                    " " + e.getSessionID() + " not equal to current sessionID of "
                                                     + currentSessionID);
            }
            else if(e.getTelemetryName().equals("SessionStart") && currentSessionID != -1){
                throw new SessionValidationException("SessionStart for session " + e.getSessionID() + 
                                                    " occurs before EndSession of " + currentSessionID);
            }
        }
        /**
         * Checks if user related to an event is valid for the current session established.
         * 
         * @param e event being checked.
         * @throws UserValidationException exception used to specify invalid users
         */
        private void isCorrectUser(TelemetryEvent e) throws UserValidationException{
            if(currentUserID != e.getUserID()){
                throw new UserValidationException("UserID of event " + e.getTelemetryName() + 
                                                    " " + e.getUserID() + " not equal to current sessionID of "
                                                     + currentUserID);
            }
        }

        /**
         * Checks if timestamp related to an event is valid.
         * 
         * @param e
         * @throws TimestampValidationException
         */
        private void isCorrectTimeStamp(TelemetryEvent e) throws TimestampValidationException{
            try{
                LocalDateTime eventTime = LocalDateTime.parse(e.getTimestamp(), formatter);
                if(eventTime.isAfter(LocalDateTime.now())){
                    throw new TimestampValidationException("Time stamp of event " + e.getTelemetryName() + 
                                                            " " + e.getTimestamp() + " is in the future");
                }
                else if(mostRecentTimeStamp != null && eventTime.isBefore(mostRecentTimeStamp)){
                    throw new TimestampValidationException("Time stamp of event " + e.getTelemetryName() + 
                                                            " " + e.getTimestamp() + " is not current");
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
            this.mostRecentTimeStamp = LocalDateTime.parse(e.getTimestamp(), formatter);
            try {
                if (!SettingsSingleton.getSettingsSingleton().isTelemetryEnabled()) {
                    return; 
                }
            }
            catch(AuthenticationException ex){
                System.err.println("No user authenticated: " + ex.getMessage());
            }
            
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
        public void onStartSession(StartSessionEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                currentSessionID = e.getSessionID();
                currentUserID = e.getUserID();
                saveEvent(e);
            } catch (TimestampValidationException | SessionValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when a normal encounter starts.
         * 
         * @param e the NormalEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterStart(NormalEncounterStartEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
            
        }

        /**
         * Called when a normal encounter is completed.
         * 
         * @param e the NormalEncounterCompleteEvent to be recorded to the JSON
         *          database.
         */
        @Override
        public void onNormalEncounterComplete(NormalEncounterCompleteEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when a normal encounter is failed as the player has died and ran out
         * of lives.
         * 
         * @param e the NormalEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onNormalEncounterFail(NormalEncounterFailEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when a boss encounter is started.
         * 
         * @param e the BossEncounterStartEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterStart(BossEncounterStartEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when a boss encounter is completed.
         * 
         * @param e the BossEncounterCompleteEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterComplete(BossEncounterCompleteEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when a boss encounter is failed as the player has died and has no
         * lives remaining.
         * 
         * @param e the BossEncounterFailEvent to be recorded to the JSON database.
         */
        @Override
        public void onBossEncounterFail(BossEncounterFailEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when the player gains coins.
         * 
         * @param e the GainCoinEvent to be recorded to the JSON database.
         */
        @Override
        public void onGainCoin(GainCoinEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when the player buys an upgrade.
         * 
         * @param e the BuyUpgradeEvent to be recorded to the JSON database.
         */
        @Override
        public void onBuyUpgrade(BuyUpgradeEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when the user signs out or disables telemetry.
         * 
         * @param e the EndSessionEvent to be recorded to the JSON database.
         */
        @Override
        public void onEndSession(EndSessionEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                saveEvent(e);
                this.currentSessionID = -1;
                this.currentUserID = -1;
            } catch (TimestampValidationException | SessionValidationException ex) {
                System.err.println(ex.getMessage());
            }
            
        }

        /**
         * Called when a setting is changed by the user.
         * 
         * @param e the SettingsChangeEvent to be recorded to the JSON database.
         */
        @Override
        public void onSettingsChange(SettingsChangeEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }

        /**
         * Called when an enemy is killed.
         * 
         * @param e the KillEnemyEvent to be recorded to the JSON database.
         */
        @Override
        public void onKillEnemy(KillEnemyEvent e){
            try {
                isCorrectSession(e);
                isCorrectTimeStamp(e);
                isCorrectUser(e);
                saveEvent(e);
            } catch (SessionValidationException | TimestampValidationException | UserValidationException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}