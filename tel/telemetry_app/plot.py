import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import numpy as np

def plot_fail_difficulty_spikes(difficulty_input: dict[int, int]):
    plt.title("Difficulty Spikes")
    plt.xlabel("Stage Number")
    plt.ylabel("No. of Failures")
    plt.grid()
    sns.barplot(x=list(difficulty_input.keys()), y=list(difficulty_input.values()))
    plt.show()

def plot_funnel_view(funnel_input: dict[int, int]):
    plt.title("Difficulty Spikes")
    plt.xlabel("Stage Number")
    plt.ylabel("No. of Users Remaining")
    plt.grid()
    sns.barplot(x=list(funnel_input.keys()), y=list(funnel_input.values()))
    plt.show()

def main():
    plot_fail_difficulty_spikes({1: 0, 2: 1, 3: 1, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0})
    plot_funnel_view({1: 2, 2: 1, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0})
if __name__ == "__main__":
    main()