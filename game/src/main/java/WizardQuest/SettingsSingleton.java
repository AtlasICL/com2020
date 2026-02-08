package WizardQuest;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Provides singleton access to settings and user properties
 */
public class SettingsSingleton {
    private static SettingsInterface settings = new Settings();

    private SettingsSingleton() {}

    public static SettingsInterface getInstance() {
        return settings;
    }

    private static class Settings implements SettingsInterface {
        private boolean telemetryEnabled;
        private int userID;
        private Role userRole;
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

        private static final File SETTINGS_FILE = new File("settings_file.json");
        private static final ObjectMapper jsonMapper = new ObjectMapper();


        /**
         * Reads in settings from user database and populates the game settings.
         */
        public Settings() {
            loadDefaults();
        }

        /**
         * Loads in default configs for settings.
         */
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

        /**
         * Load the settings of a given player from the settings file (.json).
         * If the user settings file does not exist, cannot be read, or does not
         * contain the necessary settings values, the function will keep default
         * values instead as a fallback.
         * If the user settings file does not have an entry for the given userID, 
         * a profile will be created in the file for the user, initially
         * populated with the default values.
         * NOTE: this function requires that the settings file exists before
         * it is called.
         */
        private void loadSettingsFromJson(int userID) {
            if (!SETTINGS_FILE.exists()) {
                loadDefaults();
                return;
            }

            try {
                JsonNode allUserSettings = jsonMapper.readTree(SETTINGS_FILE);
                JsonNode userSettings = allUserSettings.get(String.valueOf(userID));

                // Case that no personalised settings exist for give user yet.
                // Therefore we create one, and initialise it with defaults.
                if (userSettings == null) {
                    ObjectNode allUsers = (ObjectNode) allUserSettings;
                    ObjectNode newProfileObjectNode = jsonMapper.createObjectNode();

                    newProfileObjectNode.set("furthestLevel", createIntNode(maxStageReached));
                    newProfileObjectNode.set("playerMaxHealth", createIntNode(playerMaxHealth));
                    newProfileObjectNode.set("shopItemCount", createIntNode(shopItemCount));
                    newProfileObjectNode.set("enemyDamageMultiplier", createFloatNode(enemyDamageMultiplier));
                    newProfileObjectNode.set("enemyHealthMultiplier", createFloatNode(enemyMaxHealthMultiplier));
                    newProfileObjectNode.set("startingLives", createIntNode(startingLives));
                    newProfileObjectNode.set("maxMagic", createIntNode(maxMagic));
                    newProfileObjectNode.set("magicGenerationRate", createIntNode(magicRegenRate));
                    newProfileObjectNode.set("upgradePriceMultiplier", createFloatNode(upgradePriceMultiplier));
                    
                    newProfileObjectNode.put("telemetryEnabled", true);
                    newProfileObjectNode.put("role", (userRole != null ? userRole : Role.PLAYER).toString());

                    allUsers.set(String.valueOf(userID), newProfileObjectNode);
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, newProfileObjectNode);;
                    return;
                }

                loadIntNode(userSettings, "furthestLevel", maxStageReached);
                loadIntNode(userSettings, "playerMaxHealth", playerMaxHealth);
                loadIntNode(userSettings, "shopItemCount", shopItemCount);
                loadFloatNode(userSettings, "enemyDamageMultiplier", enemyDamageMultiplier);
                loadFloatNode(userSettings, "enemyHealthMultiplier", enemyMaxHealthMultiplier);
                loadIntNode(userSettings, "startingLives", startingLives);
                loadIntNode(userSettings, "maxMagic", maxMagic);
                loadIntNode(userSettings, "magicGenerationRate", magicRegenRate);
                loadFloatNode(userSettings, "upgradePriceMultiplier", upgradePriceMultiplier);

                telemetryEnabled = userSettings.get("telemetryEnabled").asBoolean();
                userRole = Role.valueOf(userSettings.get("role").asText());
            } catch (IOException e) {
                System.out.println("ERROR! Error reading settings from settings file." + e.toString());
            }
        }

        /**
         * Helper function for creating a json object to represent an **int** 
         * settings field.
         */
        private ObjectNode createIntNode(EnumMap<Difficulty, Integer> settingsMap) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            for (Difficulty difficulty : Difficulty.values()) {
                newNode.put(difficulty.toString(), settingsMap.get(difficulty));
            }
            return newNode;
        }

        /**
         * Helper function for creating a json object to represent a **float** 
         * settings field.
         */
        private ObjectNode createFloatNode(EnumMap<Difficulty, Float> settingsMap) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            for (Difficulty difficulty : Difficulty.values()) {
                newNode.put(difficulty.toString(), settingsMap.get(difficulty));
            }
            return newNode;
        }

        /**
         * Helper function which creates a json object to represent the 
         * telemetryEnabled setting for a user.
         */
        private ObjectNode createTelemetryNode(boolean val) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            newNode.put("telemetryEnabled", val);
            return newNode;
        }

        /**
         * Helper function which creates a json object to represent the 
         * role setting for a user.
         */
        private ObjectNode createRoleNode(Role role) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            newNode.put("role", role.toString());
            return newNode;
        }

        /**
         * Helper function for reading in / loading the json settings for an **int**
         * settings field.
         */
        private void loadIntNode(JsonNode userSettings, String field, EnumMap<Difficulty, Integer> settingsMap) {
            JsonNode newNode = userSettings.get(field);
            for (Difficulty difficulty : Difficulty.values()) {
                JsonNode value = newNode.get(difficulty.toString());
                settingsMap.put(difficulty, value.asInt());
            }
        }

        /**
         * Helper function for reading in / loading the json settings for an **float**
         * settings field.
         */
        private void loadFloatNode(JsonNode userSettings, String field, EnumMap<Difficulty, Float> settingsMap) {
            JsonNode newNode = userSettings.get(field);
            for (Difficulty difficulty : Difficulty.values()) {
                JsonNode value = newNode.get(difficulty.toString());
                settingsMap.put(difficulty, (float)value.asDouble());
            }
        }

        /**
         * Helper function which saves the current profile to the settings file.
         */
        public void saveProfile() {
            try {
                ObjectNode allUsers = (ObjectNode) jsonMapper.readTree(SETTINGS_FILE);
                ObjectNode profileNode = jsonMapper.createObjectNode();

                profileNode.set("furthestLevel", createIntNode(maxStageReached));
                profileNode.set("playerMaxHealth", createIntNode(playerMaxHealth));
                profileNode.set("upgradePriceMultiplier", createFloatNode(upgradePriceMultiplier));
                profileNode.set("shopItemCount", createIntNode(shopItemCount));
                profileNode.set("enemyDamageMultiplier", createFloatNode(enemyDamageMultiplier));
                profileNode.set("enemyHealthMultiplier", createFloatNode(enemyMaxHealthMultiplier));
                profileNode.set("startingLives", createIntNode(startingLives));
                profileNode.set("maxMagic", createIntNode(maxMagic));
                profileNode.set("magicGenerationRate", createIntNode(magicRegenRate));

                profileNode.put("telemetryEnabled", telemetryEnabled);
                profileNode.put("role", (userRole != null ? userRole : Role.PLAYER).toString());

                allUsers.set(String.valueOf(userID), profileNode);
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, allUsers);
            } catch (IOException e) {
                System.out.println("ERROR! Error saving settings to settings file. " + e.toString());
            }
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
            saveProfile();
        }

        @Override
        public void setMaxStageReached(Difficulty difficulty, int maxStageReached) throws AuthenticationException {
            this.maxStageReached.put(difficulty, maxStageReached);
            saveProfile();
        }

        @Override
        public void setPlayerMaxHealth(Difficulty difficulty, int newplayerMaxHealth) {
            this.playerMaxHealth.put(difficulty, newplayerMaxHealth);
            saveProfile();
        }

        @Override
        public void setUpgradePriceMultiplier(Difficulty difficulty, float newUpgradePriceMultiplier) {
            this.upgradePriceMultiplier.put(difficulty, newUpgradePriceMultiplier);
            saveProfile();
        }

        @Override
        public void setEnemyDamageMultiplier(Difficulty difficulty, float newEnemyDamageMultiplier) {
            this.enemyDamageMultiplier.put(difficulty, newEnemyDamageMultiplier);
            saveProfile();
        }

        @Override
        public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float newEnemyMaxHealthMultiplier) {
            this.enemyMaxHealthMultiplier.put(difficulty, newEnemyMaxHealthMultiplier);
            saveProfile();
        }

        @Override
        public void setStartingLives(Difficulty difficulty, int newStartingLives) {
            this.startingLives.put(difficulty, newStartingLives);
            saveProfile();
        }

        @Override
        public void setMaxMagic(Difficulty difficulty, int newMaxMagic) {
            this.maxMagic.put(difficulty, newMaxMagic);
            saveProfile();
        }

        @Override
        public void setMagicRegenRate(Difficulty difficulty, int newMagicRegenRate) {
            this.magicRegenRate.put(difficulty, newMagicRegenRate);
            saveProfile();
        }

        @Override
        public void setShopItemCount(Difficulty difficulty, int newShopItemCount) {
            this.shopItemCount.put(difficulty, newShopItemCount);
            saveProfile();
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
