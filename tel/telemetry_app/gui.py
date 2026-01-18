import tkinter as tk
from tkinter import ttk, messagebox
import tkinter.font as tkfont

# Settings for main/entry window
class GUI_SELECTION_settings:
    WINDOW_TITLE: str = "Telemetry App"
    WINDOW_GEOMETRY: str = "600x500"
    FONT_FAMILY: str = "Arial"
    FONT_SIZE: int = 14
    DATA_ENTRY_FONT_SIZE: int = 12
    BACKGROUND_COLOR: str = "#f5f0e1"  # Light brown/cream background

# Settings for secondary tab
class GUI_RESULTS_settings:
    WINDOW_TITLE: str = "Some tab title"
    WINDOW_GEOMETRY: str = "600x600"
    FONT_FAMILY: str = "Arial"
    FONT_SIZE: int = 14
    HEADER_COLOR: str = "#3a7ca5"      # Blue header
    CONTAINER_BG: str = "#f5f5f5"      # Light gray background
    CONTAINER_BORDER: str = "#dcdcdc"  # Border color
    BLOCKS_BG: str = "#ffffff"         # White background for blocks text
    WEIGHT_COLOR: str = "#2c666e"      # Teal for weight information
    BACKGROUND_COLOR: str = "#f5f0e1"  # Light brown/cream background matching selection window


class TelemetryAppGUI(tk.Tk):
    def __init__(self) -> None:
        super().__init__()
        self.title(GUI_SELECTION_settings.WINDOW_TITLE)
        self.geometry(GUI_SELECTION_settings.WINDOW_GEOMETRY)
        self.configure(background=GUI_SELECTION_settings.BACKGROUND_COLOR)

        style = ttk.Style(self)
        try:
            style.theme_use("clam")
        except tk.TclError:
            style.theme_use(style.theme_names()[0])
        style.configure(
            '.', 
            font=(
                GUI_SELECTION_settings.FONT_FAMILY, 
                GUI_SELECTION_settings.FONT_SIZE
            ), 
            padding=6
        )
        
        # Configure ttk frame background
        style.configure(
            'TFrame', 
            background=GUI_SELECTION_settings.BACKGROUND_COLOR
        )
        style.configure(
            'TLabel', 
            background=GUI_SELECTION_settings.BACKGROUND_COLOR
        )
        style.configure(
            'TButton', 
            background=GUI_SELECTION_settings.BACKGROUND_COLOR
        )
        entry_font = tkfont.Font(
            family=GUI_SELECTION_settings.FONT_FAMILY, 
            size=GUI_SELECTION_settings.DATA_ENTRY_FONT_SIZE
        )