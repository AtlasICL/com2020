package WizardQuest;

/**
 * Provides singleton access to settings and user properties
 */
public class SettingsSingleton {
    private static SettingsInterface settings = new Settings();

    private SettingsSingleton() {}

    public static SettingsInterface getSettings(){
        return settings;
    }
    
    public static SettingsInterface getSettingsSingleton(){
        return settings;
    }

    private static class Settings implements SettingsInterface {
        private boolean telemetryEnabled;
        private String username;
        private Role userRole;

        // ID for the current session, generated when a user enables telemetry or log in
        // with telemetry enabled.
        private int sessionID;

        private int hardMaxStageReached;
        private int mediumMaxStageReached;
        private int easyMaxStageReached;

        // DESIGN PARAMETERS: Integer values multiplied by these parameters to be rounded.

        private int hardPlayerMaxHealth;
        private int mediumPlayerMaxHealth;
        private int easyPlayerMaxHealth;

        private float hardUpgradePriceMultiplier;
        private float mediumUpgradePriceMultiplier;
        private float easyUpgradePriceMultiplier;

        private float hardEnemyDamageMultiplier;
        private float mediumEnemyDamageMultiplier;
        private float easyEnemyDamageMultiplier;

        private float hardEnemyMaxHealthMultiplier;
        private float mediumEnemyMaxHealthMultiplier;
        private float easyEnemyMaxHealthMultiplier;

        private int hardStartingLives = 1;
        private int mediumStartingLives = 3;
        private int easyStartingLives = 5;

        private int hardMaxMagic;
        private int mediumMaxMagic;
        private int easyMaxMagic;

        private int hardMagicRegenRate;
        private int mediumMagicRegenRate;
        private int easyMagicRegenRate;

        private int hardShopItemCount;
        private int mediumShopItemCount;
        private int easyShopItemCount;

        /**
         * Reads in settings from user database and populates the game settings.
         */
        public Settings() {}

        @Override
        public void createNewUser(String username, String password, Role role) throws AuthenticationException {}

        @Override
        public void authenticateUser(String username, String password) throws AuthenticationException {}

        @Override
        public Role getUserRole() throws AuthenticationException {
            return userRole;
        }

        @Override
        public void setUserRole(String username, Role role) throws AuthenticationException {}

        @Override
        public String getUsername() throws AuthenticationException {
            return username;
        }

        @Override
        public int getSessionID() throws AuthenticationException {
            return sessionID;
        }

        @Override
        public int getUserID() throws AuthenticationException {
            return sessionID;
        }

        @Override
        public boolean isTelemetryEnabled() throws AuthenticationException {
            return telemetryEnabled;
        }

        @Override
        public int getMaxStageReached(Difficulty difficulty) throws AuthenticationException {
            return -1; // PLACEHOLDER
        }

        @Override
        public int getPlayerMaxHealth(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public float getUpgradePriceMultiplier(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public float getEnemyDamageMultiplier(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public float getEnemyMaxHealthMultiplier(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public int getStartingLives(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public int getMaxMagic(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public int getMagicRegenRate(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public int getShopItemCount(Difficulty difficulty) {
            return -1; // PLACEHOLDER
        }

        @Override
        public void setTelemetryEnabled(boolean telemetryEnabled) throws AuthenticationException {
            this.telemetryEnabled = telemetryEnabled;
        }

        @Override
        public void setMaxStageReached(Difficulty difficulty, int maxStageReached) throws AuthenticationException {}

        @Override
        public void setPlayerMaxHealth(Difficulty difficulty, int newPlayerMaxHealth) {}

        @Override
        public void setUpgradePriceMultiplier(Difficulty difficulty, float newUpgradePriceMultiplier) {}

        @Override
        public void setEnemyDamageMultiplier(Difficulty difficulty, float newEnemyDamageMultiplier) {}

        @Override
        public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float newEnemyMaxHealthMultiplier) {}

        @Override
        public void setStartingLives(Difficulty difficulty, int newStartingLives) {
            switch (difficulty) { 
                case EASY: 
                    easyStartingLives = newStartingLives;
                case MEDIUM:
                    mediumStartingLives = newStartingLives;
                case HARD:
                    hardStartingLives = newStartingLives;
            }
        }

        @Override
        public void setMaxMagic(Difficulty difficulty, int newMaxMagic) {
            return;
        }

        @Override
        public void setMagicRegenRate(Difficulty difficulty, int newMagicRegenRate) {
            return;
        }

        @Override
        public void setShopItemCount(Difficulty difficulty, int newShopItemCount) {
            return;
        }
    }
}
