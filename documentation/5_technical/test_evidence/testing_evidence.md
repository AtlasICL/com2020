# Testing Evidence

## Automated Test Suite

### Description
The automated test suite for our prototype contains 41 tests. These have been implemented using JUnit (v5.10.1),
and can be executed using a single Maven command - this can be found in our deployment guide.

The suite contains both unit and integration tests. Unit tests are used to validate the logic of different methods
across different modules of our code. These are isolated and do not write to any files. Integration tests are used to
validate relevant behaviours that involve manually calling multiple methods, as well as reading to and writing from
files. By utilising JUnit's `@TempDir` feature, this allows I/O with temporary JSON files instead of those used for
production, which prevents any risk of corruption to the production files during testing. Furthermore, the use of
JUnit's `@BeforeEach` and `@AfterEach` features for setup and cleanup respectively ensure that each test is independent,
fair and responsible.

### Recording

The execution of the whole automated test suite can be found [here](https://youtu.be/g6tMS7hZfqw).

## Manual End-to-End Test Suite

### Test 1: Player is authenticated to game app by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here](https://youtu.be/W4eMjM-NVRY).

#### Screenshots

From the title screen, click the 'Login with SSO' button.

<img src="images/1_title.png" alt="Image" width="600">

This opens a new tab in the user's web browser, prompting sign-in with a Google account.

<img src="images/1_google_auth.png" alt="Image" width="600">

Once signed in, this screen will appear, indicating successful authentication.

<img src="images/1_login_complete.png" alt="Image" width="600">

This is reflected in the game app, where the user is now in the game's main menu.

<img src="images/1_menu.png" alt="Image" width="600">

### Test 2: Running game simulations in the game app works correctly

#### Recording

The recording of this test can be found [here](https://youtu.be/181G4iqBl24).

#### Screenshots

Before invoking any simulated runs, we can see that the file 'simulation_events.json' is empty.

<img src="images/2_file_initial.png" alt="Image" width="600">

From the main menu, click the 'Run Simulations' button, which produces this pop-up on success.

<img src="images/2_simulations_executed.png" alt="Image" width="600">

This is reflected in 'simulation_events.json', which is now populated with sequences of telemetry events.

<img src="images/2_file_outcome.png" alt="Description" width="600">

### Test 3: Players cannot change settings parameters in the game app

#### Recording

The recording of this test can be found [here](https://youtu.be/mLBafPQSsVA).

#### Screenshots

From the main menu, click the 'Settings' button.

<img src="images/1_menu.png" alt="Description" width="600">

This opens the following screen, where you can see the user's role of Player displayed at the top.

<img src="images/3_settings_screen.png" alt="Description" width="600">

The table of values is greyed out - the user cannot modify these as Players do not have permissions to do so. The same
policy applies to the justification text box.

### Test 4: User can toggle telemetry on and off

#### Recording

The recording of this test can be found [here](https://youtu.be/MtjofZ5Zi88).

#### Screenshots

From the Settings screen, we can see that telemetry is toggled to 'ON'.

<img src="images/4_settings_screen_telemetry_enabled.png" alt="Description" width="600">

This is reflected in 'logins_file.json', where the authenticated user has the associated Boolean value as true.

<img src="images/4_file_initial.png" alt="Description" width="600">

Click the 'Toggle Telemetry' button to turn it off.

<img src="images/4_settings_screen_telemetry_disabled.png" alt="Description" width="600">

This is reflected in 'logins_file.json', where the authenticated user now has the associated Boolean value as false.

<img src="images/4_file_outcome.png" alt="Description" width="600">

### Test 5: Developers can change settings parameters in the game app

#### Recording

The recording of this test can be found [here](https://youtu.be/RoZk-zCm7o8).

#### Screenshots

From the main menu, click the 'Settings' button.

<img src="images/1_menu.png" alt="Description" width="600">

This opens the Settings screen, where you can see the user's role of Developer displayed at the top.

<img src="images/5_developer_shown.png" alt="Description" width="600">

Unlike the Player, the table of values and justification box are editable thanks to the user's role of Developer.

Before changing any settings, we can see that the file 'telemetry_events.json' is empty.

<img src="images/5_file_initial.png" alt="Description" width="600">

Change the starting lives for the Easy difficulty to 10, and give a placeholder justification.

<img src="images/5_settings_change.png" alt="Description" width="600">

Clicking the 'Save' button displays the text 'Settings updated', indicating success.

<img src="images/5_settings_updated_with_change.png" alt="Description" width="600">

This is reflected in 'telemetry.events.json', where a SettingsChangeEvent with the input setting and justification
has been written.

<img src="images/5_file_outcome.png" alt="Description" width="600">

### Test 6: Developers can change the roles of other users in the game app

#### Recording

The recording of this test can be found [here](https://youtu.be/Kq1uDihGg4w).

#### Screenshots

From the authenticated user's settings page, it is clearly displayed that they have the role Developer.

<img src="images/5_developer_shown.png" alt="Description" width="600">

In 'logins_file.json', we can initially see that this user has the role of Player.

<img src="images/6_file_initial.png" alt="Description" width="600">

From the main menu, click the 'Manage Roles' button.

<img src="images/6_menu.png" alt="Description" width="600">

This opens the following screen, where you can see a list of all users with their corresponding roles.

Using the drop-down menu, change the aforementioned user's role to Developer.

<img src="images/6_roles_screen_dropdown.png" alt="Description" width="600">

A message 'Role Updated' will appear below the list, indicating success.

<img src="images/6_roles_screen_updated.png" alt="Description" width="600">

This is reflected in 'logins_file.json', where this user now has the role of Developer.

<img src="images/6_file_outcome.png" alt="Description" width="600">

### Test 7: Encounter is started and failed with lives remaining

#### Recording

The recording of this test can be found [here](https://youtu.be/ZI6MhQegde0).

#### Screenshots

In this encounter, the user currently has three lives.

<img src="images/7_mid_battle.png" alt="Description" width="600">

After attacking one another, the user has been defeated by the enemy.

They now have two lives. Their health and magic points, as well as the enemy's health, are also reset.

<img src="images/7_new_life.png" alt="Description" width="600">

This is reflected in 'telemetry.events.json', where a NormalEncounterStartEvent and a NormalEncounterFailEvent have
been written one after the other.

<img src="images/7_file_outcome.png" alt="Description" width="600">

### Test 8: Encounter is failed with no lives remaining

#### Recording

The recording of this test can be found [here](https://youtu.be/vrwo8pT0Jz8).

#### Screenshots

In this encounter, the user currently has one life remaining.

<img src="images/8_mid_battle.png" alt="Description" width="600">

After attacking one another, the user has been defeated by the enemy.

They now have zero lives left, and their run is over.

<img src="images/8_run_complete.png" alt="Description" width="600">

This is reflected in 'telemetry.events.json', where a NormalEncounterFailEvent and EndSessionEvent have
been written one after the other to show death and the end of a run.

<img src="images/8_file_outcome.png" alt="Description" width="600">

### Test 9: Encounter is completed

#### Recording

The recording of this test can be found [here](https://youtu.be/W-RsZ1wqqzE).

#### Screenshots

In 'telemetry_events.json', we can see that no events have yet been written to file.

<img src="images/9_file_initial.png" alt="Description" width="600">

From the main menu, start a new game and select a difficulty which takes us to the battle screen.

<img src="images/9_battle_screen.png" alt="Description" width="600">

In 'telemetry_events.json', we can now see that a StartSessionEvent and a NormalEncounterStartEvent have been
written to file to indicate this.

<img src="images/9_file_outcome_1.png" alt="Description" width="600">

After a few attack exchanges, the enemy has died and the encounter has been won, redirecting us to the shop.

<img src="images/9_shop.png" alt="Description" width="600">

In 'telemetry_events.json', we can now see that a NormalEncounterCompleteEvent and GainCoinEvent have been written
to file to indicate this.

<img src="images/9_file_outcome_2.png" alt="Description" width="600">

### Test 10: Only upgrades that are affordable may be purchased in the shop

#### Recording

The recording of this test can be found [here](https://youtu.be/IrsAIoJWbFs).

#### Screenshots

In the Shop screen, it is clearly displayed that the user has 25 coins.

<img src="images/10_shop.png" alt="Description" width="600">

Purchase an upgrade for 20 coins. This succeeds and reduces the user's coins by the relevant amount.

<img src="images/10_purchase_success.png" alt="Description" width="600">

This is reflected in 'telemetry_events.json', where a BuyUpgradeEvent has been written to indicate this.

<img src="images/10_file_outcome_1.png" alt="Description" width="600">

Now, we attempt to purchase an upgrade that is unaffordable. This fails with an error message output to screen.

<img src="images/10_purchase_fail.png" alt="Description" width="600">

Since the purchase failed, no update is made to 'telemetry_events.json'.

<img src="images/10_file_outcome_1.png" alt="Description" width="600">

Leave the shop, starting a new encounter.

<img src="images/10_new_encounter.png" alt="Description" width="600">

In 'telemetry_events.json', we can now see that a NormalEncounterStartEvent has been written to file to indicate the
start of the next level.

<img src="images/10_file_outcome_2.png" alt="Description" width="600">

### Test 11: Quit Run button ends the current session

#### Recording

The recording of this test can be found [here](https://youtu.be/cLIT9aTcVUg).

#### Screenshots

Start a new session, which opens the battle screen.

<img src="images/11_new_encounter.png" alt="Description" width="600">

In 'telemetry_events.json', we can now see that a StartSessionEvent and a NormalEncounterStartEvent have been written
to file to indicate the start of a new run.

<img src="images/11_file_outcome_1.png" alt="Description" width="600">

In the battle screen, click the 'Quit Run' button to end the run. This opens the 'Run Complete' screen.

<img src="images/11_run_complete.png" alt="Description" width="600">

In 'telemetry_events.json', we can now see that a EndSessionEvent has been written
to file to indicate the start of a new run.

<img src="images/11_file_outcome_2.png" alt="Description" width="600">

### Test 12: Player authentication to telemetry app is blocked

#### Recording

The recording of this test can be found [here](https://youtu.be/KvNQhScbSKM).

#### Screenshots

In 'logins_file.json', we can see that the user has the role of Player.

<img src="images/12_file.png" alt="Image" width="600">

From the title screen, click the 'Sign in with Google' button.

<img src="images/12_title.png" alt="Image" width="600">

This opens a new tab in the user's web browser, prompting sign-in with a Google account.

<img src="images/12_google_auth.png" alt="Image" width="600">

Once signed in, this pop-up will appear, indicating failed authentication.

<img src="images/12_auth_blocked.png" alt="Image" width="600">

### Test 13: Developer is authenticated to telemetry app by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here](https://youtu.be/JypFc2JmUEU).

#### Screenshots

In 'logins_file.json', we can see that the user has the role of Developer.

<img src="images/13_file.png" alt="Image" width="600">

From the title screen, click the 'Sign in with Google' button.

<img src="images/12_title.png" alt="Image" width="600">

This opens a new tab in the user's web browser, prompting sign-in with a Google account.

<img src="images/12_google_auth.png" alt="Image" width="600">

Once signed in, the main menu of the telemetry app will appear, indicating successful authentication.

<img src="images/13_authenticated.png" alt="Image" width="600">

### Test 14: Designer is authenticated to telemetry app by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here](https://youtu.be/f8TxcP28hsQ).

#### Screenshots

In 'logins_file.json', we can see that the user has the role of Designer.

<img src="images/14_file.png" alt="Image" width="600">

From the title screen, click the 'Sign in with Google' button.

<img src="images/12_title.png" alt="Image" width="600">

This opens a new tab in the user's web browser, prompting sign-in with a Google account.

<img src="images/12_google_auth.png" alt="Image" width="600">

Once signed in, the main menu of the telemetry app will appear, indicating successful authentication.

<img src="images/13_authenticated.png" alt="Image" width="600">

### Test 15: All telemetry app views are functional

#### Recording

The recording of this test can be found [here](https://youtu.be/BMOg9SCdnvM).

#### Screenshots

Our seeded telemetry dataset, stored under 'Example data' in the telemetry app, will be used for these tests.

<img src="images/15_dropdown.png" alt="Image" width="600">

All standard views show a graph of this style.

<img src="images/15_view_standard.png" alt="Image" width="600">

Suggestions shows a table of parameter balancing suggestions for the users, with issues highlighted by level and
difficulty.

<img src="images/15_suggestions.png" alt="Image" width="600">

Decision log keeps record of all SettingsChangeEvents that impact design parameters.

<img src="images/15_decision_log.png" alt="Image" width="600">

All compare by difficulty views show a graph of this style.

<img src="images/15_view_difficulty.png" alt="Image" width="600">

All compare by time views show a graph of this style.

<img src="images/15_view_time.png" alt="Image" width="600">

All compare by coin hold duration views show a graph of this style.

<img src="images/15_view_coin.png" alt="Image" width="600">

### Test 16: Telemetry data is exported to CSV

#### Recording

The recording of this test can be found [here](https://youtu.be/hIFxVSJ2lVc).

#### Screenshots

From the main menu, with Example Data selected in the drop-down, click the 'Export data to CSV' button. This will
open the following window.

<img src="images/16_save.png" alt="Image" width="600">

Opening this new .csv file reflects all telemetry data stored in the JSON file, ordered by event type.

<img src="images/16_csv.png" alt="Image" width="600">

### Test 17: Telemetry data reset wipes the relevant JSON file

#### Recording

The recording of this test can be found [here](https://youtu.be/Dwqo5ilQ3wU).

#### Screenshots

From looking at each view in our telemetry app, we can see that example data is populated.

<img src="images/17_before_reset.png" alt="Image" width="600">

From the main menu, with Example Data selected in the drop-down, click the 'Reset data' button. This will
open the following confirmation window.

<img src="images/17_reset.png" alt="Image" width="600">

Now, looking at each view in our telemetry app, we can see that example data has been wiped completely.

<img src="images/17_after_reset.png" alt="Image" width="600">

This is reflected in 'example_data.json', which is now empty.

<img src="images/17_file_outcome.png" alt="Image" width="600">