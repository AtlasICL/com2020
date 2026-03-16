"""
Docstring for auth.auth_logger

Provides functionality for creating a logger instance for logging 
authentication events with a custom format and output file.
"""
import os
import logging

def setup_logger(output_file: str) -> logging.Logger:
    """
    Sets up a logger instance, with appropriate formatting and output
    file setup.

    :param per_stage_data: List of dictionaries which represent 
    :type per_stage_data: list[dict[int, int]]
    :return: Average of the given data per stage. 
    :rtype: dict[int, float]
    """
    logger = logging.getLogger(__name__)
    logger.setLevel(logging.INFO)

    os.makedirs("auth", exist_ok=True) # makes sure the directory exists before file handler

    logging_handler = logging.FileHandler("auth/logs.txt", encoding="utf-8")
    logging_handler.setLevel(logging.INFO)
    logging_handler.setFormatter(logging.Formatter(
        fmt="%(asctime)s\n    %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S"
    ))

    logger.addHandler(logging_handler)
    return logger
