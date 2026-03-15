package wizardquest.gamemanager;

import java.util.ArrayList;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class ExampleEventsGenerator {
    
    private final ArrayList<String> userIDs;
    private final SettingsInterface settings;

    private ExampleEventsGenerator() {
        userIDs = new ArrayList<>();
        settings = SettingsSingleton.getInstance();
    }

    public static void main(String[] args) throws AuthenticationException {
        ExampleEventsGenerator generator = new ExampleEventsGenerator();
        AuthenticationResult result;

        // Each user has three sessions - one for each difficulty.
        int totalUsers = 50;
        generator.generateUserIDs(totalUsers);

        for (int i = 0; i < generator.userIDs.size(); i++) {
            
            // (Mock) authentication of a user
            result = new AuthenticationResult(
                "User " + generator.userIDs.get(i),
                generator.userIDs.get(i),
                RoleEnum.PLAYER); 
            generator.settings.loginWithResult(result);

            // User's first session on easy difficulty
            new SimulatedGameRun(
                DifficultyEnum.EASY,
                "../event_logs/example_events.json");

            // User's second session on medium difficulty
            new SimulatedGameRun(
                DifficultyEnum.MEDIUM,
                "../event_logs/example_events.json");

            // User's third session on hard difficulty
            new SimulatedGameRun(
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
}
