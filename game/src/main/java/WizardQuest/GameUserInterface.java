package WizardQuest;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Scanner;


public class GameUserInterface {

    private final GameManagerInterface gameManager;
    private final SettingsInterface settings = SettingsSingleton.getSettings();
    private final Scanner scanner;

    private boolean telemetryEnabled = true;

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";

    private GameUserInterface() {
        this.gameManager = GameManagerSingleton.getGameManager();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GameUserInterface ui = new GameUserInterface();
        ui.start();
    }

    public void start() {
        showTitle();
        showMenu();
    }

    private void showTitle() {
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
     * LOGIN SCREEN/LOGIC TO BE IMPLEMENTED BY EMRE + LUCA C
     * Needs role testing for settings access
     * First user will be developer by default, can assign other roles through settings menu
     */

    private void showMenu() {
        while (true) {
            System.out.println();
            System.out.println(BOLD + "Main Menu" + RESET);
            System.out.println("1. " + YELLOW + "Start New Game" + RESET);
            System.out.println("2. " + YELLOW + "Settings" + RESET);
            System.out.println("3. Quit");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    startGame();
                    break;
                case "2":
                    settingsMenu();
                    break;
                case "3":
                    quit();
                    return;
                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }
    }

    private void settingsMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println();
            System.out.println(BOLD + "Settings" + RESET);

            Role role = Role.DEVELOPER; //placeholder values in case of auth failure, overrides if auth successful
            boolean telemetryEnabled = false;

            try {
                role = settings.getUserRole();
                telemetryEnabled = settings.isTelemetryEnabled();
            }
            catch (AuthenticationException e) {
                System.out.println(RED + "You must be logged in to view settings." + RESET);
                System.out.println("0. Go Back");
                System.out.print(BLUE + ">>> " + RESET);
                scanner.nextLine();
                return;
            }

            System.out.println("User role: " + CYAN + role + RESET);
            System.out.println("Telemetry: " + (telemetryEnabled ? GREEN + "ON" + RESET : RED + "OFF" + RESET));
            System.out.println();

            System.out.println("1. Toggle telemetry");
            System.out.println("2. Telemetry disclosure");

            if (role == Role.DESIGNER || role == Role.DEVELOPER) {
                System.out.println("3. Change starting lives (design parameter)");
            }

            if (role == Role.DEVELOPER) {
                System.out.println("4. Assign user role");
            }

            System.out.println("0. Go Back");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            if (input.equals("0")) {
                return;
            }

            if (input.equals("1")) {
                toggleTelemetry();
                continue;
            }

            if (input.equals("2")) {
                showTelemetryDisclosure();
                continue;
            }

            if (input.equals("3")) {
                if (role == Role.DESIGNER || role == Role.DEVELOPER) {
                    changeStartingLives();
                }
                else {
                    System.out.println(RED + "You do not have permission to change design parameters." + RESET);
                }
                continue;
            }

            if (input.equals("4")) {
                if (role == Role.DEVELOPER) {
                    assignUserRole();
                }
                else {
                    System.out.println(RED + "You do not have permission to assign roles." + RESET);
                }
                continue;
            }

            System.out.println(RED + "Invalid option." + RESET);
        }
    }

    private void toggleTelemetry() {

        try {
            boolean current = settings.isTelemetryEnabled();

            if (current == true) {
                settings.setTelemetryEnabled(false);
                System.out.println(RED + "Telemetry disabled." + RESET);
            }
            else {
                settings.setTelemetryEnabled(true);
                System.out.println(GREEN + "Telemetry enabled." + RESET);
            }
        }
        catch (AuthenticationException e) {
            System.out.println(RED + "You must be logged in to change telemetry settings." + RESET);
        }
    }

    private void changeStartingLives() {

        Difficulty difficulty = selectDifficulty();

        if (difficulty == null) {
            return;
        }

        System.out.println();
        System.out.println(BOLD + "Set Starting Lives" + RESET);
        System.out.println("Difficulty: " + CYAN + difficulty + RESET);
        System.out.print(BLUE + "Enter starting lives (e.g. 1-10): " + RESET);

        String input = scanner.nextLine();

        int lives;
        try {
            lives = Integer.parseInt(input);
        }
        catch (NumberFormatException e) {
            System.out.println(RED + "Please enter a number." + RESET);
            return;
        }

        if (lives < 1) {
            System.out.println(RED + "Starting lives must be at least 1." + RESET);
            return;
        }

        try {
            settings.setStartingLives(difficulty, lives);
            System.out.println(GREEN + "Starting lives updated." + RESET);
        }
        catch (AuthenticationException e) {
            System.out.println(RED + "You must be logged in to change design parameters." + RESET);
        }
    }

    private void assignUserRole() {

        System.out.println();
        System.out.println(BOLD + "Assign User Role" + RESET);

        System.out.print(BLUE + "Username: " + RESET);
        String username = scanner.nextLine();

        System.out.println("1. PLAYER");
        System.out.println("2. DESIGNER");
        System.out.println("3. DEVELOPER");
        System.out.print(BLUE + ">>> " + RESET);

        String input = scanner.nextLine();

        Role newRole;

        if (input.equals("1")) {
            newRole = Role.PLAYER;
        }
        else if (input.equals("2")) {
            newRole = Role.DESIGNER;
        }
        else if (input.equals("3")) {
            newRole = Role.DEVELOPER;
        }
        else {
            System.out.println(RED + "Invalid role." + RESET);
            return;
        }

        try {
            settings.setUserRole(username, newRole);
            System.out.println(GREEN + "Role updated for " + username + "." + RESET);
        }
        catch (AuthenticationException e) {
            System.out.println(RED + "You must be logged in as a developer to assign roles." + RESET);
        }
    }

    private void showTelemetryDisclosure() {

        System.out.println();
        System.out.println(BOLD + "Telemetry Disclosure" + RESET);
        System.out.println();

        System.out.println("If telemetry is enabled, the game records events anonymously to help balance difficulty and improve gameplay.");
        System.out.println("This includes:");

        System.out.println("timestamp, session id, user id");
        System.out.println("encounter start, completion, or fail (stage, difficulty, encounter name)");
        System.out.println("player state on completion or fail (HP remaining, lives left if relevant)");
        System.out.println("coins gained and upgrades purchased");
        System.out.println("settings changes (new values)");
        System.out.println("enemies defeated");

        System.out.println();
        System.out.println("Why we collect it:");
        System.out.println("to spot difficulty spikes and unfair encounters");
        System.out.println("to support balancing changes with evidence");

        System.out.println();
        System.out.println("You can toggle telemetry off at any time in Settings and between encounters.");
        System.out.println();
        System.out.println("0. Back");

        System.out.print(BLUE + ">>> " + RESET);
        scanner.nextLine();
    }



    private void startGame() {
        Difficulty difficulty = selectDifficulty();

        if (difficulty == null) {
            return;
        }

        gameManager.startNewGame(difficulty);
        gameLoop();
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
        while (true) {
            System.out.println();
            System.out.println(BOLD + "Select Difficulty" + RESET);

            System.out.println("1. " + difficultyLabel(Difficulty.EASY));
            System.out.println("2. " + difficultyLabel(Difficulty.MEDIUM));
            System.out.println("3. " + difficultyLabel(Difficulty.HARD));
            System.out.println("0. Go Back");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    return null;
                case "1":
                    return Difficulty.EASY;
                case "2":
                    return Difficulty.MEDIUM;
                case "3":
                    return Difficulty.HARD;
                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }
    }

    private void gameLoop() {
        while (gameManager.isGameRunning()) {

            boolean encounterWon = playEncounter();

            if (!gameManager.isGameRunning()) {
                return;
            }

            if (!encounterWon) {
                continue;
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
            System.out.println(RED + BOLD + "No encounters available right now." + RESET);
            System.out.println("Ending run.");
            gameManager.endGame();
            return false;
        }

        System.out.println();
        System.out.println(BOLD + "Stage " + gameManager.getCurrentRun().getStage() + RESET);
        System.out.println("Encounter: " + CYAN + encounter.getType() + RESET);

        while (true) {

            PlayerInterface player = gameManager.getCurrentPlayer();

            if (player == null) {
                gameManager.endGame();
                return false;
            }

            EntityInterface[] enemies = encounter.getEnemies();

            if (allEnemiesDead(enemies)) {
                //win case, encounter completed
                gameManager.completeCurrentEncounter();
                System.out.println(GREEN + "Encounter complete." + RESET);
                return true;
            }

            //lose case (health <= 0)
            if (player.getHealth() <= 0) {
                System.out.println(RED + BOLD + "You died." + RESET);

                gameManager.resetFailedEncounter();

                //lose game case (no lives left)
                if (player.getLives() == 0) {
                    System.out.println(RED + "Out of lives." + RESET);
                    gameManager.endGame();
                }

                return false;
            }

            System.out.println();
            System.out.println("HP: " + GREEN + player.getHealth() + RESET + " / " + player.getMaxHealth());
            System.out.println("Magic: " + CYAN + player.getMagic() + RESET + " / " + player.getMaxMagic());
            System.out.println("Lives: " + YELLOW + player.getLives() + RESET);
            System.out.println("Coins: " + YELLOW + player.getCoins() + RESET);

            AbilityType[] abilities = getPlayerAbilities(player);

            if (abilities == null || abilities.length == 0) {
                System.out.println(RED + "No abilities available." + RESET);
                gameManager.endGame(); //end game if no abilities to use, player can't do anything
                return false;
            }

            System.out.println();
            System.out.println(BOLD + "Choose an ability:" + RESET);

            for (int i = 0; i < abilities.length; i++) {
                System.out.println((i + 1) + ". " + abilities[i].getDescription()
                        + " (Cost: " + abilities[i].getMagicCost() + ")");
            }

            System.out.println("0. Quit Run");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            int abilityChoice;
            try {
                abilityChoice = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number." + RESET);
                continue;
            }

            if (abilityChoice == 0) {
                gameManager.endGame();
                return false;
            }

            if (abilityChoice < 1 || abilityChoice > abilities.length) {
                System.out.println(RED + "Invalid choice." + RESET);
                continue;
            }

            AbilityType chosenAbility = abilities[abilityChoice - 1];

            EntityInterface target = selectEnemyTarget(enemies);

            if (target == null) {
                System.out.println(RED + "No valid target." + RESET);
                continue;
            }

            chosenAbility.execute(player, target);

            if (allEnemiesDead(enemies)) {
                //win case, complete encounter, will advance to shop/next level
                gameManager.completeCurrentEncounter();
                System.out.println(GREEN + "Encounter complete." + RESET);
                return true;
            }

            //enemy turn
            EntityAIInterface ai = EntityAISingleton.getInstance();

            for (EntityInterface enemy : enemies) {
                if (enemy == null) continue;
                if (enemy.getHealth() <= 0) continue;

                AbilityType[] enemyAbilities = getEnemyAbilities(enemy);

                if (enemyAbilities == null || enemyAbilities.length == 0) {
                    continue;
                }

                ai.useAbility(enemyAbilities, enemy, new EntityInterface[] { player });
            }
        }
    }

    private AbilityType[] getPlayerAbilities(PlayerInterface player) {

        if (player == null || player.getAbilities().isEmpty()) {
            return new AbilityType[0];
        }

        AbilityType[] abilities = new AbilityType[player.getAbilities().size()];

        for (int i = 0; i < player.getAbilities().size(); i++) {
            abilities[i] = player.getAbilities().get(i);
        }

        return abilities;
    }

        private AbilityType[] getEnemyAbilities(EntityInterface enemy) {

        if (enemy == null || enemy.getAbilities().isEmpty()) {
            return new AbilityType[0];
        }

        AbilityType[] abilities = new AbilityType[enemy.getAbilities().size()];

        for (int i = 0; i < enemy.getAbilities().size(); i++) {
            abilities[i] = enemy.getAbilities().get(i);
        }

        return abilities;
    }

    private EntityInterface selectEnemyTarget(EntityInterface[] enemies) {

        System.out.println();
        System.out.println(BOLD + "Choose a target:" + RESET); //for multi-enemy encounters (single-case too)
        int shown = 0;

        for (int i = 0; i < enemies.length; i++) {
            EntityInterface enemy = enemies[i];
            if (enemy == null) continue;
            if (enemy.getHealth() <= 0) continue;

            shown++;
            System.out.println((i + 1) + ". " + enemy.getType() + " (HP: " + enemy.getHealth() + ")");
        }

        if (shown == 0) {
            return null;
        }

        System.out.print(BLUE + ">>> " + RESET);
        String input = scanner.nextLine();

        int choice;
        try {
            choice = Integer.parseInt(input);
        }
        catch (NumberFormatException e) {
            return null;
        }

        if (choice < 1 || choice > enemies.length) {
            return null;
        }

        EntityInterface chosen = enemies[choice - 1];

        if (chosen == null) return null;
        if (chosen.getHealth() <= 0) return null;

        return chosen;
    }

    private boolean allEnemiesDead(EntityInterface[] enemies) {
        for (EntityInterface enemy : enemies) {
            if (enemy == null) continue;
            if (enemy.getHealth() > 0) {
                return false;
            }
        }
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

        boolean telemetryNow = false;

        try {
            telemetryNow = settings.isTelemetryEnabled();
        }
        catch (AuthenticationException ignored) {
        }

        System.out.println();
        System.out.println("9. Toggle telemetry (Currently: "
                + (telemetryNow ? GREEN + "ON" : RED + "OFF") + RESET + ")");
        System.out.println("0. Leave shop");

        while (true) {

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            if (input.equals("0")) {
                return;
            }

            if (input.equals("9")) {

                try {
                    boolean current = settings.isTelemetryEnabled();
                    settings.setTelemetryEnabled(!current);

                    System.out.println();
                    System.out.println(YELLOW + "Telemetry updated. Returning to main menu..." + RESET);
                    gameManager.endGame();
                    return;
                }
                catch (AuthenticationException e) {
                    System.out.println(RED + "You must be logged in to change telemetry." + RESET);
                    continue;
                }
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number." + RESET);
                continue;
            }

            if (choice < 1 || choice > upgrades.length) {
                System.out.println(RED + "Invalid choice." + RESET);
                continue;
            }

            try {
                gameManager.purchaseUpgrade(upgrades[choice - 1]);
                System.out.println(GREEN + "Upgrade purchased." + RESET);
            }
            catch (LackingResourceException e) {
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
        LocalDateTime start = run.getRunStartTime();
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

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

        System.out.println("Run Time: " + CYAN + minutes + "m " + seconds + "s" + RESET);
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

