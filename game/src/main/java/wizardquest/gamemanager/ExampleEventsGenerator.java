package wizardquest.gamemanager;

import java.util.ArrayList;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class ExampleEventsGenerator {
    
    private ArrayList<String> userIDs;
    private ArrayList<Integer> sessionIDs;
    private SettingsInterface settings;

    private ExampleEventsGenerator() {
        userIDs = new ArrayList<>();
        sessionIDs = new ArrayList<>();
        settings = SettingsSingleton.getInstance();
    }

    public static void main(String[] args) throws AuthenticationException {
        ExampleEventsGenerator generator = new ExampleEventsGenerator();
        AuthenticationResult result;

        // Each user has three sessions - one for each difficulty.
        int totalUsers = 40;
        generator.generateUserIDs(totalUsers);
        generator.generateSessionIDs(totalUsers * 3);

        for (int i = 0; i < generator.userIDs.size(); i++) {
            // Authenticate the user
            result = new AuthenticationResult(
                "User " + generator.userIDs.get(i),
                generator.userIDs.get(i),
                RoleEnum.PLAYER); 
            generator.settings.loginWithResult(result);

            // User's first session on easy difficulty
            SimulatedGameRun easyRun = new SimulatedGameRun(
                DifficultyEnum.EASY,
                "../event_logs/example_events.json");

            // User's second session on medium difficulty
            SimulatedGameRun mediumRun = new SimulatedGameRun(
                DifficultyEnum.MEDIUM,
                "../event_logs/example_events.json");

            // User's third session on hard difficulty
            SimulatedGameRun hardRun = new SimulatedGameRun(
                DifficultyEnum.HARD,
                "../event_logs/example_events.json");
        }
    }

    private void generateUserIDs(int total) {
        for (int i = 0; i < total; i++) {
            String userIDString = Integer.toString(i + 100000);
            userIDs.add(userIDString);
        }
    }

    private void generateSessionIDs(int total) {
        for (int i = 0; i < total; i++) {
            sessionIDs.add(i + 100000);
        }
    }
}
