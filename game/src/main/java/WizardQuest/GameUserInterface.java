package WizardQuest;

import java.util.Scanner;

public class GameUserInterface {

    private final GameManagerInterface gameManager;
    private final Scanner scanner;

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";

    // Google SSO template

    //private String currentUserId = null; //ID is just email?
    //private boolean ssoPlanned = true;

    public GameUserInterface() {
        this.gameManager = GameManagerSingleton.getGameManager();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GameUserInterface ui = new GameUserInterface();
        ui.start();
    }

    public void start() {
        showTitle();
        //loginScreen(); only needed once sso is properly implemented
        showMenu();
    }

    private void showTitle() {
        //aspect ratio may affect how this looks on different screens, can remove if needed
        System.out.println(MAGENTA + 
            " _    _   _                            _   _____                        _   \n" +
            "| |  | | (_)                          | | |  _  |                      | |  \n" +
            "| |  | |  _   ____   __ _   _ __    __| | | | | |  _   _    ___   ___  | |_ \n" +
            "| |/\\| | | | |_  /  / _` | | '__|  / _` | | | | | | | | |  / _ \\ / __| | __|\n" +
            "\\  /\\  / | |  / /  | (_| | | |    | (_| | \\ \\/' / | |_| | |  __/ \\__ \\ | |_ \n" +
            " \\/  \\/  |_| /___|  \\__,_| |_|     \\__,_|  \\_/\\_\\  \\__,_|  \\___| |___/  \\__|\n"
            + RESET
        );

    System.out.println();
}

    /**
     *placeholder for google sso implementation
     *ID will be provided by google sso
     */
    //private void loginScreen() {
   // }




    private void showMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println();
            System.out.println(BOLD + "Main Menu" + RESET);
            System.out.println("1. " + YELLOW + "Start New Game" + RESET);
            System.err.println("2. " + YELLOW + "Settings" + RESET);
            System.out.println("3. Quit");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    startGame();
                    break;
                case "2":
                    System.out.println(YELLOW + "Settings are not available yet." + RESET);
                    break;
                case "3":
                    quit();
                    return;
                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }
    }

    private void startGame() {
        Difficulty difficulty = selectDifficulty();

        if (difficulty == null) {
            return; //back to main menu
        }

        gameManager.startNewGame(difficulty);
        gameLoop();
    }

    /**
     * Checks whether a difficulty has available encounters.
     */
    private boolean hasAvailableEncounters(Difficulty difficulty) {
        try {
            gameManager.startNewGame(difficulty);
            gameManager.pickEncounter();
            gameManager.endGame();
            return true;
        }
        catch (Exception e) {
            gameManager.endGame();
            return false;
        }
    }

    private String difficultyLabel(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return GREEN + "Easy" + RESET;
        }
        if (difficulty == Difficulty.MEDIUM) {
            return YELLOW + "Medium" + RESET;
        }
        return RED + "Hard" + RESET;
    }

    private Difficulty selectDifficulty() {

        boolean easyAvailable = hasAvailableEncounters(Difficulty.EASY);
        boolean mediumAvailable = hasAvailableEncounters(Difficulty.MEDIUM);
        boolean hardAvailable = hasAvailableEncounters(Difficulty.HARD);

        boolean isRunning = true;
        while (isRunning) {
            System.out.println();
            System.out.println(BOLD + "Select Difficulty" + RESET);

            String noEnc = MAGENTA + BOLD + " [NO AVAILABLE ENCOUNTERS]" + RESET;

            System.out.println("1. " + difficultyLabel(Difficulty.EASY) + (easyAvailable ? "" : noEnc));
            System.out.println("2. " + difficultyLabel(Difficulty.MEDIUM) + (mediumAvailable ? "" : noEnc));
            System.out.println("3. " + difficultyLabel(Difficulty.HARD) + (hardAvailable ? "" : noEnc));
            System.out.println("0. Go Back");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    return null;

                case "1":
                    if (easyAvailable) {
                        return Difficulty.EASY;
                    }
                    System.out.println(RED + "Easy is not available yet." + RESET);
                    break;

                case "2":
                    if (mediumAvailable) {
                        return Difficulty.MEDIUM;
                    }
                    System.out.println(RED + "Medium is not available yet." + RESET);
                    break;

                case "3":
                    if (hardAvailable) {
                        return Difficulty.HARD;
                    }
                    System.out.println(RED + "Hard is not available yet." + RESET);
                    break;

                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }

        return null;
    }

    private void gameLoop() {
        while (gameManager.isGameRunning()) {

            boolean encounterStarted = playEncounter();

            if (!gameManager.isGameRunning()) {
                return;
            }

            if (!encounterStarted) {
                return;
            }

            openShop();

            if (!gameManager.isGameRunning()) {
                return;
            }

            gameManager.advanceToNextLevel();
        }

        endScreen();
    }

    private boolean playEncounter() {
        EncounterInterface encounter;

        try {
            encounter = gameManager.pickEncounter();
        }
        catch (Exception e) {
            System.out.println();
            System.out.println(RED + BOLD + "No encounters available for this difficulty yet." + RESET);
            System.out.println("Please try another difficulty.");
            System.out.println("Ending run.");
            gameManager.endGame();
            return false;
        }

        System.out.println();
        System.out.println(BOLD + "Stage " + gameManager.getCurrentRun().getStage() + RESET);
        System.out.println("Encounter: " + CYAN + encounter.getType() + RESET);
        System.out.println();

        while (!encounter.isComplete()) {

            PlayerInterface player = gameManager.getCurrentPlayer();

            if (player != null) {
                System.out.println("HP: " + GREEN + player.getHealth() + RESET + " / " + player.getMaxHealth());
                System.out.println("Magic: " + CYAN + player.getMagic() + RESET);
                System.out.println("Lives: " + YELLOW + player.getLives() + RESET);
                System.out.println("Coins: " + YELLOW + player.getCoins() + RESET);
            }

            System.out.println();
            System.out.println("1. " + YELLOW + "Attack" + RESET);
            System.out.println("2. Quit Run");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            if (input.equals("1")) {
                System.out.println(YELLOW + "You attack..." + RESET);
                gameManager.completeCurrentEncounter();
            }
            else if (input.equals("2")) {
                gameManager.endGame();
                return true;
            }
            else {
                System.out.println(RED + "Invalid option." + RESET);
            }
        }

        System.out.println(GREEN + "Encounter complete." + RESET);
        return true;
    }

    private void openShop() {
        System.out.println();
        System.out.println(BOLD + "Shop" + RESET);

        UpgradeType[] upgrades;

        try {
            upgrades = gameManager.viewShop();
        }
        catch (Exception e) {
            System.out.println(RED + "Shop is not available right now." + RESET);
            return;
        }

        if (upgrades == null || upgrades.length == 0) {
            System.out.println(RED + "No upgrades available." + RESET);
            return;
        }

        for (int i = 0; i < upgrades.length; i++) {
            System.out.println((i + 1) + ". " + CYAN + upgrades[i].getTelemetryName() + RESET
                    + " (Cost: " + YELLOW + upgrades[i].getPrice() + RESET + ")");
        }

        System.out.println("0. Leave shop");
        boolean isRunning = true;
        while (isRunning ) {
            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number." + RESET);
                continue;
            }

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > upgrades.length) {
                System.out.println(RED + "Invalid choice." + RESET);
                continue;
            }

            try {
                gameManager.purchaseUpgrade(upgrades[choice - 1]);
                System.out.println(GREEN + "Upgrade purchased." + RESET);
            }
            catch (NotEnoughResourceException e) {
                System.out.println(RED + "Not enough coins." + RESET);
            }
            catch (Exception e) {
                System.out.println(RED + "Could not purchase upgrade." + RESET);
            }
        }
    }

    private void endScreen() {
        System.out.println();
        System.out.println(BOLD + "Run Complete" + RESET);

        GameRunInterface run = gameManager.getCurrentRun();

        if (run == null) {
            System.out.println(RED + "No run data available." + RESET);
            System.out.println();
            return;
        }

        PlayerInterface player;
        try {
            player = gameManager.getCurrentPlayer();
        }
        catch (Exception e) {
            player = null;
        }

        System.out.println("Stages Reached: " + CYAN + run.getStage() + RESET);
        System.out.println("Deaths: " + CYAN + run.getDeathCount() + RESET);

        if (player != null) {
            System.out.println("Coins: " + YELLOW + player.getCoins() + RESET);
        }

        System.out.println();
        System.out.println("Returning to main menu...");
        System.out.println();

        gameManager.endGame();
    }

    private void quit() {
        System.out.println(YELLOW + "Thanks for playing WizardQuest!" + RESET);
        scanner.close();
    }
}
