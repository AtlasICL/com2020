[Setup]
AppName=WizardQuest Suite
AppVersion=1.0
DefaultDirName={pf}\WizardQuest
DefaultGroupName=WizardQuest
OutputDir=output
OutputBaseFilename=WizardQuestInstaller

[Files]
Source: "..\executables\WizardQuest\*"; DestDir: "{app}\WizardQuest"; Flags: recursesubdirs
Source: "..\executables\telemetry_app.exe"; DestDir: "{app}"

[Icons]
[Icons]
Name: "{group}\WizardQuest"; Filename: "{app}\WizardQuest\WizardQuest.exe"
Name: "{group}\Telemetry App"; Filename: "{app}\telemetry_app.exe"
Name: "{commondesktop}\WizardQuest"; Filename: "{app}\WizardQuest\WizardQuest.exe"