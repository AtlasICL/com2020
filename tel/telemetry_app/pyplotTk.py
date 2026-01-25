import numpy as np
import tkinter as tk
import seaborn as sns

import matplotlib
matplotlib.use('TkAgg')

from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import matplotlib.pyplot as plt

root = tk.Tk()
root.geometry('800x600')

fig = plt.figure(1)
plt.ion()
sns.barplot(x=[1,2,3], y=[1,2,3])

canvas = FigureCanvasTkAgg(fig, master=root)
plot_widget = canvas.get_tk_widget()

def update():
    pass

plot_widget.grid(row=0, column=0, sticky='nsew')
tk.Button(root,text="Update",command=update).grid(row=1, column=0, sticky='ew')

root.grid_rowconfigure(0, weight=1)
root.grid_columnconfigure(0, weight=1)

root.mainloop()
