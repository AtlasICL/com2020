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
        private int sessionID; // WE ARE NOT YET SETTING THIS - TODO LATER

        private EnumMap<Difficulty, Integer> maxStageReached;
        private EnumMap<Difficulty, Integer> playerMaxHealth;
        private EnumMap<Difficulty, Float> enemyDamageMultiplier;
        private EnumMap<Difficulty, Float> enemyMaxHealthMultiplier;
        private EnumMap<Difficulty, Integer> startingLives;
        private EnumMap<Difficulty, Integer> maxMagic;
        private EnumMap<Difficulty, Integer> magicRegenRate;
        private EnumMap<Difficulty, Integer> shopItemCount;

        private static final File SETTINGS_FILE = new File("settings_file.json");
        private static final ObjectMapper jsonMapper = new ObjectMapper();

        // TEMPORARY
        private static final File LOGINS_FILE = new File("logins_file.json");

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
         * Design parameters (global, affecting all users) loaded from
         * "designParameters" key. 
         * User-specific data is loaded from "users", with the key being 
         * their userID. Reminder that an example settings.json file is provided.
         * If a profile for a user does not yet exist, it will be created with
         * default values.
         */
        private void loadSettingsFromJson(int userID) {
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
                JsonNode userSettings = usersNode.get(String.valueOf(userID));

                // If the user did not have a section in the file, create it.
                if (userSettings == null) {
                    ObjectNode newProfile = jsonMapper.createObjectNode();
                    newProfile.put("telemetryEnabled", true);
                    newProfile.put("role", (userRole != null ? userRole : Role.PLAYER).toString());
                    newProfile.set("furthestLevel", createIntNode(maxStageReached));

                    usersNode.set(String.valueOf(userID), newProfile);
                    root.set("users", usersNode);
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValue(SETTINGS_FILE, root);
                    return;
                }

                // Load user's (user-specific / non-global) data.
                telemetryEnabled = userSettings.get("telemetryEnabled").asBoolean();
                userRole = Role.valueOf(userSettings.get("role").asText());
                loadIntNode(userSettings, "furthestLevel", maxStageReached);
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
                profileNode.put("role", (userRole != null ? userRole : Role.PLAYER).toString());
                profileNode.set("furthestLevel", createIntNode(maxStageReached));

                usersNode.set(String.valueOf(userID), profileNode);
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
        public void createNewUser(String username, String password, Role role) throws AuthenticationException {
            try {
                ObjectNode allLogins = (ObjectNode) jsonMapper.readTree(LOGINS_FILE);

                ObjectNode userNode = jsonMapper.createObjectNode();
                userNode.put("password", password);
                userNode.put("role", role.getJSONName());

                allLogins.set(username, userNode);
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(LOGINS_FILE, allLogins);
            } catch (IOException e) {
                System.out.println("Failed to save new user to logins file." + e.toString());
            }
        }

        @Override
        public void authenticateUser(String username, String password) throws AuthenticationException {
            try {
                JsonNode allLogins = jsonMapper.readTree(LOGINS_FILE);
                JsonNode userNode = allLogins.get(username);

                if (userNode == null) {
                    throw new AuthenticationException("User does not exist.");
                }

                String storedPassword = userNode.get("password").asText();
                if (!storedPassword.equals(password)) {
                    throw new AuthenticationException("Incorrect password");
                }

                userID = username.hashCode();

                // TODO: I think there is a bug here, mapping from the json
                // to the Role enum. 
                // Potentially make a helper function to convert from string to 
                // Role? Idk
                userRole = Role.valueOf(userNode.get("role").asText());

                loadSettingsFromJson(userID);
            } catch (IOException e) {
                System.out.println("Error reading login file" + e);
            }
        }

        /**
         * Verifies the currently authenticated user is a developer (role).
         */
        private boolean currentUserIsDeveloper() {
            return userRole == Role.DEVELOPER;
        }

        /**
         * Verifies the currently authenticated user is a designer (role).
         */
        private boolean currentUserIsDesigner() {
            return userRole == Role.DESIGNER;
        }

        @Override
        public Role getUserRole() throws AuthenticationException {
            return userRole;
        }

        @Override
        public void setUserRole(String username, Role role) throws AuthenticationException {
            // TODO
        }

        @Override
        public int getMaxStageReached(Difficulty difficulty) throws AuthenticationException {
            return maxStageReached.get(difficulty);
        }

        @Override
        public int getPlayerMaxHealth(Difficulty difficulty) {
            return playerMaxHealth.get(difficulty);
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
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.telemetryEnabled = telemetryEnabled;
            saveProfile();
        }

        @Override
        public void setMaxStageReached(Difficulty difficulty, int maxStageReached) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.maxStageReached.put(difficulty, maxStageReached);
            saveProfile();
        }

        @Override
        public void setPlayerMaxHealth(Difficulty difficulty, int newplayerMaxHealth) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.playerMaxHealth.put(difficulty, newplayerMaxHealth);
            saveDesignParameters();
        }

        @Override
        public void setEnemyDamageMultiplier(Difficulty difficulty, float newEnemyDamageMultiplier) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.enemyDamageMultiplier.put(difficulty, newEnemyDamageMultiplier);
            saveDesignParameters();
        }

        @Override
        public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float newEnemyMaxHealthMultiplier) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.enemyMaxHealthMultiplier.put(difficulty, newEnemyMaxHealthMultiplier);
            saveDesignParameters();
        }

        @Override
        public void setStartingLives(Difficulty difficulty, int newStartingLives) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.startingLives.put(difficulty, newStartingLives);
            saveDesignParameters();
        }

        @Override
        public void setMaxMagic(Difficulty difficulty, int newMaxMagic) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.maxMagic.put(difficulty, newMaxMagic);
            saveDesignParameters();
        }

        @Override
        public void setMagicRegenRate(Difficulty difficulty, int newMagicRegenRate) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
                throw new AuthenticationException();
            }
            this.magicRegenRate.put(difficulty, newMagicRegenRate);
            saveDesignParameters();
        }

        @Override
        public void setShopItemCount(Difficulty difficulty, int newShopItemCount) throws AuthenticationException {
            if (!currentUserIsDesigner() || !currentUserIsDeveloper()) {
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
        public int getUserID() throws AuthenticationException {
            return this.userID;
        }

        @Override
        public boolean isTelemetryEnabled() throws AuthenticationException {
            return this.telemetryEnabled;
        }
    }
}
