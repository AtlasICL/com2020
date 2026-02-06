package WizardQuest;

import java.util.EnumMap;

/**
 * Provides singleton access to settings and user properties
 */
public class SettingsSingleton {
    private static SettingsInterface settings = new Settings();

    public static SettingsInterface getInstance() {
        return settings;
    }

    private static class Settings implements SettingsInterface {
        private boolean telemetryEnabled;
        private int userID;
        private Role userRole;

        // ID for the current session, generated when a user enables telemetry or log in
        // with telemetry enabled.
        private int sessionID;

        private EnumMap<Difficulty, Integer> maxStageReached;
        private EnumMap<Difficulty, Integer> playerMaxHealth;
        private EnumMap<Difficulty, Float> upgradePriceMultiplier;
        private EnumMap<Difficulty, Float> enemyDamageMultiplier;
        private EnumMap<Difficulty, Float> enemyMaxHealthMultiplier;
        private EnumMap<Difficulty, Integer> startingLives;
        private EnumMap<Difficulty, Integer> maxMagic;
        private EnumMap<Difficulty, Integer> magicRegenRate;
        private EnumMap<Difficulty, Integer> shopItemCount;

        /**
         * Reads in settings from user database and populates the game settings.
         */
        public Settings() {
            loadDefaults();
        }

        private void loadDefaults() {
            maxStageReached = new EnumMap<>(Difficulty.class);
            playerMaxHealth = new EnumMap<>(Difficulty.class);
            upgradePriceMultiplier = new EnumMap<>(Difficulty.class);
            enemyDamageMultiplier = new EnumMap<>(Difficulty.class);
            enemyMaxHealthMultiplier = new EnumMap<>(Difficulty.class);
            startingLives = new EnumMap<>(Difficulty.class);
            maxMagic = new EnumMap<>(Difficulty.class);
            magicRegenRate = new EnumMap<>(Difficulty.class);
            shopItemCount = new EnumMap<>(Difficulty.class);

            maxStageReached.put(Difficulty.EASY, 0);
            maxStageReached.put(Difficulty.MEDIUM, 0);
            maxStageReached.put(Difficulty.HARD, 0);

            playerMaxHealth.put(Difficulty.EASY, 200);
            playerMaxHealth.put(Difficulty.MEDIUM, 100);
            playerMaxHealth.put(Difficulty.HARD, 50);

            upgradePriceMultiplier.put(Difficulty.EASY, 1.0f);
            upgradePriceMultiplier.put(Difficulty.MEDIUM, 1.5f);
            upgradePriceMultiplier.put(Difficulty.HARD, 2.0f);

            enemyDamageMultiplier.put(Difficulty.EASY, 0.5f);
            enemyDamageMultiplier.put(Difficulty.MEDIUM, 1.0f);
            enemyDamageMultiplier.put(Difficulty.HARD, 2.0f);

            enemyMaxHealthMultiplier.put(Difficulty.EASY, 1.0f);
            enemyMaxHealthMultiplier.put(Difficulty.MEDIUM, 1.5f);
            enemyMaxHealthMultiplier.put(Difficulty.HARD, 2.0f);

            startingLives.put(Difficulty.EASY, 5);
            startingLives.put(Difficulty.MEDIUM, 3);
            startingLives.put(Difficulty.HARD, 1);

            maxMagic.put(Difficulty.EASY, 200);
            maxMagic.put(Difficulty.MEDIUM, 100);
            maxMagic.put(Difficulty.HARD, 50);

            magicRegenRate.put(Difficulty.EASY, 5);
            magicRegenRate.put(Difficulty.MEDIUM, 3);
            magicRegenRate.put(Difficulty.HARD, 1);

            shopItemCount.put(Difficulty.EASY, 3);
            shopItemCount.put(Difficulty.MEDIUM, 2);
            shopItemCount.put(Difficulty.HARD, 1);
        }

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
        public int getMaxStageReached(Difficulty difficulty) throws AuthenticationException {

            return maxStageReached.get(difficulty);
        }

        @Override
        public int getPlayerMaxHealth(Difficulty difficulty) {
            return playerMaxHealth.get(difficulty);
        }

        @Override
        public float getUpgradePriceMultiplier(Difficulty difficulty) {
            return upgradePriceMultiplier.get(difficulty);
        }

        @Override
        public float getEnemyDamageMultiplier(Difficulty difficulty) {
            return enemyDamageMultiplier.get(difficulty);
        }

        @Override
        public float getEnemyMaxHealthMultiplier(Difficulty difficulty) {
            return enemyMaxHealthMultiplier.get(difficulty);
        }

        @Override
        public int getStartingLives(Difficulty difficulty) {
            return startingLives.get(difficulty);
        }

        @Override
        public int getMaxMagic(Difficulty difficulty) {
            return maxMagic.get(difficulty);
        }

        @Override
        public int getMagicRegenRate(Difficulty difficulty) {
            return magicRegenRate.get(difficulty);
        }

        @Override
        public int getShopItemCount(Difficulty difficulty) {
            return shopItemCount.get(difficulty);
        }

        @Override
        public void setTelemetryEnabled(boolean telemetryEnabled) throws AuthenticationException {
            this.telemetryEnabled = telemetryEnabled;
        }

        @Override
        public void setMaxStageReached(Difficulty difficulty, int maxStageReached) throws AuthenticationException {
            this.maxStageReached.put(difficulty, maxStageReached);
        }

        @Override
        public void setPlayerMaxHealth(Difficulty difficulty, int newPlayerMaxHealth) {
            this.playerMaxHealth.put(difficulty, newPlayerMaxHealth);
        }

        @Override
        public void setUpgradePriceMultiplier(Difficulty difficulty, float newUpgradePriceMultiplier) {
            this.upgradePriceMultiplier.put(difficulty, newUpgradePriceMultiplier);
        }

        @Override
        public void setEnemyDamageMultiplier(Difficulty difficulty, float newEnemyDamageMultiplier) {
            this.enemyDamageMultiplier.put(difficulty, newEnemyDamageMultiplier);
        }

        @Override
        public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float newEnemyMaxHealthMultiplier) {
            this.enemyMaxHealthMultiplier.put(difficulty, newEnemyMaxHealthMultiplier);
        }

        @Override
        public void setStartingLives(Difficulty difficulty, int newStartingLives) {
            this.startingLives.put(difficulty, newStartingLives);
        }

        @Override
        public void setMaxMagic(Difficulty difficulty, int newMaxMagic) {
            this.maxMagic.put(difficulty, newMaxMagic);
        }

        @Override
        public void setMagicRegenRate(Difficulty difficulty, int newMagicRegenRate) {
            this.magicRegenRate.put(difficulty, newMagicRegenRate);
        }

        @Override
        public void setShopItemCount(Difficulty difficulty, int newShopItemCount) {
            this.shopItemCount.put(difficulty, newShopItemCount);
        }

        @Override
        public int getSessionID() throws AuthenticationException {
            return this.sessionID;
        }

        @Override
        public int getUserID() throws AuthenticationException {
            return this.userID;
        }

        @Override
        public boolean isTelemetryEnabled() throws AuthenticationException {
            return this.telemetryEnabled;
        }
    }
}
