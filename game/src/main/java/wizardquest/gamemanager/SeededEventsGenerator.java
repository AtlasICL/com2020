package wizardquest.gamemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import wizardquest.auth.AuthenticationException;
import wizardquest.auth.AuthenticationResult;
import wizardquest.auth.RoleEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;
import wizardquest.telemetry.SettingsChangeEvent;
import wizardquest.telemetry.TelemetryListenerInterface;
import wizardquest.telemetry.TelemetryListenerSingleton;

public class SeededEventsGenerator {
    
    private final ArrayList<String> userIDs;
    private final SettingsInterface settings;
    private final TelemetryListenerInterface telemetryListener;
    private final TimeManagerInterface timeManager;
    private static final File DESTINATION_FILE = new File("../event_logs/example_events.json");

    /**
     * Event generator for the seeded telemetry dataset.
     * Uses a list of mock userIDs, which are generated in this class and then (mock)
     * authenticated.
     * Each mock user conducts a simulated run using the logic of SimulatedGameRun,
     * to ensure that event order constraints are enforced correctly.
     */
    private SeededEventsGenerator() {
        userIDs = new ArrayList<>();
        settings = SettingsSingleton.getInstance();
        telemetryListener = TelemetryListenerSingleton.getInstance();
        TimeManagerSingleton.useSimulationTime();
        timeManager = TimeManagerSingleton.getInstance();

    }

    /**
     * Generate mock user IDs for the seeded events, starting from 100000 and
     * incrementing by 1 each time.
     *
     * @param total the total user IDs to be generated.
     */
    private void generateUserIDs(int total) {
        for (int i = 0; i < total; i++) {
            String userIDString = Integer.toString(i + 100000);
            userIDs.add(userIDString);
        }
    }

    /**
     * Selects a random setting to be adjusted by a SettingsChangeEvent.
     * The purpose of this script is to generate telemetry events - therefore,
     * the telemetry toggle is not a setting that can be returned here.
     *
     * @return the setting to be changed.
     */
    private SettingsEnum selectSetting() {
        int index = 0;
        // Prevent index 0 (TELEMETRY_ENABLED) of SettingsEnum from being returned.
        while (index == 0) {
            index = new Random().nextInt(SettingsEnum.values().length);
        }
        return SettingsEnum.values()[index];
    }

    /**
     * Select a random difficulty to be used in a SettingsChangeEvent.
     * This value only needs to be a string, as its purpose is for display
     * in the decision log of the telemetry app.
     *
     * @return a difficulty at random.
     */
    private String selectDifficultyString() {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        Random r = new Random();
        return difficulties[r.nextInt(difficulties.length)];
    }

    /**
     * Selects a random value within a given range for a chosen setting.
     * Each setting has its own minimum and maximum possible value in this script.
     * Calculation = min + new Random().nextInt(max - min + 1)
     *
     * @param setting the setting for which the value is being calculated.
     *
     * @return the value to change the setting to.
     */
    private Number selectSettingValue(SettingsEnum setting) {
        switch (setting) {
            case PLAYER_MAX_HEALTH:
                return 50 + new Random().nextInt(200 - 50 + 1);
            case SHOP_ITEM_COUNT:
                return 1 + new Random().nextInt(5 - 1 + 1);
            case ENEMY_DAMAGE_MULTIPLIER:
                return Math.round(
                        (0.25f + new Random().nextFloat(3.0f - 0.25f + 1.0f)) * 100f) / 100f;
            case ENEMY_MAX_HEALTH_MULTIPLIER:
                return Math.round(
                        (1.0f + new Random().nextFloat(3.0f - 1.0f + 1.0f)) * 100f) / 100f;
            case STARTING_LIVES:
                return 1 + new Random().nextInt(5 - 1 + 1);
            case MAX_MAGIC:
                return 50 + new Random().nextInt(200 - 50 + 1);
            case MAGIC_REGEN_RATE:
                return 5 + new Random().nextInt(30 - 5 + 1);
            default:
                return -1;
        }
    }

    public static void main(String[] args) throws AuthenticationException {
        SeededEventsGenerator g = new SeededEventsGenerator();
        AuthenticationResult result;

        // Each user has three sessions - one for each difficulty.
        int totalUsers = 50;
        g.generateUserIDs(totalUsers);

        for (int i = 0; i < g.userIDs.size(); i++) {
            
            // (Mock) authentication of each user
            result = new AuthenticationResult(
                "User " + g.userIDs.get(i),
                g.userIDs.get(i),
                RoleEnum.PLAYER); 
            g.settings.loginWithResult(result);

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

            // After a user has completed three sessions, invoke a random settings change.
            // Ending a simulation resets the telemetry listener's destination file,
            // so we must set it again before invoking this event.
            g.telemetryListener.setDestinationFile(DESTINATION_FILE);
            SettingsEnum settingToChange = g.selectSetting();
            g.telemetryListener.onSettingsChange(
                    new SettingsChangeEvent(
                            g.settings.getUserID(),
                            g.timeManager.getCurrentTime(),
                            settingToChange,
                            g.selectDifficultyString() + ": "
                                    + g.selectSettingValue(settingToChange).toString(),
                            "Seeded SettingsChangeEvent"
                    )
            );
        }
        // Reset the telemetry listener's destination file once seeding is complete.
        g.telemetryListener.resetDestinationFile();
    }
}