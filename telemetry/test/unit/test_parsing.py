"""
This submodule is responsible for testing the event parsing 
functionality of the telemetry application. It defines a suite of unit 
tests using Python's unittest library, which assert correct parsing of
events from json into memory.
"""

import unittest
from datetime import datetime
from pathlib import Path

from core.parsing import convert_time, get_file, parse_file, parse_event
from core.events import (
    Difficulty,
    StartSession,
    EndSession,
)

class TestParsing(unittest.TestCase):
    def setUp(self):
        """
        Setup / bootstrapping method to set up environment and
        resources for testing.
        """
        self.resource_file_path = \
            Path(__file__).resolve().parents[1] / "resources"

    
    def test_convert_time(self):
        """
        Tests whether convert_time converts standardised timestamp
        string to python datetime object.
        """
        self.assertEqual(
            datetime(2026, 3, 7, 5, 30, 0), 
            convert_time("2026/03/07/05/30/0")
        )

    
    def test_get_correct_file(self):
        """
        Tests whether a json file can be parsed to a list of dicts
        using get_file.
        """
        expected_dict = \
        [
            {
            "userID" : "1",
            "event" : "StartSession",
            "sessionID" : 1,
            "difficulty" : "Easy",
            "timestamp" : "2026/03/07/00/00/00"
            },
            {
            "userID" : "1",
            "event" : "EndSession",
            "sessionID" : 1,
            "timestamp" : "2026/03/07/00/00/01"
            }
        ]
        path = self.resource_file_path / "test_events_short.json"
        self.assertEqual(get_file(path), expected_dict)


    def test_get_missing_file(self):
        """
        Tests whether an incorrect file path throws the correct error.
        """
        path = self.resource_file_path / "does_not_exist.json"
        with self.assertRaises(FileNotFoundError):
            get_file(path)
    

    def test_get_incorrect_file(self):
        """
        Tests whether an invalid json file throws the correct error.
        """
        path = self.resource_file_path / "test_events_incorrect.json"
        with self.assertRaises(RuntimeError):
            get_file(path)


    def test_parse_file(self):
        """
        Tests whether a json file can be parsed into valid python
        events.
        """
        path = self.resource_file_path / "test_events_short.json"
        session_start = StartSession(
            '1', 
            1, 
            datetime(2026, 3, 7, 0, 0, 0),
            Difficulty.EASY
        )
        session_end = EndSession('1', 1, datetime(2026, 3, 7, 0, 0, 1))
        self.assertEqual(parse_file(path), [session_start, session_end])

        
    def test_parse_correct_event(self):
        """
        Tests whether a valid json object can be parsed into a valid 
        Python event.
        """
        event_dict = {
                "userID" : "1",
                "event" : "StartSession",
                "sessionID" : 1,
                "difficulty" : "Easy",
                "timestamp" : "2026/03/07/00/00/00"
                }
        event = StartSession(
            '1', 
            1, 
            datetime(2026, 3, 7, 0, 0, 0), 
            Difficulty.EASY
        )
        self.assertEqual(parse_event(event_dict), event)

    
    def test_parse_incorrect_event(self):
        """
        Tests whether an invalid type of json object is rejected.
        """
        event_dict = {
                    "userID" : "1",
                    "event" : "FakeType",
                    "sessionID" : 1,
                    "difficulty" : "Easy",
                    "timestamp" : "2026/03/07/00/00/00"
                    }
        with self.assertRaises(RuntimeError) as error:
            parse_event(event_dict)
        self.assertEqual(
            str(error.exception), 
            "Unexpected event type: FakeType"
        )


    def test_parse_missing_event(self):
        """
        Tests whether a json object with missing parameters is rejected.
        """
        event_dict = \
        {
            "userID" : "1",
            "event" : "StartSession",
            "difficulty" : "Easy",
            "timestamp" : "2026/03/07/00/00/00"
        }
        with self.assertRaises(RuntimeError) as error:
            parse_event(event_dict)
        self.assertEqual(
            str(error.exception),
            "An event of type StartSession is missing the field 'sessionID'"
        )
    