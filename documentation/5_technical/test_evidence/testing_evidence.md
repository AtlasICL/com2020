# Testing Evidence

## Automated Test Suite

### Description
The automated test suite for our prototype contains three unit tests and two integration tests. These have been
implemented using JUnit (v5.10.1), and can be executed using a single Maven command - this can be found in our
deployment guide.

These five tests validate the logic of our TelemetryListener class - which is fundamental to the core objective of
what our system aims to achieve. The test suite utilises JUnit's `@TempDir` feature which causes the tests to interact
with temporary JSON files where necessary - this prevents any risk of corruption to the JSON files to be used for
production. The concrete `TelemetryEvent` class primarily used for testing is `NormalEncounterStartEvent` - however, we
have also utilised JUnit's `@BeforeEach` and `@AfterEach` features to invoke a `StartSessionEvent` and `EndSessionEvent`
object before and after each event respectively. This is due to dynamic assignment of the SessionID field.

### Recording

TO BE REDONE ONCE ALL AUTOMATED TESTS ARE COMPLETE.

## Manual End-to-End Test Suite

### Test 1: Player is authenticated by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 2: Running game simulations in the GUI works correctly

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 3: Players cannot change settings parameters in the GUI

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 4: Developers can change settings parameters in the GUI

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 5: Encounter is started and failed with lives remaining

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 6: Encounter is started and failed with no lives remaining

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 7: Encounter is completed

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 8: Only upgrades that are affordable may be purchased in the shop

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 9: Quit Run button ends the current session

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 10: Player authentication to telemetry app is blocked

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 11: Developer is authenticated to telemetry app by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 12: Designer is authenticated to telemetry app by Google OAuth 2.0 OIDC

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 13: All telemetry app views are functional

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 14: Telemetry data is exported to CSV

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">

### Test 15: Telemetry data reset wipes the relevant JSON file

#### Recording

The recording of this test can be found [here]().

#### Screenshots

<img src="images/name.png" alt="Description" width="600">