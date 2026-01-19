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
    BACKGROUND_COLOR: str = "#f5f0e1" 

# Settings for secondary tab
class GUI_RESULTS_settings:
    WINDOW_TITLE: str = "Some tab title"
    WINDOW_GEOMETRY: str = "600x600"
    FONT_FAMILY: str = "Arial"
    FONT_SIZE: int = 14
    HEADER_COLOR: str = "#3a7ca5"
    CONTAINER_BG: str = "#f5f5f5"
    CONTAINER_BORDER: str = "#dcdcdc"
    BLOCKS_BG: str = "#ffffff"
    WEIGHT_COLOR: str = "#2c666e"
    BACKGROUND_COLOR: str = "#f5f0e1"


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

        # Create scrollable frame for main content
        outer_frame = ttk.Frame(self)
        outer_frame.pack(fill=tk.BOTH, expand=True)
        
        # Add canvas for scrolling with background color
        canvas = tk.Canvas(
            outer_frame, 
            highlightthickness=0, 
            background=GUI_SELECTION_settings.BACKGROUND_COLOR
        )
        canvas.pack(
            side=tk.LEFT, 
            fill=tk.BOTH, 
            expand=True
        )
        
        # Add scrollbar
        scrollbar = ttk.Scrollbar(
            outer_frame, 
            orient=tk.VERTICAL, 
            command=canvas.yview
        )
        scrollbar.pack(
            side=tk.RIGHT, 
            fill=tk.Y
        )
        
        # Configure canvas
        canvas.configure(yscrollcommand=scrollbar.set)
        canvas.bind(
            '<Configure>', 
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )

