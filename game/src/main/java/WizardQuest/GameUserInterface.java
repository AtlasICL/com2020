package WizardQuest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;


public class GameUserInterface {

    private final GameManagerInterface gameManager;
    private final SettingsInterface settings;
    private final TimeManagerInterface timeManager;
    private final TelemetryListenerInterface telemetryListener;
    private final Scanner scanner;

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";

    private final boolean[] ownedUpgrades = new boolean[UpgradeEnum.values().length];

    private GameUserInterface() {
        this.gameManager = GameManagerSingleton.getInstance();
        this.settings = SettingsSingleton.getInstance();
        this.timeManager = TimeManagerSingleton.getInstance();
        this.telemetryListener = TelemetryListenerSingleton.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GameUserInterface ui = new GameUserInterface();
        ui.start();
    }

    public void start() {
        showTitle();
        if (!login()) {
            System.out.println(RED + "Cannot continue without authentication." + RESET);
            return;
        }
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
                + RESET);
        System.out.println();
    }

    private boolean login() {
        System.out.println(YELLOW + "Signing in via Google..." + RESET);
        try {
            Authenticator auth = new Authenticator();
            AuthenticationResult result = auth.login();
            settings.loginWithResult(result); // Settings handles session establishment.
            System.out.println(GREEN + "Welcome, " + result.name() + "! (Role: " + result.role() + ")" + RESET);
            return true;
        } catch (AuthenticationException e) {
            System.out.println(RED + "Login failed: " + e.getMessage() + RESET);
            return false;
        }
    }

    private void showMenu() {
        while (true) {
            System.out.println();
            System.out.println(BOLD + "Main Menu" + RESET);
            System.out.println("1. " + YELLOW + "Start New Game" + RESET);
            System.out.println("2. " + YELLOW + "Settings" + RESET);
            System.out.println("3, " + YELLOW + "View Telemetry Disclosure" + RESET);
            System.out.println("0. Quit");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    startGame();
                    break;
                case "2":
                    settingsMenu();
                    break;
                case "0":
                    quit();
                    return;
                case "3":
                    showTelemetryDisclosure();
                    break;
                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }
    }

    private void settingsMenu() {
        while (true) {
            System.out.println();
            System.out.println(BOLD + "Settings" + RESET);

            RoleEnum role;
            boolean telemetryEnabled;

            try {
                role = settings.getUserRole();
                telemetryEnabled = settings.isTelemetryEnabled();
            } catch (AuthenticationException e) {
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

            if (role == RoleEnum.DESIGNER || role == RoleEnum.DEVELOPER) {
                System.out.println("3. Change starting lives (design parameter)");
                System.out.println("4. Run Simulated Game");
            }

            /*
             * if (role == RoleEnum.DEVELOPER) {
             * System.out.println("5. Assign user roles [NOT FOR SPRINT 1]");
             * }
             */
            System.out.println("0. Go Back");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            if (input.equals("0"))
                return;

            if (input.equals("1")) {
                toggleTelemetry();
                continue;
            }

            if (input.equals("2")) {
                showTelemetryDisclosure();
                continue;
            }

            if (input.equals("3")) {
                if (role == RoleEnum.DESIGNER || role == RoleEnum.DEVELOPER) {
                    changeStartingLives();
                } else {
                    System.out.println(RED + "You do not have permission to change design parameters." + RESET);
                }
                continue;
            }

            if (input.equals("4")) {
                // TODO
            }
            /*
             * if (input.equals("5")) {
             * System.out.println("We are not doing this in Sprint 1.");
             * continue;
             * }
             */
            System.out.println(RED + "Invalid option." + RESET);
        }
    }

    private void toggleTelemetry() {
        try {
            boolean current = settings.isTelemetryEnabled();

            if (current) {
                settings.setTelemetryEnabled(false);
                System.out.println(RED + "Telemetry disabled." + RESET);
            } else {
                settings.setTelemetryEnabled(true);
                System.out.println(GREEN + "Telemetry enabled." + RESET);
            }
        } catch (AuthenticationException e) {
            System.out.println(RED + "You must be logged in to change telemetry settings." + RESET);
        }
    }

    private void changeStartingLives() {

        DifficultyEnum difficulty = selectDifficulty();
        if (difficulty == null)
            return;

        System.out.println();
        System.out.println(BOLD + "Set Starting Lives" + RESET);
        System.out.println("Difficulty: " + CYAN + difficulty + RESET);
        System.out.print(BLUE + "Enter starting lives (e.g. 1-10): " + RESET);

        String input = scanner.nextLine();

        int lives;
        try {
            lives = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Please enter a number." + RESET);
            return;
        }

        if (lives < 1) {
            System.out.println(RED + "Starting lives must be at least 1." + RESET);
            return;
        }

        try {
            settings.setStartingLives(difficulty, lives);
            telemetryListener.onSettingsChange(
                    new SettingsChangeEvent( settings.getUserID(),
                            timeManager.getCurrentTime(),
                            SettingsEnum.STARTING_LIVES,
                            String.valueOf(lives)));
            System.out.println(GREEN + "Starting lives updated." + RESET);
        } catch (AuthenticationException e) {
            System.out.println(RED + "You must be logged in to change design parameters." + RESET);
        }
    }

    private void showTelemetryDisclosure() {

        System.out.println();
        System.out.println(BOLD + "Telemetry Disclosure" + RESET);
        System.out.println();

        System.out.println(
                "If telemetry is enabled, the game records events anonymously to help balance difficulty and improve gameplay.");
        System.out.println("This includes:");
        System.out.println("timestamp, session id, user id");
        System.out.println("encounter start, completion, or fail (stage, difficulty, encounter name)");
        System.out.println("player state on completion or fail (health remaining, lives left if relevant)");
        System.out.println("coins gained and upgrades purchased");
        System.out.println("settings changes (new values)");
        System.out.println("enemies defeated");

        System.out.println();
        System.out.println("Why we collect it:");
        System.out.println("to spot difficulty spikes and unfair encounters");
        System.out.println("to support balancing changes with evidence");

        System.out.println();
        System.out.println("0. Back");

        System.out.print(BLUE + ">>> " + RESET);
        scanner.nextLine();
    }

    private void startGame() {
        DifficultyEnum difficulty = selectDifficulty();
        if (difficulty == null)
            return;

        //reset owned upgrades at the start of each new run
        for (int i = 0; i < ownedUpgrades.length; i++) {
            ownedUpgrades[i] = false;
        }

        gameManager.startNewGame(difficulty);
        gameLoop();
    }

    private String difficultyLabel(DifficultyEnum difficulty) {
        if (difficulty == DifficultyEnum.EASY)
            return GREEN + "Easy" + RESET;
        if (difficulty == DifficultyEnum.MEDIUM)
            return YELLOW + "Medium" + RESET;
        return RED + "Hard" + RESET;
    }

    private DifficultyEnum selectDifficulty() {
        while (true) {
            System.out.println();
            System.out.println(BOLD + "Select Difficulty" + RESET);

            int easyLives = settings.getStartingLives(DifficultyEnum.EASY);
            int mediumLives = settings.getStartingLives(DifficultyEnum.MEDIUM);
            int hardLives = settings.getStartingLives(DifficultyEnum.HARD);

            System.out.println("1. " + difficultyLabel(DifficultyEnum.EASY) + " " + BLUE + "[Starting Lives: "
                    + easyLives + "]" + RESET);
            System.out.println("2. " + difficultyLabel(DifficultyEnum.MEDIUM) + " " + BLUE + "[Starting Lives: "
                    + mediumLives + "]" + RESET);
            System.out.println("3. " + difficultyLabel(DifficultyEnum.HARD) + " " + BLUE + "[Starting Lives: "
                    + hardLives + "]" + RESET);
            System.out.println("0. Go Back");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    return null;
                case "1":
                    return DifficultyEnum.EASY;
                case "2":
                    return DifficultyEnum.MEDIUM;
                case "3":
                    return DifficultyEnum.HARD;
                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }
        }
    }

    private void gameLoop() {
        GameRunInterface lastRun = null;
        PlayerInterface lastPlayer = null;

        while (gameManager.isGameRunning()) {

            // keep latest references while the run is still alive
            try {
                lastRun = gameManager.getCurrentRun();
            } catch (Exception ignored) {
            }
            try {
                lastPlayer = gameManager.getCurrentPlayer();
            } catch (Exception ignored) {
            }

            boolean encounterWon = playEncounter();

            if (!gameManager.isGameRunning()) {
                endScreen(lastRun, lastPlayer);
                return;
            }

            if (!encounterWon) {
                continue;
            }

            //stop after stage 2 (no shop after finishing)
            GameRunInterface run = gameManager.getCurrentRun();
            if (run != null && run.getStage() >= 2) {
                System.out.println();
                System.out.println(GREEN + BOLD + "Stage 2 complete. Ending prototype run." + RESET);
                endScreen(lastRun, lastPlayer);
                return;
            }

            openShop();

            if (!gameManager.isGameRunning()) {
                endScreen(lastRun, lastPlayer);
                return;
            }

            gameManager.advanceToNextLevel();

            if (!gameManager.isGameRunning()) {
                endScreen(lastRun, lastPlayer);
                return;
            }
        }

        // if ever drops out without triggering the early returns
        endScreen(lastRun, lastPlayer);
    }

    private boolean playEncounter() {

        EncounterInterface encounter;

        try {
            encounter = gameManager.pickEncounter();
        } catch (Exception e) {
            System.out.println();
            System.out.println(RED + BOLD + "No encounters available right now." + RESET);
            System.out.println("Ending run.");
            gameManager.endGame();
            return false;
        }

        System.out.println();
        GameRunInterface run = gameManager.getCurrentRun();
        if (run != null) {
            System.out.println(BOLD + "Stage " + run.getStage() + RESET);
        }

        telemetryListener.onNormalEncounterStart(
                new NormalEncounterStartEvent(
                        settings.getUserID(), gameManager.getSessionID(), TimeManagerSingleton.getInstance().getCurrentTime(),
                        encounter.getType(),
                        gameManager.getCurrentDifficulty(),
                        run != null ? run.getStage() : 1));

        // Reset player at the start of the encounter
        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player == null) {
            gameManager.endGame();
            return false;
        }
        player.resetHealth();
        player.resetMagic();

        // Encounter Loop
        while (true) {
            System.out.println("Encounter: " + CYAN + encounter.getType().getDisplayName() + RESET);
            for (EntityInterface e : encounter.getEnemies()) {
                System.out
                        .println(RED + "\t" + e.getType().getDisplayName() + " (Health: " + e.getHealth() + "/" + e.getMaxHealth() + ")" + RESET);
            }

            player.gainMagic(Math.min(player.getMagicRegenRate(), (player.getMaxMagic() - player.getMagic())));


            EntityInterface[] enemies = encounter.getEnemies();

            if (player.getHealth() <= 0) {
                System.out.println(RED + BOLD + "You died." + RESET);
                telemetryListener.onNormalEncounterFail(
                        new NormalEncounterFailEvent(
                                settings.getUserID(), gameManager.getSessionID(),
                                timeManager.getCurrentTime(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                player.getLives()));
                gameManager.resetFailedEncounter();

                if (player.getLives() == 0) {
                    System.out.println(RED + "Out of lives." + RESET);
                    gameManager.endGame();
                }

                return false;
            }

            System.out.println();
            System.out.println("Health: " + GREEN + player.getHealth() + RESET + " / " + player.getMaxHealth());
            System.out.println("Magic: " + CYAN + player.getMagic() + RESET + " / " + player.getMaxMagic());
            System.out.println("Lives: " + YELLOW + player.getLives() + RESET);
            System.out.println("Coins: " + YELLOW + player.getCoins() + RESET);

            AbilityEnum[] abilities = getPlayerAbilities(player);

            if (abilities == null || abilities.length == 0) {
                System.out.println(RED + "No abilities available." + RESET);
                gameManager.endGame();
                return false;
            }

            System.out.println();
            System.out.println(BOLD + "Choose an ability:" + RESET);

            for (int i = 0; i < abilities.length; i++) {
                System.out.println((i + 1) + ". " + abilities[i].getDisplayName() + " (" + abilities[i].getBaseDamage() + " damage) - " + abilities[i].getDescription()
                        + " (Cost: " + abilities[i].getMagicCost() + " magic)");
            }

            System.out.println("0. Quit Run");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            int abilityChoice;
            try {
                abilityChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
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

            AbilityEnum chosenAbility = abilities[abilityChoice - 1];

            EntityInterface target = selectEnemyTarget(enemies);
            if (target == null) {
                System.out.println(RED + "No valid target." + RESET);
                continue;
            }

            int cost = chosenAbility.getMagicCost();
            if (player.getMagic() < cost) {
                System.out.println(RED + "Not enough magic, select another ability." + RESET);
                continue;
            }

            int targetHpBefore = target.getHealth();

            try {
                chosenAbility.execute(player, target);
            } catch (LackingResourceException e) {
                System.out.println(RED + "Not enough magic." + RESET);
                continue;
            }

            int targetHpAfter = target.getHealth();
            int damageDealt = targetHpBefore - targetHpAfter;
            if (damageDealt < 0)
                damageDealt = 0;

            System.out.println();
            System.out.println(GREEN + "You used " + chosenAbility.getDisplayName() + " on " + target.getType().getDisplayName()
                    + " for " + damageDealt + " damage." + RESET);

            if (target.getHealth() <= 0) {
                telemetryListener.onKillEnemy(
                        new KillEnemyEvent(
                                settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                target.getType()));
            }

            if (allEnemiesDead(enemies)) {
                telemetryListener.onNormalEncounterComplete(
                        new NormalEncounterCompleteEvent(
                                settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                encounter.getType(),
                                gameManager.getCurrentDifficulty(),
                                run != null ? run.getStage() : 1,
                                player.getHealth()));
                gameManager.completeCurrentEncounter();
                System.out.println(GREEN + "Encounter complete." + RESET);
                if (player != null) {
                    player.gainCoins(10);
                    telemetryListener.onGainCoin(
                            new GainCoinEvent(
                                    settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                    encounter.getType(),
                                    gameManager.getCurrentDifficulty(),
                                    run != null ? run.getStage() : 1,
                                    10));
                    System.out.println(YELLOW + "+10 coins" + RESET);
                }
                return true;
            }

            EntityAIInterface ai = EntityAISingleton.getInstance();

            for (EntityInterface enemy : enemies) {
                if (enemy == null)
                    continue;
                if (enemy.getHealth() <= 0)
                    continue;

                AbilityEnum[] enemyAbilities = getEnemyAbilities(enemy);
                if (enemyAbilities == null || enemyAbilities.length == 0)
                    continue;

                int playerHpBefore = player.getHealth();

                ai.useAbility(enemyAbilities, enemy, new EntityInterface[] { player });

                int playerHpAfter = player.getHealth();
                int damageTaken = playerHpBefore - playerHpAfter;
                System.out.println(damageTaken);
                if (damageTaken < 0)
                    damageTaken = 0;

                System.out.println();
                System.out.println(RED + enemy.getType().getDisplayName() + " attacked you for " + damageTaken + " damage." + RESET);
                System.out.println("Your health: " + GREEN + playerHpAfter + RESET + " / " + player.getMaxHealth());
            }
        }
    }

    private AbilityEnum[] getPlayerAbilities(PlayerInterface player) {
        if (player == null || player.getAbilities().isEmpty()) {
            return new AbilityEnum[0];
        }

        AbilityEnum[] abilities = new AbilityEnum[player.getAbilities().size()];
        for (int i = 0; i < player.getAbilities().size(); i++) {
            abilities[i] = player.getAbilities().get(i);
        }
        return abilities;
    }

    private AbilityEnum[] getEnemyAbilities(EntityInterface enemy) {
        if (enemy == null || enemy.getAbilities().isEmpty()) {
            return new AbilityEnum[0];
        }

        AbilityEnum[] abilities = new AbilityEnum[enemy.getAbilities().size()];
        for (int i = 0; i < enemy.getAbilities().size(); i++) {
            abilities[i] = enemy.getAbilities().get(i);
        }
        return abilities;
    }

    private EntityInterface selectEnemyTarget(EntityInterface[] enemies) {

        System.out.println();
        System.out.println(BOLD + "Choose a target:" + RESET);
        int shown = 0;

        for (int i = 0; i < enemies.length; i++) {
            EntityInterface enemy = enemies[i];
            if (enemy == null)
                continue;
            if (enemy.getHealth() <= 0)
                continue;

            shown++;
            System.out.println((i + 1) + ". " + enemy.getType().getDisplayName() + " (Health: " + enemy.getHealth() + "/" + enemy.getMaxHealth() +")");
        }

        if (shown == 0)
            return null;

        System.out.print(BLUE + ">>> " + RESET);
        String input = scanner.nextLine();

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }

        if (choice < 1 || choice > enemies.length)
            return null;

        EntityInterface chosen = enemies[choice - 1];
        if (chosen == null)
            return null;
        if (chosen.getHealth() <= 0)
            return null;

        return chosen;
    }

    private boolean allEnemiesDead(EntityInterface[] enemies) {
        for (EntityInterface enemy : enemies) {
            if (enemy == null)
                continue;
            if (enemy.getHealth() > 0)
                return false;
        }
        return true;
    }

    private void openShop() {
        System.out.println();
        System.out.println(BOLD + "Shop" + RESET);

        PlayerInterface player = gameManager.getCurrentPlayer();
        if (player != null) {
            System.out.println("Your Coins: " + YELLOW + player.getCoins() + RESET);
            System.out.println();
        }

        UpgradeEnum[] upgrades;

        try {
            upgrades = gameManager.viewShop();
        } catch (Exception e) {
            System.out.println(RED + "Shop is not available right now." + RESET);
            return;
        }

        if (upgrades == null || upgrades.length == 0) {
            System.out.println(RED + "No upgrades available." + RESET);
            return;
        }

        String ownedTag = MAGENTA + " [ALREADY OWNED]" + RESET;

        int[] optionToIndex = new int[upgrades.length];

        while (true) {
            int shown = 0;
            for (int i = 0; i < upgrades.length; i++) {
                UpgradeEnum up = upgrades[i];
                if (up == null)
                    continue;

                shown++;
                optionToIndex[shown - 1] = i;

                boolean owned = ownedUpgrades[up.ordinal()];
                String tag = owned ? ownedTag : "";

                System.out.println(shown + ". " + CYAN + up.getTelemetryName() + RESET
                        + " (Cost: " + YELLOW + up.getPrice() + RESET + ")" + tag);
            }

            if (shown == 0) {
                System.out.println(RED + "No upgrades available." + RESET);
                return;
            }

            System.out.println();
            System.out.println("0. Leave shop");

            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();

            if (input.equals("0"))
                return;

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number." + RESET);
                System.out.println();
                continue;
            }

            if (choice < 1 || choice > shown) {
                System.out.println(RED + "Invalid choice." + RESET);
                System.out.println();
                continue;
            }

            int upgradeIndex = optionToIndex[choice - 1];
            UpgradeEnum bought = upgrades[upgradeIndex];

            if (bought == null) {
                System.out.println(RED + "Invalid choice." + RESET);
                System.out.println();
                continue;
            }

            if (ownedUpgrades[bought.ordinal()]) {
                System.out.println(RED + "You already own that upgrade." + RESET);
                System.out.println();
                continue;
            }

            try {
                gameManager.purchaseUpgrade(bought);
                telemetryListener.onBuyUpgrade(
                        new BuyUpgradeEvent(
                                settings.getUserID(), gameManager.getSessionID(), timeManager.getCurrentTime(),
                                gameManager.getCurrentEncounter() != null ? gameManager.getCurrentEncounter().getType()
                                        : null,
                                gameManager.getCurrentDifficulty(),
                                gameManager.getCurrentRun() != null ? gameManager.getCurrentRun().getStage() : 1,
                                bought,
                                bought.getPrice()));
                System.out.println(GREEN + "Upgrade purchased." + RESET);

                PlayerInterface p = gameManager.getCurrentPlayer();
                if (p != null) {
                    System.out.println("Remaining Coins: " + YELLOW + p.getCoins() + RESET);
                }
            } catch (LackingResourceException e) {
                System.out.println(RED + "Not enough coins." + RESET);
            } catch (Exception e) {
                System.out.println(RED + "Could not purchase upgrade." + RESET);
            }

            System.out.println();
            if (player != null) {
                System.out.println("Your Coins: " + YELLOW + player.getCoins() + RESET);
                System.out.println();
            }
        }
    }

    private void endScreen(GameRunInterface run, PlayerInterface player) {
        System.out.println();
        System.out.println(BOLD + "Run Complete" + RESET);

        if (run == null) {
            System.out.println(RED + "No run data available." + RESET);
            System.out.println();
            System.out.print(BLUE + "Press 0 to return to the main menu..." + RESET);
            while (!"0".equals(scanner.nextLine())) {
                System.out.println(RED + "Invalid option." + RESET);
                System.out.print(BLUE + ">>> " + RESET);
            }
            gameManager.endGame();
            return;
        }

        LocalDateTime start = run.getRunStartTime();
        if (start == null)
            start = LocalDateTime.now();

        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        System.out.println("Run Time: " + CYAN + minutes + "m " + seconds + "s" + RESET);
        System.out.println("Stages Reached: " + CYAN + run.getStage() + RESET);
        System.out.println("Deaths: " + CYAN + run.getDeathCount() + RESET);

        if (player != null) {
            System.out.println("Coins: " + YELLOW + player.getCoins() + RESET);
        }

        System.out.println();
        System.out.println("0. Back to main menu");

        while (true) {
            System.out.print(BLUE + ">>> " + RESET);
            String input = scanner.nextLine();
            if ("0".equals(input))
                break;
            System.out.println(RED + "Invalid option." + RESET);
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
