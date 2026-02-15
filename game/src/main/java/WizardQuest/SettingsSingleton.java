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
        private String userID;
        private RoleEnum userRole;
        private int sessionID; // WE ARE NOT YET SETTING THIS - TODO LATER

        private EnumMap<DifficultyEnum, Integer> maxStageReached;
        private EnumMap<DifficultyEnum, Integer> playerMaxHealth;
        private EnumMap<DifficultyEnum, Float> enemyDamageMultiplier;
        private EnumMap<DifficultyEnum, Float> enemyMaxHealthMultiplier;
        private EnumMap<DifficultyEnum, Integer> startingLives;
        private EnumMap<DifficultyEnum, Integer> maxMagic;
        private EnumMap<DifficultyEnum, Integer> magicRegenRate;
        private EnumMap<DifficultyEnum, Integer> shopItemCount;

        private static File SETTINGS_FILE = new File("settings_file.json");
        private static final ObjectMapper jsonMapper = new ObjectMapper();

        private static File LOGINS_FILE = new File("../telemetry/logins_file.json");

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
            maxStageReached = new EnumMap<>(DifficultyEnum.class);
            playerMaxHealth = new EnumMap<>(DifficultyEnum.class);
            enemyDamageMultiplier = new EnumMap<>(DifficultyEnum.class);
            enemyMaxHealthMultiplier = new EnumMap<>(DifficultyEnum.class);
            startingLives = new EnumMap<>(DifficultyEnum.class);
            maxMagic = new EnumMap<>(DifficultyEnum.class);
            magicRegenRate = new EnumMap<>(DifficultyEnum.class);
            shopItemCount = new EnumMap<>(DifficultyEnum.class);

            maxStageReached.put(DifficultyEnum.EASY, 0);
            maxStageReached.put(DifficultyEnum.MEDIUM, 0);
            maxStageReached.put(DifficultyEnum.HARD, 0);

            playerMaxHealth.put(DifficultyEnum.EASY, 200);
            playerMaxHealth.put(DifficultyEnum.MEDIUM, 100);
            playerMaxHealth.put(DifficultyEnum.HARD, 50);

            enemyDamageMultiplier.put(DifficultyEnum.EASY, 0.5f);
            enemyDamageMultiplier.put(DifficultyEnum.MEDIUM, 1.0f);
            enemyDamageMultiplier.put(DifficultyEnum.HARD, 2.0f);

            enemyMaxHealthMultiplier.put(DifficultyEnum.EASY, 1.0f);
            enemyMaxHealthMultiplier.put(DifficultyEnum.MEDIUM, 1.5f);
            enemyMaxHealthMultiplier.put(DifficultyEnum.HARD, 2.0f);

            startingLives.put(DifficultyEnum.EASY, 5);
            startingLives.put(DifficultyEnum.MEDIUM, 3);
            startingLives.put(DifficultyEnum.HARD, 1);

            maxMagic.put(DifficultyEnum.EASY, 200);
            maxMagic.put(DifficultyEnum.MEDIUM, 100);
            maxMagic.put(DifficultyEnum.HARD, 50);

            magicRegenRate.put(DifficultyEnum.EASY, 5);
            magicRegenRate.put(DifficultyEnum.MEDIUM, 3);
            magicRegenRate.put(DifficultyEnum.HARD, 1);

            shopItemCount.put(DifficultyEnum.EASY, 3);
            shopItemCount.put(DifficultyEnum.MEDIUM, 2);
            shopItemCount.put(DifficultyEnum.HARD, 1);
        }

        /**
         * Load the settings of a given player from the settings file (.json).
         * Design parameters (global, affecting all users) loaded from
         * "designParameters" key. 
         * User-specific data is loaded from "users", with the key being 
         * their userID. Reminder that an example settings.json file is provided.
         * If a profile for a user does not yet exist, it will be created with
         * default values.
         */
        private void loadSettingsFromJson(String userID) {
            if (!SETTINGS_FILE.exists()) {
                loadDefaults();
                return;
            }

            try {
                ObjectNode root = (ObjectNode) jsonMapper.readTree(SETTINGS_FILE);

                JsonNode designParams = root.get("designParameters");
                if (designParams == null) {
                    root.set("designParameters", createDesignParametersNode());
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, root);
                } else {
                    loadIntNode(designParams, "playerMaxHealth", playerMaxHealth);
                    loadIntNode(designParams, "shopItemCount", shopItemCount);
                    loadFloatNode(designParams, "enemyDamageMultiplier", enemyDamageMultiplier);
                    loadFloatNode(designParams, "enemyHealthMultiplier", enemyMaxHealthMultiplier);
                    loadIntNode(designParams, "startingLives", startingLives);
                    loadIntNode(designParams, "maxMagic", maxMagic);
                    loadIntNode(designParams, "magicGenerationRate", magicRegenRate);
                }

                // Load per-user data.
                ObjectNode usersNode = root.has("users") ? (ObjectNode) root.get("users") : jsonMapper.createObjectNode();
                JsonNode userSettings = usersNode.get(userID);

                // If the user did not have a section in the file, create it.
                if (userSettings == null) {
                    ObjectNode newProfile = jsonMapper.createObjectNode();
                    newProfile.put("telemetryEnabled", true);
                    newProfile.set("furthestLevel", createIntNode(maxStageReached));

                    usersNode.set(userID, newProfile);
                    root.set("users", usersNode);
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, root);
                    return;
                }

                // Load user's (user-specific / non-global) data.
                telemetryEnabled = userSettings.get("telemetryEnabled").asBoolean();
                loadIntNode(userSettings, "furthestLevel", maxStageReached);
            } catch (IOException e) {
                System.out.println("ERROR! Error reading settings from settings file." + e.toString());
            }
        }

        /**
         * Helper function for creating a json object to represent an **int** 
         * settings field.
         */
        private ObjectNode createIntNode(EnumMap<DifficultyEnum, Integer> settingsMap) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            for (DifficultyEnum difficulty : DifficultyEnum.values()) {
                newNode.put(difficulty.toString(), settingsMap.get(difficulty));
            }
            return newNode;
        }

        /**
         * Helper function for creating a json object to represent a **float** 
         * settings field.
         */
        private ObjectNode createFloatNode(EnumMap<DifficultyEnum, Float> settingsMap) {
            ObjectNode newNode = jsonMapper.createObjectNode();
            for (DifficultyEnum difficulty : DifficultyEnum.values()) {
                newNode.put(difficulty.toString(), settingsMap.get(difficulty));
            }
            return newNode;
        }

        /**
         * Creates a JSON node containing all shared design parameters.
         */
        private ObjectNode createDesignParametersNode() {
            ObjectNode node = jsonMapper.createObjectNode();
            node.set("playerMaxHealth", createIntNode(playerMaxHealth));
            node.set("shopItemCount", createIntNode(shopItemCount));
            node.set("enemyDamageMultiplier", createFloatNode(enemyDamageMultiplier));
            node.set("enemyHealthMultiplier", createFloatNode(enemyMaxHealthMultiplier));
            node.set("startingLives", createIntNode(startingLives));
            node.set("maxMagic", createIntNode(maxMagic));
            node.set("magicGenerationRate", createIntNode(magicRegenRate));
            return node;
        }

        /**
         * Helper function for reading in / loading the json settings for an **int**
         * settings field.
         */
        private void loadIntNode(JsonNode userSettings, String field, EnumMap<DifficultyEnum, Integer> settingsMap) {
            JsonNode newNode = userSettings.get(field);
            for (DifficultyEnum difficulty : DifficultyEnum.values()) {
                JsonNode value = newNode.get(difficulty.toString());
                settingsMap.put(difficulty, value.asInt());
            }
        }

        /**
         * Helper function for reading in / loading the json settings for an **float**
         * settings field.
         */
        private void loadFloatNode(JsonNode userSettings, String field, EnumMap<DifficultyEnum, Float> settingsMap) {
            JsonNode newNode = userSettings.get(field);
            for (DifficultyEnum difficulty : DifficultyEnum.values()) {
                JsonNode value = newNode.get(difficulty.toString());
                settingsMap.put(difficulty, (float)value.asDouble());
            }
        }

        /**
         * Saves USER-SPECIFIC info:
         * - telemetryEnabled
         * - role
         * - furthestLevel (per difficulty)
         */
        public void saveProfile() {
            try {
                ObjectNode root = (ObjectNode) jsonMapper.readTree(SETTINGS_FILE);
                ObjectNode usersNode = root.has("users") ? (ObjectNode) root.get("users") : jsonMapper.createObjectNode();

                ObjectNode profileNode = jsonMapper.createObjectNode();
                profileNode.put("telemetryEnabled", telemetryEnabled);
                profileNode.set("furthestLevel", createIntNode(maxStageReached));

                usersNode.set(userID, profileNode);
                root.set("users", usersNode);
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, root);
            } catch (IOException e) {
                System.out.println("ERROR! Error saving settings to settings file. " + e.toString());
            }
        }

        /**
         * Saves (global) design parameters to the settings file.
         */
        private void saveDesignParameters() {
            try {
                ObjectNode root = (ObjectNode) jsonMapper.readTree(SETTINGS_FILE);
                root.set("designParameters", createDesignParametersNode());
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, root);
            } catch (IOException e) {
                System.out.println("ERROR! Error saving design parameters to settings file. " + e.toString());
            }
        }

        @Override
        public void loginWithResult(AuthenticationResult result) throws AuthenticationException {
            if (result == null) {
                throw new AuthenticationException("Authentication result is null.");
            }

            userID = result.userID();
            userRole = result.role();

            loadSettingsFromJson(userID);
        }

        /**
         * Verifies the currently authenticated user is a developer (role).
         */
        private boolean currentUserIsDeveloper() {
            return userRole == RoleEnum.DEVELOPER;
        }

        /**
         * Verifies the currently authenticated user is a designer (role).
         */
        private boolean currentUserIsDesigner() {
            return userRole == RoleEnum.DESIGNER;
        }

        @Override
        public RoleEnum getUserRole() throws AuthenticationException {
            return userRole;
        }

        @Override
        public void setUserRole(String userID, RoleEnum role) throws AuthenticationException {
            if (!currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }

            try {
                ObjectNode allLogins = (ObjectNode) jsonMapper.readTree(LOGINS_FILE);
                allLogins.put(userID, role.name().toLowerCase());
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(LOGINS_FILE, allLogins);
            } catch (IOException e) {
                System.out.println("ERROR! Error updating user role in logins file. " + e.toString());
            }

            // If user whose role was updated is the current user, then update
            // the value of role in memory also.
            if (userID.equals(this.userID)) {
                userRole = role;
            }
        }

        @Override
        public int getMaxStageReached(DifficultyEnum difficulty) throws AuthenticationException {
            return maxStageReached.get(difficulty);
        }

        @Override
        public int getPlayerMaxHealth(DifficultyEnum difficulty) {
            return playerMaxHealth.get(difficulty);
        }

        @Override
        public float getEnemyDamageMultiplier(DifficultyEnum difficulty) {
            return enemyDamageMultiplier.get(difficulty);
        }

        @Override
        public float getEnemyMaxHealthMultiplier(DifficultyEnum difficulty) {
            return enemyMaxHealthMultiplier.get(difficulty);
        }

        @Override
        public int getStartingLives(DifficultyEnum difficulty) {
            return startingLives.get(difficulty);
        }

        @Override
        public int getMaxMagic(DifficultyEnum difficulty) {
            return maxMagic.get(difficulty);
        }

        @Override
        public int getMagicRegenRate(DifficultyEnum difficulty) {
            return magicRegenRate.get(difficulty);
        }

        @Override
        public int getShopItemCount(DifficultyEnum difficulty) {
            return shopItemCount.get(difficulty);
        }

        @Override
        public void setTelemetryEnabled(boolean telemetryEnabled) {
            this.telemetryEnabled = telemetryEnabled;
            saveProfile();
        }

        @Override
        public void setMaxStageReached(DifficultyEnum difficulty, int maxStageReached) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.maxStageReached.put(difficulty, maxStageReached);
            saveProfile();
        }

        @Override
        public void setPlayerMaxHealth(DifficultyEnum difficulty, int newplayerMaxHealth) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.playerMaxHealth.put(difficulty, newplayerMaxHealth);
            saveDesignParameters();
        }

        @Override
        public void setEnemyDamageMultiplier(DifficultyEnum difficulty, float newEnemyDamageMultiplier) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.enemyDamageMultiplier.put(difficulty, newEnemyDamageMultiplier);
            saveDesignParameters();
        }

        @Override
        public void setEnemyMaxHealthMultiplier(DifficultyEnum difficulty, float newEnemyMaxHealthMultiplier) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.enemyMaxHealthMultiplier.put(difficulty, newEnemyMaxHealthMultiplier);
            saveDesignParameters();
        }

        @Override
        public void setStartingLives(DifficultyEnum difficulty, int newStartingLives) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.startingLives.put(difficulty, newStartingLives);
            saveDesignParameters();
        }

        @Override
        public void setMaxMagic(DifficultyEnum difficulty, int newMaxMagic) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.maxMagic.put(difficulty, newMaxMagic);
            saveDesignParameters();
        }

        @Override
        public void setMagicRegenRate(DifficultyEnum difficulty, int newMagicRegenRate) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.magicRegenRate.put(difficulty, newMagicRegenRate);
            saveDesignParameters();
        }

        @Override
        public void setShopItemCount(DifficultyEnum difficulty, int newShopItemCount) throws AuthenticationException {
            if (!(currentUserIsDesigner() || currentUserIsDeveloper())) {
                throw new AuthenticationException();
            }
            this.shopItemCount.put(difficulty, newShopItemCount);
            saveDesignParameters();
        }

        @Override
        public int getSessionID() throws AuthenticationException {
            return this.sessionID;
        }

        @Override
        public String getUserID() throws AuthenticationException {
            return this.userID;
        }

        @Override
        public boolean isTelemetryEnabled() throws AuthenticationException {
            return this.telemetryEnabled;
        }

        @Override
        public void setLoginsDestinationFile(File file) {
            LOGINS_FILE = file;
        }

        @Override
        public void resetLoginsDestinationFile() {
            LOGINS_FILE = new File("../telemetry/logins_file.json");
        }

        @Override
        public void setSettingsDestinationFile(File file) {
            SETTINGS_FILE = file;
        }

        @Override
        public void resetSettingsDestinationFile() {
            SETTINGS_FILE = new File("settings_file.json");
        }
    }
}
