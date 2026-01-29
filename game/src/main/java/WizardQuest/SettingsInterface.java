package WizardQuest;

/**
 * Interface for settings. Provides access to user settings and properties, as
 * well as the ability to authenticate and create users.
 */
public interface SettingsInterface {
    /**
     * Attempts to create a new user with the given parameters. On success will add
     * this user to the user JSON database.
     * 
     * @param username the user's username. Must consists exclusively of
     *                 alphanumeric characters, underscores or hyphens.
     * @param password the user's password. It is hashed and salted before being
     *                 stored.
     * @param role     the role for the user.
     * @throws AuthenticationException if there is a user with the same username or
     *                                 the username is invalid.
     */
    public void createNewUser(String username, String password, Role role) throws AuthenticationException;

    /**
     * Attempts to authenticate the specified user, logging in as them on success.
     * 
     * @param username the user's username. Must consists exclusively of
     *                 alphanumeric characters, underscores or hyphens.
     * @param password the user's password. It is hashed and salted with the user's
     *                 salt before being looked up in the database.
     * @throws AuthenticationException if the user does not exist or their login
     *                                 credentials are invalid or incorrect.
     */
    public void authenticateUser(String username, String password) throws AuthenticationException;

    /**
     * Returns the role of the currently authenticated user or throws an exception
     * if no user is authenticated.
     * 
     * @throws AuthenticationException if no user is authenticated.
     * @return the user's role.
     */
    public Role getUserRole() throws AuthenticationException;

    /**
     * Returns the username of the currently authenticated user.
     * 
     * @throws AuthenticationException if no user is authenticated.
     * @return the user's username.
     */
    public String getUsername() throws AuthenticationException;

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
    public int getUserID() throws AuthenticationException;

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
    public int getMaxStageReached(Difficulty difficulty) throws AuthenticationException;

    /**
     * Gets the value of the enemy max health multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public float getEnemyMaxHealthMultiplier(Difficulty difficulty);

    /**
     * Gets the value of the player max health design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getPlayerMaxHealth(Difficulty difficulty);

    /**
     * Gets the value of the upgrade price multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public float getUpgradePriceMultiplier(Difficulty difficulty);

    /**
     * Gets the value of the enemy damage multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public float getEnemyDamageMultiplier(Difficulty difficulty);

    /**
     * Gets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getStartingLives(Difficulty difficulty);

    /**
     * Gets the value of the max magic design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getMaxMagic(Difficulty difficulty);

    /**
     * Gets the value of the magic regeneration rate design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getMagicRegenRate(Difficulty difficulty);

    /**
     * Gets the value of the shop item count design parameter for the
     * specified difficulty.
     * 
     * @param difficulty the difficulty being queried.
     * @return the value of the design parameter.
     */
    public int getShopItemCount(Difficulty difficulty);

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
    public void setMaxStageReached(Difficulty difficulty, int maxStageReached) throws AuthenticationException;

    /**
     * Sets the value of the enemy max health multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty               the difficulty it's being set for.
     * @param enemyMaxHealthMultiplier the value it's being set to.
     */
    public void setEnemyMaxHealthMultiplier(Difficulty difficulty, float enemyMaxHealthMultiplier);

    /**
     * Sets the value of the player max health multiplier design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty               the difficulty it's being set for.
     * @param playerMaxHealth the value it's being set to.
     */
    public void setPlayerMaxHealth(Difficulty difficulty, int playerMaxHealth);

    /**
     * Sets the value of the upgrade price multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty               the difficulty it's being set for.
     * @param upgradePriceMultiplier the value it's being set to.
     */
    public void setUpgradePriceMultiplier(Difficulty difficulty, float upgradePriceMultiplier);

    /**
     * Sets the value of the enemy damage multiplier design parameter for the
     * specified difficulty.
     * 
     * @param difficulty            the difficulty it's being set for.
     * @param enemyDamageMultiplier the value it's being set to.
     */
    public void setEnemyDamageMultiplier(Difficulty difficulty, float enemyDamageMultiplier);

    /**
     * Sets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty    the difficulty it's being set for.
     * @param startingLives the value it's being set to.
     */
    public void setStartingLives(Difficulty difficulty, int startingLives);

    /**
     * Sets the value of the max magic multiplier design parameter for the
     * specified difficulty. 
     * 
     * @param difficulty         the difficulty it's being set for.
     * @param maxMagic the value it's being set to.
     */
    public void setMaxMagic(Difficulty difficulty, int maxMagic);

    /**
     * Sets the value of the starting lives design parameter for the
     * specified difficulty.
     * 
     * @param difficulty     the difficulty it's being set for.
     * @param magicRegenRate the value it's being set to.
     */
    public void setMagicRegenRate(Difficulty difficulty, int magicRegenRate);

    /**
     * Sets the value of the shop item count design parameter for the
     * specified difficulty.
     * 
     * @param difficulty    the difficulty it's being set for.
     * @param shopItemCount the value it's being set to.
     */
    public void setShopItemCount(Difficulty difficulty, int shopItemCount);
}
