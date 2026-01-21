package com.thespecialcharacters.WizardQuest;


public class GameManager implements GameManagerInterface {
    static GameManagerInterface gameManager = new GameManager();

    private GameRun currentGame;

    private Encounter currentEncounter;

    private GameManager(){
    }

    /**
     * Provides access to the game manager, providing a single access point between the front and back end.
     * 
     * @return returns a reference to the game manager singleton.
     */
    @Overide
    public static GameManagerInterface getGameManager(){
        return gameManager
    }
}
