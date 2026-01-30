package WizardQuest;

/**
 * Provides singleton access to settings and user properties
 */
public class SettingsSingleton {
    private SettingsInterface settings = new Settings();

    private SettingsSingleton() {}

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

        private int hardStartingLives;
        private int mediumStartingLives;
        private int easyStartingLives;

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
        public void setPlayerMaxHealth(Difficulty difficulty, int playerMaxHealth) {}

        @Override
        public void setUpgradePriceMultiplier(Difficulty difficulty, float upgradePriceMultiplier) {}

        @Override
        public void setEnemyDamageMultiplier(Difficulty difficulty, float enemyDamageMultiplier) {}

        @Override
        public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float enemyMaxHealthMultiplier) {}

        @Override
        public void setStartingLives(Difficulty difficulty, int startingLives) {}

        @Override
        public void setMaxMagic(Difficulty difficulty, int maxMagic) {}

        @Override
        public void setMagicRegenRate(Difficulty difficulty, int magicRegenRate) {}

        @Override
        public void setShopItemCount(Difficulty difficulty, int shopItemCount) {}
    }
}
