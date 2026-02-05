package WizardQuest;

/**
 * Provides global access to the game manager.
 */
public class GameManagerSingleton {
    private static GameManagerInterface gameManager = new GameManager();

    private GameManagerSingleton() {
    }

    /**
     * Provides a reference to the game manager.
     * 
     * @return a reference to the game manager singleton.
     */
    public static GameManagerInterface getGameManager() {
        return gameManager;
    }

    /**
     * Internal game manager class, acting as an interface to the back end.
     */
    private static class GameManager implements GameManagerInterface {
        private GameRunInterface currentGame;
        private EncounterInterface currentEncounter;

        public GameManager() {
            this.currentEncounter = null;
            this.currentGame = null;
        }
    }
}
