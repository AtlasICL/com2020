#!/bin/bash
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
BUILD="$ROOT/build/macos"
STAGING="$BUILD/staging"

GAME_JAR="wizard-quest-0.1.1.jar"
GAME_MAIN="wizardquest.ui.GameRunPage"
APP_VERSION="1.0"
PKG_ID="com.thespecialcharacters.wizardquest"

if [ "$1" = "clean" ]; then
    echo "Cleaning build artifacts..."
    rm -rf "$ROOT/build"
    rm -rf "$ROOT/game/target"
    rm -rf "$ROOT/telemetry/build" "$ROOT/telemetry/dist" "$ROOT/telemetry/TelemetryViewer.spec"
    find "$ROOT/telemetry" -name __pycache__ -type d -exec rm -rf {} + 2>/dev/null || true
    echo "Done."
    exit 0
fi

rm -rf "$BUILD"
mkdir -p "$STAGING"
# jar + dependencies
echo "[1/3] Building game JAR..."
cd "$ROOT/game"
mvn clean package dependency:copy-dependencies \
    -DoutputDirectory=target/lib -DincludeScope=runtime \
    -DskipTests -q
echo "[2/3] Creating .app bundles..."

# wizardquest
jpackage \
    --type app-image \
    --input target \
    --main-jar "$GAME_JAR" \
    --main-class "$GAME_MAIN" \
    --name WizardQuest \
    --app-version "$APP_VERSION" \
    --java-options "--module-path \$APPDIR/lib --add-modules javafx.controls,javafx.graphics,javafx.base" \
    --java-options "-DOIDC_CLIENT_ID=<>" \
    --java-options "-DOIDC_CLIENT_SECRET=<SECRET>" \
    --dest "$BUILD"

cp -R "$BUILD/WizardQuest.app" "$STAGING/WizardQuest.app"
GAME_BIN="$STAGING/WizardQuest.app/Contents/MacOS"
GAME_APP="$STAGING/WizardQuest.app/Contents/app"
mv "$GAME_BIN/WizardQuest" "$GAME_BIN/WizardQuest_bin"
mv "$GAME_APP/WizardQuest.cfg" "$GAME_APP/WizardQuest_bin.cfg"
cat > "$GAME_BIN/WizardQuest" << 'WRAPPER'
#!/bin/bash
export OIDC_CLIENT_ID=""
export OIDC_CLIENT_SECRET=""
DIR="$(dirname "$(dirname "$(dirname "$(cd "$(dirname "$0")" && pwd)")")")"
cd "$DIR/game"
exec "$(cd "$(dirname "$0")" && pwd)/WizardQuest_bin" "$@"
WRAPPER
chmod +x "$GAME_BIN/WizardQuest"

# --- TelemetryViewer.app ---
cd "$ROOT/telemetry"
if [ ! -d venv ]; then
    python3 -m venv venv
fi
source venv/bin/activate
pip3 install -r requirements.txt pyinstaller --quiet
python3 -m PyInstaller --noconfirm --onedir --windowed --name TelemetryViewer \
    --add-data "auth:auth" --add-data "logins_file.json:." \
    --add-data "core:core" --add-data "gui:gui" \
    telemetry_app.py
deactivate

cp -R dist/TelemetryViewer.app "$STAGING/TelemetryViewer.app"
TELEM_BIN="$STAGING/TelemetryViewer.app/Contents/MacOS"
mv "$TELEM_BIN/TelemetryViewer" "$TELEM_BIN/TelemetryViewer_bin"
cat > "$TELEM_BIN/TelemetryViewer" << 'WRAPPER'
#!/bin/bash
export OIDC_CLIENT_ID=""
export OIDC_CLIENT_SECRET=""
DIR="$(dirname "$(dirname "$(dirname "$(cd "$(dirname "$0")" && pwd)")")")"
mkdir -p "$DIR/telemetry/auth"
cd "$DIR/telemetry"
exec "$(cd "$(dirname "$0")" && pwd)/TelemetryViewer_bin" "$@"
WRAPPER
chmod +x "$TELEM_BIN/TelemetryViewer"
rm -rf "$ROOT/telemetry/build" "$ROOT/telemetry/dist" "$ROOT/telemetry/TelemetryViewer.spec"

# installer pkg
echo "[3/3] Packaging installer..."

mkdir -p "$STAGING/game" "$STAGING/telemetry" "$STAGING/event_logs"
cp "$ROOT/game/settings_file.json"      "$STAGING/game/"      2>/dev/null || true
cp "$ROOT/telemetry/logins_file.json"   "$STAGING/telemetry/" 2>/dev/null || true
cp "$ROOT/event_logs/"*.json            "$STAGING/event_logs/" 2>/dev/null || true

pkgbuild --analyze --root "$STAGING" "$BUILD/component.plist"
plutil -replace 0.BundleIsRelocatable    -bool false "$BUILD/component.plist"
plutil -replace 0.BundleIsVersionChecked -bool false "$BUILD/component.plist"
plutil -replace 1.BundleIsRelocatable    -bool false "$BUILD/component.plist"
plutil -replace 1.BundleIsVersionChecked -bool false "$BUILD/component.plist"

SCRIPTS="$BUILD/scripts"
mkdir -p "$SCRIPTS"
cat > "$SCRIPTS/postinstall" << 'POSTINSTALL'
#!/bin/bash
chmod -R a+rw "/Applications/WizardQuest/game" \
              "/Applications/WizardQuest/telemetry" \
              "/Applications/WizardQuest/event_logs" 2>/dev/null || true
POSTINSTALL
chmod +x "$SCRIPTS/postinstall"

pkgbuild \
    --root "$STAGING" \
    --component-plist "$BUILD/component.plist" \
    --scripts "$SCRIPTS" \
    --identifier "$PKG_ID" \
    --version "$APP_VERSION" \
    --install-location "/Applications/WizardQuest" \
    "$BUILD/WizardQuestInstaller.pkg"

echo ""
echo "Done: $BUILD/WizardQuestInstaller.pkg"
