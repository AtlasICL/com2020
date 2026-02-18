# Manual End-to-End Test Suite

## Test 1: Player is authenticated by Google OAuth 2.0 OIDC

### Recording

The recording of this test can be found [here](https://www.youtube.com/watch?v=J7zUf3znLJk).

### Screenshots

When the game is run, this is the startup screen that appears in the command line.

<img src="images/startup_screen.png" alt="Startup Screen" width="600">

This opens a new tab in my default browser, as shown below:

<img src="images/google_sso.png" alt="Google OAuth" width="600">
<img src="images/google_review_policies.png" alt="Google Review Policies" width="600">

After entering my credentials, I am then shown the following screen indicating login success:

<img src="images/login_complete_screen.png" alt="Login Complete Screen" width="600">

This is reflected in the command line, where I am now clearly signed in.

<img src="images/main_menu.png" alt="Main Menu" width="600">