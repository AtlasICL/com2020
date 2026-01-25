from tkinter import ttk
from matplotlib.figure import Figure
from matplotlib.ticker import FixedLocator
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

class PlotTab:
    def __init__(
            self, 
            parent: ttk.Frame, 
            title: str, 
            xlabel: str = "", 
            ylabel: str = ""
    ):
        parent.rowconfigure(0, weight=1)
        parent.columnconfigure(0, weight=1)

        container = ttk.Frame(parent)
        container.grid(row=0, column=0, sticky="nsew")

        self.title = title
        self.xlabel = xlabel
        self.ylabel = ylabel

        self.figure = Figure(figsize=(6, 4), dpi=90)
        self.axes = self.figure.add_subplot()
        self.axes.set_title(self.title)
        self.axes.set_xlabel(self.xlabel)
        self.axes.set_ylabel(self.ylabel)

        self.canvas = FigureCanvasTkAgg(self.figure, master=container)
        self.canvas_widget = self.canvas.get_tk_widget()
        self.canvas_widget.pack(fill="both", expand=True)

        self.color_map: dict[str, str] = {
            "Easy": "green",
            "Normal": "orange",
            "Hard": "red"
        }


    def plot_line(self, x, y, label=None):
        self.axes.clear()
        self.axes.set_title(self.title)
        self.axes.set_xlabel(self.xlabel)
        self.axes.set_ylabel(self.ylabel)
        stages = list(range(1, 11))
        self.axes.set_xlim(1, 10)
        self.axes.xaxis.set_major_locator(FixedLocator(stages))

        self.axes.plot(
            x, 
            y, 
            linestyle=':', # dotted lines
            marker='o',    # dots on the points
            linewidth=3,   # thicker lines
            markersize=6,  # bolder points
            label=label, 
            color='black'
        )
        # self.axes.legend(
        #     handlelength=4,    # longer lines in legend
        #     markerscale=1,     # dot scale in legend
        #     handletextpad=0.2, # padding with text
        #     borderaxespad=0.2, # padding from border
        # )

        self.axes.grid(True)
        self.canvas.draw()

    def plot_multi_line(self, series: list[tuple[list, list, str]]):
        self.axes.clear()
        self.axes.set_title(self.title)
        self.axes.set_xlabel(self.xlabel)
        self.axes.set_ylabel(self.ylabel)
        stages = list(range(1, 11))
        self.axes.set_xlim(1, 10)
        self.axes.xaxis.set_major_locator(FixedLocator(stages))

        for x, y, label in series:
            self.axes.plot(
                x, 
                y, 
                linestyle=':', # dotted lines
                marker='o',    # dots on the points
                linewidth=3,   # thicker lines
                markersize=6,  # bolder points
                label=label, 
                color=self.color_map[label] # map colour to difficulty
            )
        self.axes.legend(
            handlelength=0.5,    # longer line in legend
            markerscale=1,     # dot scale in legend
            handletextpad=0.4, # padding with text
            borderaxespad=0.2, # padding from border
        )
        self.axes.grid(True)
        self.canvas.draw()