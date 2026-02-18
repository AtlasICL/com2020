# Deployment Guide

## Prerequisites
_Please find our prerequisite configuration guides at the bottom of the page, if required._
- Java 17+
- Python 3.13+
- Maven

## Instructions for Running Game App

**NOTE**: For Windows machines, please use a PowerShell terminal.

1. In the terminal, navigate into the `game` directory from root.
```
cd game
```
2. Run ONE of the following scripts, dependent on your OS - which will set environment variables for Google OAuth 2.0 OIDC.

**NOTE**: The CLIENT_ID and CLIENT_SECRET values cannot be publicly distributed. To obtain these for running the below script, please email _ea616@exeter.ac.uk_.

**Windows**
```
$env:OIDC_ISSUER="https://accounts.google.com"
$env:OIDC_CLIENT_ID=""
$env:OIDC_CLIENT_SECRET=""
```
**macOS or Linux**
```
export OIDC_ISSUER="https://accounts.google.com"
export OIDC_CLIENT_ID=""
export OIDC_CLIENT_SECRET=""
```
3. Run the following Maven command, which will execute the game.
```
mvn clean compile exec:java "-Dexec.mainClass=WizardQuest.GameUserInterface"
```

## Instructions for Running Telemetry App

**NOTE**: For Windows machines, please use a PowerShell terminal.

1. In the terminal, se the following command to navigate into the `telemetry` directory from root.
```
cd telemetry
```
2. Run ONE of the following scripts, dependent on your OS - which will set environment variables for Google OAuth 2.0 OIDC.

**NOTE**: The CLIENT_ID and CLIENT_SECRET values cannot be publicly distributed. To obtain these for running the below script, please email _ea616@exeter.ac.uk_.

**Windows**
```
$env:OIDC_ISSUER="https://accounts.google.com"
$env:OIDC_CLIENT_ID=""
$env:OIDC_CLIENT_SECRET=""
```
**macOS or Linux**
```
export OIDC_ISSUER="https://accounts.google.com"
export OIDC_CLIENT_ID=""
export OIDC_CLIENT_SECRET=""
```

3. Run ONE of the following scripts, dependent on your OS.

**Windows**
```
python -m venv venv
.\venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

**Linux**
```
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

**MacOS**
```
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

To deactivate on any OS, run `deactivate`.

4. Run the following command, which will execute the telemetry app.
```
python ./telemetry_app.py
```

## Instructions for Running Tests
1. In the terminal, navigate into the `game` directory from root.
```
cd game
```
2. Run the following Maven commands, which will execute all five automated tests in our suite.
```
mvn clean install
mvn test
```

## Configuration Guides
### Java 17+
**Windows**
1. Open Command Prompt with administrative privileges.
2. Run the following command:
```
winget install EclipseAdoptium.Temurin.21.JDK
```
3. Close and reopen Command Prompt with administrative privileges.
4. Run the following commands, which should print version numbers:
```
java -version
javac -version
```
5. Run the following command. If no value is returned, proceed to step 6. Otherwise, installation complete.
```
echo %JAVA_HOME%
```
6. Open Run (Win + R), and run the following command:
```
sysdm.cpl
```
7. Navigate to Advanced -> Environment Variables.
8. Under System Variables, click New.
9. Variable name: `JAVA_HOME`. Variable value: `C:\Program Files\Eclipse Adoptium\jdk-21.x.x`  (_- You'll need to replace this value with your actual filepath._)
10. Click OK.
11. Under System Variables, select Path.
12. Click Edit -> New, and add the following: `%JAVA_HOME%\bin`
13. Click OK on all windows and return to Step 3.

**macOS**
1. Navigate to https://adoptium.net/temurin/releases
2. Select either `JDK 25 - LTS`, `JDK 21 - LTS`, or `JDK 17 - LTS`.
3. Scroll down to macOS (third one down), and ensure that JDK and x64 are selected.
4. Download and run the installer marked .PKG.
5. Follow the instructions provided by the installer.
6. Open a terminal and run the following command:
```
java -version
```
7. Run the following command. If no value is returned, proceed to step 8. Otherwise, installation complete.
```
echo $JAVA_HOME
```
8. Run the following command and return to Step 6:
```
echo 'export JAVA_HOME=$(/usr/libexec/java_home)' >> ~/.zshrc
source ~/.zshrc
```

**Linux**
1. Open a terminal and run the following commands:
```
sudo apt update && sudo apt full-upgrade -y
sudo apt install -y openjdk-21-jdk
```
2. Run the following commands, which should print version numbers:
```
java -version
javac -version
```
3. Run the following command. If no value is returned, proceed to step 5. Otherwise, installation complete.
```
echo $JAVA_HOME
```
4. Run the following command, which will retrieve the filepath.
```
sudo update-alternatives --config java
```
5. Run the following command and return to Step 3: (_NOTE: You'll need to replace /usr/lib/jvm/java-21-openjdk-amd64 with your filepath retrieved in Step 5._)
```
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

### Python 3.13+
**Windows**
1. Navigate to https://www.python.org/downloads/windows
2. Download and run the installer.
3. IMPORTANT - Ensure that you check the box stating 'Add Python 3.x to PATH'.
4. Run the following command in the terminal, which should print version numbers:
```
python --version
```

**macOS**
1. Navigate to https://www.python.org/downloads/mac-osx/
2. Download and run the .pkg installer.
3. Run the following command in the terminal, which should print version numbers:
```
python3 --version
```

**Linux**
1. Open a terminal and run the following commands:
```
sudo apt update && sudo apt full-upgrade -y
sudo apt install -y python3 python3-pip
```
2. Run the following commands, which should print version numbers:
```
python3 --version
pip3 --version
```

### Maven
**Windows**
1. Navigate to https://maven.apache.org/download.cgi
2. Download the binary zip archive.
3. Open the folder and extract its contents to `C:\Program Files\Apache\Maven`.
4. Open Run (Win + R), and run the following command:
```
sysdm.cpl
```
5. Navigate to Advanced -> Environment Variables.
6. Under System Variables, click New.
7. Variable name: `MAVEN_HOME`. Variable value: `C:\Program Files\Apache\Maven`
8. Click OK.
9. Under System Variables, select Path.
10. Click Edit -> New, and add the following: `%MAVEN_HOME%\bin`
11. Run the following command in the terminal, which should print version numbers:
```
mvn -version
```

**macOS**
1. Navigate to https://maven.apache.org/download.cgi
2. Download the binary tar.gz file.
3. Open a Terminal and run the following commands (_NOTE: Replace 3.9.x with your actual version_):
```
sudo tar xzf apache-maven-3.9.x-bin.tar.gz -C /usr/local/
echo 'export MAVEN_HOME=/usr/local/apache-maven/apache-maven-3.9.x' >> ~/.zshrc
echo 'export PATH=$MAVEN_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```
4. Run the following command, which should print version numbers:
```
mvn -version
```

**Linux**
1. Open a terminal and run the following commands:
```
sudo apt update && sudo apt full-upgrade -y
sudo apt install -y maven
```
2. Run the following command, which should print version numbers:
```
mvn -version
```