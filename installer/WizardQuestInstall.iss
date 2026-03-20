[Setup]
AppName=WizardQuest Suite
AppVersion=1.0
DefaultDirName={pf}\WizardQuest
DefaultGroupName=WizardQuest
OutputDir=output
OutputBaseFilename=WizardQuestInstaller
Compression=lzma
SolidCompression=yes

[Tasks]
Name: "desktopicon"; Description: "Create a desktop shortcut"; GroupDescription: "Additional icons:"

[Files]
Source: "..\game\target\image\*"; DestDir: "{app}\game"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\game\settings_file.json"; DestDir: "{app}\game"; Flags: ignoreversion onlyifdoesntexist
Source: "..\telemetry\telemetry_app.exe"; DestDir: "{app}\telemetry"; Flags: ignoreversion
Source: "..\telemetry\logins_file.json"; DestDir: "{app}\telemetry"; Flags: ignoreversion onlyifdoesntexist
Source: "..\telemetry\auth\*"; DestDir: "{app}\telemetry\auth"; Excludes: "__pycache__\*,*.pyc"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\event_logs\example_events.json"; DestDir: "{app}\event_logs"; Flags: ignoreversion onlyifdoesntexist
Source: "..\event_logs\simulation_events.json"; DestDir: "{app}\event_logs"; Flags: ignoreversion onlyifdoesntexist
Source: "..\event_logs\telemetry_events.json"; DestDir: "{app}\event_logs"; Flags: ignoreversion onlyifdoesntexist

[Icons]
Name: "{group}\WizardQuest"; Filename: "{app}\game\bin\javaw.exe"; Parameters: "-m wizard.quest/wizardquest.ui.GameRunPage"; WorkingDir: "{app}\game"
Name: "{group}\Telemetry App"; Filename: "{app}\telemetry\telemetry_app.exe"; WorkingDir: "{app}\telemetry"
Name: "{commondesktop}\WizardQuest"; Filename: "{app}\game\bin\javaw.exe"; Parameters: "-m wizard.quest/wizardquest.ui.GameRunPage"; WorkingDir: "{app}\game"; Tasks: desktopicon
