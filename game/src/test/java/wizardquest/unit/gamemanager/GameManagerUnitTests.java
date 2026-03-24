package wizardquest.unit.gamemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.abilities.DamageEnum;
import wizardquest.gamemanager.GameManagerInterface;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerUnitTests {

    private GameManagerInterface gameManager;

    /**
     * Instantiate a gameManager object.
     */
    @BeforeEach
    void setUp() {
        gameManager = GameManagerSingleton.getInstance();
    }

    /**
     * When starting a new game, a sessionID should be created for this.
     * This should also instantiate a GameRun object for the current game.
     */
    @Test
    @DisplayName("GameManager - Starting a new game creates a sessionID")
    void startNewGame_setsSessionID() {
        // Before starting a new game, the game should not be running.
        // Current run of the game manager should also be null.
        assertFalse(gameManager.isGameRunning());
        assertNull(gameManager.getCurrentRun());
        // After starting the game, the game should be running.
        // Current run of the game manager should no longer be null.
        gameManager.startNewGame(DifficultyEnum.MEDIUM);
        assertTrue(gameManager.isGameRunning());
        assertNotNull(gameManager.getCurrentRun());
        assertNotEquals(-1, gameManager.getSessionID());
        // Invoke the endGame method to clean up.
        gameManager.endGame();
    }

    /**
     * When an encounter is selected, this should be set as the current
     * encounter of the game manager.
     */
    @Test
    @DisplayName("GameManager - Selecting an encounter instantiates the current encounter")
    void selectEncounter_setsEncounter() {
        // Before selecting an encounter, the current encounter of the game manager should
        // be null.
        assertNull(gameManager.getCurrentEncounter());
        // After starting the game and selecting an encounter, the current run of the game
        // manager should no longer be null.
        gameManager.startNewGame(DifficultyEnum.MEDIUM);
        gameManager.pickEncounter();
        assertNotNull(gameManager.getCurrentEncounter());
        // Invoke the endGame method to clean up.
        gameManager.endGame();
    }

    /**
     * When an encounter is failed, the health of all enemies in that encounter should be reset
     * to its maximum value.
     * The player's death count should also be incremented.
     */
    @Test
    @DisplayName("GameManager - Values modified correctly after a failed encounter")
    void resetFailedEncounter_modifiesValuesCorrectly() {
        // Start the game and select an encounter.
        gameManager.startNewGame(DifficultyEnum.MEDIUM);
        gameManager.pickEncounter();
        // Get the player's initial death count.
        int initialDeathCount = gameManager.getCurrentRun().getDeathCount();
        // Decrease an enemy's health by 10.
        gameManager.getCurrentEncounter().getEnemies()[0].loseHealth(10, DamageEnum.ABSOLUTE);
        // After resetting the encounter, the player's death count should have
        // incremented by 1.
        // The health of the aforementioned enemy should have reset to its
        // maximum value.
        gameManager.resetFailedEncounter();
        assertEquals(initialDeathCount + 1, gameManager.getCurrentRun().getDeathCount());
        assertEquals(gameManager.getCurrentEncounter().getEnemies()[0].getMaxHealth(),
                gameManager.getCurrentEncounter().getEnemies()[0].getHealth());
        // Invoke the endGame method to clean up.
        gameManager.endGame();
    }

    /**
     * When the game ends, the run and encounter member variables should be reset to NULL.
     */
    @Test
    @DisplayName("GameManager - Ending the game clears the current run and encounter")
    void endGame_clearsCurrentRunAndEncounter() {
        // Start the game and select an encounter.
        gameManager.startNewGame(DifficultyEnum.MEDIUM);
        gameManager.pickEncounter();
        // After ending the game, the current run and encounter of the game manager
        // should have reset to null.
        gameManager.endGame();
        assertNull(gameManager.getCurrentRun());
        assertNull(gameManager.getCurrentEncounter());
    }
}