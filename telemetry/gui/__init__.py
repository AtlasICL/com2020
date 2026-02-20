"""
This module is responsible for the graphical user interface (GUI) of
the telemetry app.

It contains the logic for creating the app's window, calling all
functions necessary to be displayed, as well as refreshing the event 
log data when necessary (currently, polling every 3 seconds).

The plotting submodule contains the logic for generating the matplotlib 
/ seaborn plots from the data. 
"""