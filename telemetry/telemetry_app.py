"""
Docstring for telemetry.telemetry_app

This file is the main entry point for the telemetry application.
The main() function instantiates the GUI, and runs th main loop.
"""

import tkinter as tk
import sys
from tkinter import messagebox
import ctypes
from gui.gui import TelemetryAppGUI

def isAdmin() -> bool:
    try:
        is_admin = ctypes.windll.shell32.IsUserAnAdmin() != 0
    except Exception as e:
        is_admin = False
    return is_admin

def main() -> None:
    if not isAdmin():
        root = tk.Tk()
        root.withdraw()
        messagebox.showwarning(
            title="Warning",
            message="Not Running as Administrator\n\n"
                    "This application requires administrator privileges to function correctly.")
        root.destroy()
        sys.exit(1)
        root.destroy()
    app = TelemetryAppGUI()
    app.mainloop()

if __name__ == '__main__':
    main()
