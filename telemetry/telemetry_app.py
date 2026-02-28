"""
Docstring for telemetry.telemetry_app

This file is the main entry point for the telemetry application.
The main() function instantiates the GUI, and runs th main loop.
"""

from gui.gui import TelemetryAppGUI

def main() -> None:
    app = TelemetryAppGUI()
    app.mainloop()

if __name__ == '__main__':
    main()