import tkinter as tk
from tkinter import ttk, messagebox
import seaborn as sns
from pathlib import Path

from core.logic import EventLogicEngine
from gui.plotting import PlotTab
from auth.auth import google_login, Role


ROOT_DIRECTORY = Path.cwd().parent
POLLING_INTERVAL_MS = 3000


class GUI_SETTINGS:
    """Stores settings for tkinter GUI appearance."""
    WINDOW_TITLE = "Telemetry App"   # Title of window.
    WINDOW_GEOMETRY = "800x600"      # Size at startup.
    WINDOW_MINIMUM_WIDTH = 600       # Minimum width of window.
    WINDOW_MINIMUM_HEIGHT = 450      # Minimum height of window. 
    FONT_FAMILY = "Arial"
    FONT_SIZE = 12
    BACKGROUND_COLOR = "#edd68f"


class TelemetryAppGUI(tk.Tk):
    def __init__(self) -> None:
        super().__init__()
        self.title(GUI_SETTINGS.WINDOW_TITLE)
        self.geometry(GUI_SETTINGS.WINDOW_GEOMETRY)
        self.configure(background=GUI_SETTINGS.BACKGROUND_COLOR)
        style = ttk.Style(self)
        style.theme_use("clam")
        self.file_name = ROOT_DIRECTORY / "event_logs" / "events.json"
        self.logic_engine = EventLogicEngine()
        self.authenticated = False
        self.current_user_name = None

        style.configure(
            ".",
            font=(GUI_SETTINGS.FONT_FAMILY, GUI_SETTINGS.FONT_SIZE),
            padding=6,
        )
        style.configure(
            "TFrame", 
            background=GUI_SETTINGS.BACKGROUND_COLOR
        )
        style.configure(
            "TLabel", 
            background=GUI_SETTINGS.BACKGROUND_COLOR
        )

        self.minsize(
            width=GUI_SETTINGS.WINDOW_MINIMUM_WIDTH, 
            height=GUI_SETTINGS.WINDOW_MINIMUM_HEIGHT
        )

        self.notebook = ttk.Notebook(self)
        self.notebook.grid(row=0, column=0, sticky="nsew")
        self.grid_rowconfigure(0, weight=1)
        self.grid_columnconfigure(0, weight=1)

        self.tab_home = ttk.Frame(self.notebook)
        self.tab_funnel = ttk.Frame(self.notebook)
        self.tab_spike = ttk.Frame(self.notebook)
        self.tab_curves = ttk.Frame(self.notebook)
        self.tab_fairness = ttk.Frame(self.notebook)
        self.tab_suggestions = ttk.Frame(self.notebook)

        self.notebook.add(self.tab_home, text="Home")

        self.make_welcome_screen()

        sns.set_theme(style="dark", context="notebook")


    def make_welcome_screen(self):
        self.welcome_label = ttk.Label(
            self.tab_home,
            text=self.get_personalised_welcome_message(),
            justify="center"
        )
        self.welcome_label.pack(pady=(30, 15))

        self.sign_in_button = ttk.Button(
            self.tab_home,
            text="Sign in with Google",
            command=self.handle_sign_in
        )
        self.sign_in_button.pack(pady=(10, 20))


    def get_personalised_welcome_message(self) -> str:
        return "Welcome to the Telemetry App" if self.current_user_name is None else "Welcome to the Telemetry App, " + self.current_user_name


    def handle_sign_in(self):
        _, self.current_user_name, role = google_login() # TODO: DEAL WITH ROLE RETURN
        self.authenticated = True
        AUTHORISED_ROLES = [Role.DESIGNER, Role.DEVELOPER]
        if role in AUTHORISED_ROLES:
            self.sign_in_button.pack_forget()
            self.welcome_label.config(text=self.get_personalised_welcome_message())
            self.on_authenticated()
        else:
            # self.make_welcome_screen()
            messagebox.showerror(
                "Authorisation Error", 
                "Only Designers and Developers may access telemetry data"
            )


    def on_authenticated(self):
        self.switch_btn_text = tk.StringVar()
        self.switch_btn_text.set("Change to simulation data")

        switch_simulation_button = ttk.Button(
            self.tab_home,
            textvariable=self.switch_btn_text,
            command=self.toggle_file
        )
        switch_simulation_button.pack(pady=(10,20))

        reset_telemetry_button = ttk.Button(
            self.tab_home,
            text="Reset Telemetry Data",
            command=self.reset_telemetry
        )
        reset_telemetry_button.pack(pady=(10,20))

        self.notebook.add(self.tab_funnel, text="Funnel view")
        self.notebook.add(self.tab_spike, text="Difficulty spike")
        self.notebook.add(self.tab_curves, text="Health")
        self.notebook.add(self.tab_fairness, text="Coins")
        self.notebook.add(self.tab_suggestions, text="Suggestions")

        self.tab_spike.rowconfigure(0, weight=1)
        self.tab_spike.columnconfigure(0, weight=1)

        self.funnel_plot = PlotTab(
            parent=self.tab_funnel,
            title="Funnel view",
            xlabel="Stage",
            ylabel="Players remaining after round",
        )
        self.spike_plot = PlotTab(
            parent=self.tab_spike,
            title="Difficulty spikes",
            xlabel="Stage",
            ylabel="Number of failures",
        )
        self.spike_suggestion = ttk.Label(
            self.tab_suggestions,
            text="Suggestion: " + self.generate_spike_suggestion()
        )
        self.spike_suggestion.pack(pady=(30, 15)) 
        self.curves_plot = PlotTab(
            parent=self.tab_curves,
            title="HP remaining by stage)",
            xlabel="Stage",
            ylabel="Average HP Remaining",
        )
        self.fairness_plot = PlotTab(
            parent=self.tab_fairness,
            title="Coins gained per stage",
            xlabel="Stage",
            ylabel="Coins gained",
        )

        self.refresh_all()
        self.do_auto_refresh()


    def do_auto_refresh(self, interval_ms=POLLING_INTERVAL_MS):
        """Use polling to refresh data for plots."""
        self.refresh_all()
        self.after(interval_ms, self.do_auto_refresh, interval_ms)


    def toggle_file(self):
        if self.switch_btn_text.get() == "Change to simulation data":
            self.switch_btn_text.set("Change to telemetry data")
            self.file_name = ROOT_DIRECTORY / "event_logs" / "simulation_events.json"
            self.refresh_all()
        else:
            self.switch_btn_text.set("Change to simulation data")
            self.file_name = ROOT_DIRECTORY / "event_logs" / "telemetry_events.json"
            self.refresh_all()


    def reset_telemetry(self):
        confirmed = messagebox.askyesno(
        title = "Switch Data Source",
        message = "Are you sure you want to reset telemetry data? " 
            + "All existing telemetry data will be lost")
        if confirmed:
            with open(ROOT_DIRECTORY / "event_logs" / "telemetry_events.json", 'w') as f:
                f.write('')


    def refresh_all(self):
        self.refresh_funnel_graph()
        self.refresh_coins_gained_plots()
        self.refresh_difficulty_spike_failure_plot()
        self.refresh_health_plots()


    def refresh_funnel_graph(self):
        """
        Refreshes the plot of players remaining per stage (referred to
        as funnel view).
        """
        self.logic_engine.categorise_events(self.file_name)
        funnel_data: dict[int, int] = self.logic_engine.funnel_view()
        self.funnel_plot.plot_line(
            funnel_data.keys(), 
            funnel_data.values(), 
            label="Players Remaining"
        )


    def refresh_difficulty_spike_failure_plot(self):
        """
        Refreshes the plots for difficulty spike in terms of number 
        of failures per stage.
        """
        self.logic_engine.categorise_events(self.file_name)
        spike_data: dict[int, int] = self.logic_engine.fail_difficulty_spikes()
        self.spike_plot.plot_line(
            spike_data.keys(), 
            spike_data.values(), 
            label="Difficulty spikes (by failure rate)"
        )
        self.spike_suggestion.config(text="Suggestion: " + self.generate_spike_suggestion())


    def get_average_dict_of_stage_dicts(
            self, 
            per_stage_data: list[dict[int, int]]
    ) -> dict[int, float]:
        """
        Helper function to get the average of a list of dictionaries
        which map stage numbers to values like HP remaining or coins
        gained.
        Example: Given a list of dictionaries of health per stage data,
        this function would return a dictionary which maps the stage
        number to the average of health at that stage. 
        
        :param per_stage_data: List of dictionaries which represent 
        "x per stage" data. 
        :type per_stage_data: list[dict[int, int]]
        :return: Average of the given data per stage. 
        :rtype: dict[int, float]
        """
        stages = range(1, 11)
        averages: dict[int, float] = {}
        for stage in stages:
            vals = [dictionary.get(stage, 0) 
                    for dictionary in per_stage_data]
            averages[stage] = (sum(vals) / len(vals)) if vals else 0.0
        return averages
    

    def refresh_health_plots(self):
        """
        Refreshes the plots of HP remaining per stage per difficulty.
        """
        self.logic_engine.categorise_events(self.file_name)
        health_by_difficulty = self.logic_engine.compare_health_per_stage_per_difficulty()

        series = []
        for difficulty, list_of_dicts in health_by_difficulty.items():
            averages = self.get_average_dict_of_stage_dicts(list_of_dicts)
            x = averages.keys()
            y = averages.values()
            series.append((x, y, str(difficulty.value)))

        self.curves_plot.plot_multi_line(series)


    def refresh_coins_gained_plots(self):
        """
        Refreshed the plots for average coins gained per stage per
        difficulty.
        """
        self.logic_engine.categorise_events(self.file_name)
        coins_by_difficulty = self.logic_engine.compare_coins_per_stage_per_difficulty()

        series = []
        for difficulty, list_of_dicts in coins_by_difficulty.items():
            averages = self.get_average_dict_of_stage_dicts(list_of_dicts)
            x = averages.keys()
            y = averages.values()
            series.append((x, y, str(difficulty.value)))

        self.fairness_plot.plot_multi_line(series)


    def generate_spike_suggestion(self):
        """
        Generates a difficulty change suggestion 
        for difficulty spikes.
        """
        stages = ""
        spikes = self.logic_engine.fail_difficulty_spikes()
        mean = sum(spikes.values())/len(spikes)
        for stage in spikes:
            if spikes[stage] > mean:
                stages += str(stage) + ", "
        if stages != "":
            return "High failure rate in " + stages + " consider increasing lives by 2."
        return "No suggestions available"

