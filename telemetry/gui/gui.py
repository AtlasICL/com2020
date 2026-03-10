"""
Docstring for telemetry.gui.gui

This module is responsible for running the Telemetry App GUI.

The GUI is implemented using tkinter.

This module is responsible for the logic of refreshing plot data.

The GUI logic lives in the TelemetryAppGUI class.
"""

import csv
import dataclasses

import tkinter as tk
from tkinter import ttk, messagebox
import seaborn as sns
from pathlib import Path

from core.logic import EventLogicEngine
from core.suggestions import SuggestionGenerator
from gui.plotting import PlotTab
from auth.auth import google_login, Role


ROOT_DIRECTORY: Path = Path.cwd().parent
EVENT_LOGS_DIRECTORY: str = "event_logs"
TELEMETRY_EVENTS_FILE: str = "telemetry_events.json"
SIMULATION_EVENTS_FILE: str = "simulation_events.json"

# Polling interval for refreshing data.
POLLING_INTERVAL_MS = 4000


class GUI_SETTINGS:
    """Stores settings for tkinter GUI appearance."""
    WINDOW_TITLE = "Telemetry App"   # Title of window.
    WINDOW_GEOMETRY = "1200x750"     # Size at startup.
    WINDOW_MINIMUM_WIDTH = 1000      # Minimum width of window.
    WINDOW_MINIMUM_HEIGHT = 700      # Minimum height of window.
    FONT_FAMILY = "Arial"            # Font for GUI.
    FONT_SIZE = 14                   # Font size.
    BUTTON_FONT_SIZE = 16            # Bigger font size for buttons.
    BACKGROUND_COLOR = "#edd68f"     # Background colour for the window.


class TelemetryAppGUI(tk.Tk):
    def __init__(self) -> None:
        super().__init__()
        self.title(GUI_SETTINGS.WINDOW_TITLE)
        self.geometry(GUI_SETTINGS.WINDOW_GEOMETRY)

        self.file_name = ROOT_DIRECTORY / EVENT_LOGS_DIRECTORY \
            / TELEMETRY_EVENTS_FILE
        self.logic_engine = EventLogicEngine()
        self.suggestion_generator = SuggestionGenerator(self.logic_engine)
        self.authenticated = False
        self.current_user_name = None
        self.compare_by_difficulty = tk.BooleanVar(value=False)

        # Set GUI colours / font styles.
        self.configure(background=GUI_SETTINGS.BACKGROUND_COLOR)
        style = ttk.Style(self)
        style.theme_use("clam")
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

        # Set a minimum size for the window - this prevents users from
        # making the window much too small.
        self.minsize(
            width=GUI_SETTINGS.WINDOW_MINIMUM_WIDTH,
            height=GUI_SETTINGS.WINDOW_MINIMUM_HEIGHT
        )

        # We use notebook for the "tabs" structure of the window.
        self.notebook = ttk.Notebook(self)
        self.notebook.grid(row=0, column=0, sticky="nsew")
        self.grid_rowconfigure(0, weight=1)
        self.grid_columnconfigure(0, weight=1)

        # Create notebook tabs
        self.tab_home = ttk.Frame(self.notebook)
        self.tab_funnel = ttk.Frame(self.notebook)
        self.tab_spike = ttk.Frame(self.notebook)
        self.tab_curves = ttk.Frame(self.notebook)
        self.tab_fairness = ttk.Frame(self.notebook)
        self.tab_completion_time = ttk.Frame(self.notebook)
        self.tab_suggestions = ttk.Frame(self.notebook)
        self.tab_decision_log = ttk.Frame(self.notebook)

        # Only add the "Home" screen on startup.
        # The other tabs will be added if appropriate upon
        # authentication.
        self.notebook.add(self.tab_home, text="Home")
        self.make_welcome_screen()

        sns.set_theme(style="dark", context="notebook")


    def make_welcome_screen(self) -> None:
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


    def set_up_decision_log(self) -> None:
        """
        Sets up the decision log tab, with a scrollable log.
        """
        self.tab_decision_log.rowconfigure(0, weight=1)
        self.tab_decision_log.columnconfigure(0, weight=1)
        columns = ("timestamp", "setting", "value")
        self.decision_log_tree = ttk.Treeview(
            self.tab_decision_log,
            columns=columns,
            show="headings"
        )
        self.decision_log_tree.heading("timestamp", text="Timestamp")
        self.decision_log_tree.heading("setting", text="Setting")
        self.decision_log_tree.heading("value", text="Value")
        self.decision_log_tree.column("timestamp", width=200)
        self.decision_log_tree.column("setting", width=300)
        self.decision_log_tree.column("value", width=100)
        scrollbar = ttk.Scrollbar(
            self.tab_decision_log,
            orient="vertical",
            command=self.decision_log_tree.yview
        )
        self.decision_log_tree.configure(yscrollcommand=scrollbar.set)
        self.decision_log_tree.grid(row=0, column=0, sticky="nsew")
        scrollbar.grid(row=0, column=1, sticky="ns")


    def get_personalised_welcome_message(self) -> str:
        """
        Gets a personalised welcome message for the user, with the
        user's name (provided by their Google account).
        """
        return "Welcome to the Telemetry App" if self.current_user_name is None \
            else "Welcome to the Telemetry App, " + self.current_user_name


    def handle_sign_in(self) -> None:
        """
        Handles sign in.
        If the authenticated user's role is authorised to see telemetry,
        then the other tabs (with telemetry info) will appear.
        If the user's role is not authorised for viewing telemetry,
        then an error message will be shown indicating this, and the
        user will be returned to the Home tab, with the option to sign
        in again.
        """
        try:
            _, self.current_user_name, role = google_login()
        except KeyError as _:
            messagebox.showerror(
                "Configuration Error",
                "OIDC_CLIENT_ID and OIDC_CLIENT_SECRET environment " \
                "variables are not set."
            )
            return
        self.authenticated = True
        AUTHORISED_ROLES = [Role.DESIGNER, Role.DEVELOPER]
        if role in AUTHORISED_ROLES:
            self.sign_in_button.pack_forget()
            self.welcome_label.config(
                text=self.get_personalised_welcome_message()
            )
            self.on_authenticated()
        else:
            # Error message for non-developer / non-designer users.
            messagebox.showerror(
                "Authorisation Error",
                "Only Designers and Developers may access telemetry data."
            )


    def on_authenticated(self) -> None:
        self.data_source = tk.StringVar()
        self.data_source.set("Telemetry data")

        # Show a dropdown for switching telemetry source in the home page.
        data_source_dropdown = ttk.Combobox(
            self.tab_home,
            textvariable=self.data_source,
            values=["Telemetry data", "Simulation data"],
            state="readonly",
            font=(GUI_SETTINGS.FONT_FAMILY, GUI_SETTINGS.BUTTON_FONT_SIZE)
        )
        data_source_dropdown.bind("<<ComboboxSelected>>", self.toggle_file)
        data_source_dropdown.pack(pady=(10,20))

        # Show a button for exporting telemetry data to csv in the
        # home page.
        export_telemetry_button = ttk.Button(
            self.tab_home,
            text="Export data to csv",
            command=self.export_to_csv
        )
        export_telemetry_button.pack(pady=(10, 20))

        # Show a button for resetting telemetry data in the home page.
        reset_telemetry_button = ttk.Button(
            self.tab_home,
            text="Reset data",
            command=self.reset_telemetry
        )
        reset_telemetry_button.pack(pady=(10,20))

        # Add a global toggle for comparing by difficulty above the
        # notebook. This will toggle whether graphs show data for all
        # difficulties, or per-difficulty.
        control_frame = ttk.Frame(self)
        control_frame.grid(
            row=0,
            column=0,
            sticky="ew",
            padx=10,
            pady=(5, 0)
        )
        ttk.Checkbutton(
            control_frame,
            text="Compare by difficulty",
            variable=self.compare_by_difficulty,
            command=self.refresh_all,
        ).pack(side="left")
        self.notebook.grid(row=1, column=0, sticky="nsew")
        self.grid_rowconfigure(0, weight=0)
        self.grid_rowconfigure(1, weight=1)

        # Once the user is authenticated (since this function has been
        # called), we now show all the telemetry views.
        self.notebook.add(self.tab_funnel, text="Funnel")
        self.notebook.add(self.tab_spike, text="Difficulty spike")
        self.notebook.add(self.tab_curves, text="Health")
        self.notebook.add(self.tab_fairness, text="Coins")
        self.notebook.add(self.tab_completion_time, text="Time")
        self.notebook.add(self.tab_suggestions, text="Suggestions")
        self.notebook.add(self.tab_decision_log, text="Decision log")

        self.tab_spike.rowconfigure(0, weight=1)
        self.tab_spike.columnconfigure(0, weight=1)

        # Add the plots to their respective tabs.
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
            self.tab_suggestions
        )
        self.spike_suggestion.pack(pady=(30, 15))

        self.curves_plot = PlotTab(
            parent=self.tab_curves,
            title="HP remaining by stage",
            xlabel="Stage",
            ylabel="Average HP Remaining",
        )
        self.fairness_plot = PlotTab(
            parent=self.tab_fairness,
            title="Coins gained per stage",
            xlabel="Stage",
            ylabel="Coins gained",
        )
        self.completion_time_plot = PlotTab(
            parent=self.tab_completion_time,
            title="Average time to complete per stage",
            xlabel="Stage",
            ylabel="Time (seconds)",
        )

        # We also set up the decision log once authenticated.
        # (to view past balancing decisions)
        self.set_up_decision_log()

        self.refresh_all()
        self.do_auto_refresh()


    def do_auto_refresh(self, interval_ms: int = POLLING_INTERVAL_MS) -> None:
        """
        Use polling to refresh data for plots.

        :param interval_ms: The polling interval in milliseconds.
        :type interval_ms: int
        """
        self.refresh_all()
        self.after(interval_ms, self.do_auto_refresh, interval_ms)


    def toggle_file(self, _event=None) -> None:
        """
        Toggles between viewing telemetry data and simulation
        data.
        """
        if self.data_source.get() == "Simulation data":
            # Switch the data source
            self.file_name = ROOT_DIRECTORY / EVENT_LOGS_DIRECTORY \
                / SIMULATION_EVENTS_FILE
        else:
            # Switch the data source
            self.file_name = ROOT_DIRECTORY / EVENT_LOGS_DIRECTORY \
                / TELEMETRY_EVENTS_FILE
        self.refresh_all()


    def reset_telemetry(self) -> None:
        """
        Resets/erases the currently active data file (telemetry events 
        or simulation data). This action is permanent. 
        """
        source_name = self.data_source.get()
        confirmed: bool = messagebox.askyesno(
            title="Reset Data",
            message=f"Are you sure you want to reset {source_name.lower()}? "
                + "All existing data will be lost"
        )
        if not confirmed:
            return
        with open(self.file_name, 'w') as f:
            f.write('')
        self.refresh_all()


    def export_to_csv(self) -> None:
        """
        Exports the telemetry data to a csv file.
        """
        # Get the list of telemetry events from the logic engine.
        all_events = []
        self.logic_engine.categorise_events(self.file_name)
        for attr in self.logic_engine._attributes:
            for event in attr:
                event_type = type(event).__name__
                event_dict = dataclasses.asdict(event)
                event_dict["event_type"] = event_type
                all_events.append(event_dict)

        # If no events exist, return.
        if not all_events:
            return

        # Get the field names for each event type.
        fieldnames = list(dict.fromkeys(
            key for event in all_events for key in event
        ))

        # Write the telemetry events to csv
        with open("export.csv", 'w', newline='', encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(all_events)


    def refresh_all(self) -> None:
        """
        Refreshes data from events source file.
        Updates all graphs.
        """
        self.refresh_funnel_graph()
        self.refresh_coins_gained_plots()
        self.refresh_difficulty_spike_failure_plot()
        self.refresh_health_plots()
        self.refresh_completion_time_plot()
        self.refresh_suggestions()
        self.refresh_decision_log()


    def refresh_funnel_graph(self) -> None:
        """
        Refreshes the plot of players remaining per stage (referred to
        as funnel view).
        """
        self.logic_engine.categorise_events(self.file_name)
        if self.compare_by_difficulty.get():
            funnel_by_diff = self.logic_engine.funnel_view_per_difficulty()
            series = []
            for diff, data in funnel_by_diff.items():
                series.append((data.keys(), data.values(), str(diff.value)))
            self.funnel_plot.plot_multi_line(series)
        else:
            funnel_data: dict[int, int] = self.logic_engine.funnel_view()
            self.funnel_plot.plot_line(
                funnel_data.keys(),
                funnel_data.values(),
                label="Players Remaining"
            )


    def refresh_difficulty_spike_failure_plot(self) -> None:
        """
        Refreshes the plots for difficulty spike in terms of number
        of failures per stage.
        """
        self.logic_engine.categorise_events(self.file_name)
        if self.compare_by_difficulty.get():
            spikes_by_diff = self.logic_engine.fail_difficulty_spikes_per_difficulty()
            series = []
            for diff, data in spikes_by_diff.items():
                series.append((data.keys(), data.values(), str(diff.value)))
            self.spike_plot.plot_multi_line(series)
        else:
            spike_data: dict[int, int] = self.logic_engine.fail_difficulty_spikes()
            self.spike_plot.plot_line(
                spike_data.keys(),
                spike_data.values(),
                label="Difficulty spikes (by failure rate)"
            )
        self.spike_suggestion.config(
            text="Suggestion: " + \
            self.suggestion_generator.generate_spike_suggestion()
        )


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


    def refresh_health_plots(self) -> None:
        """
        Refreshes the plots of HP remaining per stage.
        Shows aggregate average by default, or per difficulty when
        the compare toggle is enabled.
        """
        self.logic_engine.categorise_events(self.file_name)
        health_by_difficulty = \
            self.logic_engine.compare_health_per_stage_per_difficulty()

        if self.compare_by_difficulty.get():
            series = []
            for difficulty, list_of_dicts in health_by_difficulty.items():
                averages = self.get_average_dict_of_stage_dicts(list_of_dicts)
                series.append((
                    averages.keys(),
                    averages.values(),
                    str(difficulty.value)
                ))
            self.curves_plot.plot_multi_line(series)
        else:
            all_dicts = []
            for list_of_dicts in health_by_difficulty.values():
                all_dicts.extend(list_of_dicts)
            averages = self.get_average_dict_of_stage_dicts(all_dicts)
            self.curves_plot.plot_line(
                averages.keys(),
                averages.values(),
                label="Avg HP Remaining"
            )


    def refresh_coins_gained_plots(self) -> None:
        """
        Refreshes the plots for average coins gained per stage.
        Shows aggregate average by default, or per difficulty when
        the compare toggle is enabled.
        """
        self.logic_engine.categorise_events(self.file_name)
        coins_by_difficulty = \
            self.logic_engine.compare_coins_per_stage_per_difficulty()

        if self.compare_by_difficulty.get():
            series = []
            for difficulty, list_of_dicts in coins_by_difficulty.items():
                averages = self.get_average_dict_of_stage_dicts(list_of_dicts)
                series.append((
                    averages.keys(),
                    averages.values(),
                    str(difficulty.value)
                ))
            self.fairness_plot.plot_multi_line(series)
        else:
            all_dicts = []
            for list_of_dicts in coins_by_difficulty.values():
                all_dicts.extend(list_of_dicts)
            averages = self.get_average_dict_of_stage_dicts(all_dicts)
            self.fairness_plot.plot_line(
                averages.keys(),
                averages.values(),
                label="Avg Coins Gained"
            )


    def refresh_suggestions(self) -> None:
        """
        Refreshes the suggestions generated.
        """
        suggestions = [
            self.suggestion_generator.generate_low_health_suggestion(),
            self.suggestion_generator.generate_high_health_suggestion(),
            self.suggestion_generator.generate_spike_suggestion(),
            self.suggestion_generator.generate_high_pass_rate_suggestion(),
            self.suggestion_generator.generate_low_coin_gain_suggestion(),
            self.suggestion_generator.generate_high_coin_gain_suggestion(),
            self.suggestion_generator.generate_slow_average_time_suggestion(),
            self.suggestion_generator.generate_fast_average_time_suggestion(),
        ]
        suggestion_text = "\n".join(s for s in suggestions if s)
        # Refresh the content of the tk label
        self.spike_suggestion.config(text="SUGGESTIONS:\n" + suggestion_text)


    def refresh_decision_log(self) -> None:
        """
        Refreshes the decision log tab with the latest settings
        change events, sorted by most recent first.
        """
        self.logic_engine.categorise_events(self.file_name)
        # Clear existing rows.
        for item in self.decision_log_tree.get_children():
            self.decision_log_tree.delete(item)
        # Insert settings change events sorted by timestamp.
        for event in self.logic_engine.get_settings_change_events():
            self.decision_log_tree.insert("", "end", values=(
                event.timestamp.strftime("%Y/%m/%d %H:%M:%S"),
                event.userID,
                event.setting.value,
                event.value,
            ))


    def refresh_completion_time_plot(self) -> None:
        """
        Refreshes the plot of average time to complete per stage.
        Shows aggregate average by default, or per difficulty when
        the compare toggle is enabled.
        """
        self.logic_engine.categorise_events(self.file_name)
        if self.compare_by_difficulty.get():
            time_by_diff = self.logic_engine \
                .average_time_to_complete_per_stage_per_difficulty()
            series = []
            for diff, data in time_by_diff.items():
                series.append(
                    (data.keys(), data.values(), str(diff.value))
                )
            self.completion_time_plot.plot_multi_line(series)
            return
        
        completion_data: dict[int, float] = \
            self.logic_engine.average_time_to_complete_per_stage()
        self.completion_time_plot.plot_line(
            completion_data.keys(),
            completion_data.values(),
            label="Avg completion time"
        )
