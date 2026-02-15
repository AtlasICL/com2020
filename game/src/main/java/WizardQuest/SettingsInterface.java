package WizardQuest;

import java.io.File;
import java.math.BigInteger;

/**
 * Interface for settings. Provides access to user settings and properties, as
 * well as the ability to authenticate and create users.
 */
public interface SettingsInterface {
    /**
     * Logs in the user using the result from the Authenticator (Python OAuth).
     * Sets the current user's ID, name, and role from the authentication result,
     * and loads their settings from the settings file.
     *
     * @param result the AuthenticationResult returned by Authenticator.login().
     * @throws AuthenticationException if the result is invalid.
     */
    public void loginWithResult(AuthenticationResult result) throws AuthenticationException;

    /**
     * Returns the role of the currently authenticated user or throws an exception
     * if no user is authenticated.
     * 
     * @throws AuthenticationException if no user is authenticated.
     * @return the user's role.
     */
    public RoleEnum getUserRole() throws AuthenticationException;

    /**
     * Sets the role of the specified user or throws an exception
     * if the authenticated user is not a developer.
     *
     * @param userID the user to have their role modified.
     * @param role the new role that they will hold.
     * @throws AuthenticationException if no user is authenticated,
     *                                 or the authenticated user calling the method is not of the Developer role.
     */
    public void setUserRole(String userID, RoleEnum role) throws AuthenticationException;

    /**
     * Returns the session id of the currently authenticated user and session, or
     * throws an exception if the user is unauthenticated or no session is
     * established. A session is only established for users who have telemetry
     * enabled.
     * 
     * @throws AuthenticationException if no user is authenticated or session
     *                                 established.
     * @return the current session's ID
     */
    public int getSessionID() throws AuthenticationException;

    /**
     * Hashes the username of the currently authenticated user and returns it (as
     * their ID).
     * 
     * @throws AuthenticationException if no user is authenticated.
     * @return the user's ID.
     */
    public String getUserID() throws AuthenticationException;

    /**
     * Returns whether the user has telemetry enabled.
     * 
     * @throws AuthenticationException if no user is authenticated.
     * @return whether the user has telemetry enabled or not.
     */
    public boolean isTelemetryEnabled() throws AuthenticationException;

    /**
     * Gets the user's personal best for the furthest stage they've reached of the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @throws AuthenticationException if no user is authenticated.
     * @return the furthest stage the user has got on the specified difficulty.
     */
    public int getMaxStageReached(DifficultyEnum difficulty) throws AuthenticationException;

    /**
     * Gets the value of the player max health design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getPlayerMaxHealth(DifficultyEnum difficulty);

    /**
     * Gets the value of the enemy damage multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public float getEnemyDamageMultiplier(DifficultyEnum difficulty);

    /**
     * Gets the value of the enemy max health multiplier design parameter for the
     * specified difficulty.
     *
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public float getEnemyMaxHealthMultiplier(DifficultyEnum difficulty);

    /**
     * Gets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getStartingLives(DifficultyEnum difficulty);

    /**
     * Gets the value of the max magic design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getMaxMagic(DifficultyEnum difficulty);

    /**
     * Gets the value of the magic regeneration rate design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getMagicRegenRate(DifficultyEnum difficulty);

    /**
     * Gets the value of the shop item count design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getShopItemCount(DifficultyEnum difficulty);

    /**
     * Attempts to set the user's preference for whether they have telemetry
     * enabled, writing this to their settings in the user database JSON. Will throw
     * an exception if no user is authenticated.
     * 
     * @param telemetryEnabled whether the user has telemetry enabled or not.
     *                         Setting this to false will send an EndSession event
     *                         and setting this to true will send a StartSession
     *                         event with a new session ID.
     * @throws AuthenticationException if the user cannot be authenticated.
     */
    public void setTelemetryEnabled(boolean telemetryEnabled) throws AuthenticationException;

    /**
     * Sets the value for the furthest stage the user has gotten to for th specified
     * difficulty.
     * 
     * @param difficulty      the difficulty the run was on.
     * @param maxStageReached the furthest stage the user reached.
     * @throws AuthenticationException if no user is authenticated.
     */
    public void setMaxStageReached(DifficultyEnum difficulty, int maxStageReached) throws AuthenticationException;

    /**
     * Sets the value of the player max health multiplier design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty               the difficulty it's being set for.
     * @param newPlayerMaxHealth the value it's being set to.
     */
    public void setPlayerMaxHealth(DifficultyEnum difficulty, int newPlayerMaxHealth) throws AuthenticationException;

    /**
     * Sets the value of the enemy damage multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty            the difficulty it's being set for.
     * @param newEnemyDamageMultiplier the value it's being set to.
     */
    public void setEnemyDamageMultiplier(DifficultyEnum difficulty, float newEnemyDamageMultiplier) throws AuthenticationException;

    /**
     * Sets the value of the enemy max health multiplier design parameter for the
     * specified difficulty.
     *
     * @param difficulty            the difficulty it's being set for.
     * @param newEnemyMaxHealthMultiplier the value it's being set to.
     */
    public void setEnemyMaxHealthMultiplier(DifficultyEnum difficulty, float newEnemyMaxHealthMultiplier) throws AuthenticationException;

    /**
     * Sets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty    the difficulty it's being set for.
     * @param newStartingLives the value it's being set to.
     */
    public void setStartingLives(DifficultyEnum difficulty, int newStartingLives) throws AuthenticationException;

    /**
     * Sets the value of the max magic multiplier design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty         the difficulty it's being set for.
     * @param newMaxMagic the value it's being set to.
     */
    public void setMaxMagic(DifficultyEnum difficulty, int newMaxMagic) throws AuthenticationException;

    /**
     * Sets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty     the difficulty it's being set for.
     * @param newMagicRegenRate the value it's being set to.
     */
    public void setMagicRegenRate(DifficultyEnum difficulty, int newMagicRegenRate) throws AuthenticationException;

    /**
     * Sets the value of the shop item count design parameter for the
     * specified difficulty.
     * 
     * @param difficulty    the difficulty it's being set for.
     * @param newShopItemCount the value it's being set to.
     */
    public void setShopItemCount(DifficultyEnum difficulty, int newShopItemCount) throws AuthenticationException;

    /**
     * Allows JUnit tests to write to a temporary JSON file rather than logins_file.json,
     * mitigating any risk of test data corrupting the real JSON file.
     *
     * @param file the temporary JSON file to be written to.
     */
    public void setLoginsDestinationFile(File file);

    /**
     * Resets the filepath to the real JSON file, logins_file.json, after running a JUnit test.
     */
    public void resetLoginsDestinationFile();

    /**
     * Allows JUnit tests to write to a temporary JSON file rather than settings_file.json,
     * mitigating any risk of test data corrupting the real JSON file.
     *
     * @param file the temporary JSON file to be written to.
     */
    public void setSettingsDestinationFile(File file);

    /**
     * Resets the filepath to the real JSON file, settings_file.json, after running a JUnit test.
     */
    public void resetSettingsDestinationFile();
}
