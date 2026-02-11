package WizardQuest;

/**
 * Provides global access to the game manager.
 */
public class GameManagerSingleton {
    private static final GameManagerInterface gameManager = new GameManager();

    private GameManagerSingleton() {
    }

    /**
     * Provides a reference to the game manager.
     * 
     * @return a reference to the game manager singleton.
     */
    public static GameManagerInterface getInstance() {
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

        @Override
        public boolean isGameRunning(){
            return this.currentGame != null;
        }

        @Override
        public DifficultyEnum getCurrentDifficulty(){
            return this.currentGame.getDifficulty();
        }

        @Override
        public void startNewGame(DifficultyEnum difficulty){
            this.currentGame = new GameRun(difficulty);
        }

        @Override
        public GameRunInterface getCurrentRun(){
            return this.currentGame;
        }

        @Override
        public PlayerInterface getCurrentPlayer(){
            return this.currentGame.getPlayer();
        }

        @Override
        public EncounterInterface pickEncounter(){
            EncounterInterface encounter = this.currentGame.pickEncounter();
            this.currentEncounter = encounter;
            return encounter;
        }

        @Override
        public EncounterInterface getCurrentEncounter(){
            return this.currentEncounter;
        }

        @Override
        public void resetFailedEncounter(){
            this.currentEncounter.resetEnemyHealth();
            PlayerInterface player = this.currentGame.getPlayer();
            player.resetHealth();
            player.loseLives(1);
        }

        @Override
        public void completeCurrentEncounter(){
            this.currentEncounter.markComplete();
        }

        @Override
        public void advanceToNextLevel(){
            this.currentGame.nextStage();
        }

        @Override
        public UpgradeEnum[] viewShop(){
            return this.currentGame.viewShop();
        }

        @Override
        public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException{
            this.currentGame.purchaseUpgrade(upgrade);
        }

        @Override
        public void endGame(){
            this.currentEncounter = null;
            this.currentGame = null;
        }
    }
}
