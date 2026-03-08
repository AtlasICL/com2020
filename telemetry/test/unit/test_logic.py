import unittest
from pathlib import Path
from datetime import datetime

from core.events import (
    Difficulty,
    EndSession,
    StartSession,
)
from core.logic import EventLogicEngine


class TestLogic(unittest.TestCase):
    def setUp(self):
        self.resource_file_path = Path(__file__).resolve().parents[1] \
            / "resources"
        self.logic_engine = EventLogicEngine()
    
    
    def test_categorise_events(self):
        """
        Tests that events are correctly parsed and categorised from a
        json file.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_short.json"
        )
        self.assertEqual(
            self.logic_engine.start_session_events, 
            {
            StartSession(
                '1', 
                1, 
                datetime(2026, 3, 7, 0, 0, 0), 
                Difficulty.EASY
            )
            }
        )
        self.assertEqual(
            self.logic_engine.end_session_events, 
            {EndSession('1', 1, datetime(2026, 3, 7, 0, 0, 1))}
        )


    def test_fail_difficulty_spikes(self):
        """
        Tests that fail difficulty spikes are correctly calculated
        across all game stages.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.fail_difficulty_spikes(), 
            {1:0, 2:1, 3:1, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
        )


    def test_get_unique_user_ids(self):
        """
        Tests that unique user IDs are correctly extracted from event
        data.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.get_unique_userIDs(), {'1'})
    

    def test_count_starts(self):
        """
        Tests that encounter starts are correctly counted for each
        stage.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.count_starts(1), 1)
        self.assertEqual(self.logic_engine.count_starts(2), 1)
        self.assertEqual(self.logic_engine.count_starts(3), 1)


    def test_count_fails(self):
        """
        Tests that encounter failures are correctly counted for each
        stage.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.count_fails(1), 0)
        self.assertEqual(self.logic_engine.count_fails(2), 1)
        self.assertEqual(self.logic_engine.count_fails(3), 1)


    def test_funnel_view(self):
        """
        Tests that the funnel view correctly shows completion counts
        per stage.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.funnel_view(), 
            {1: 1, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
        )


    def test_health_per_stage(self):
        """
        Tests that player health is correctly tracked per stage for a
        given session.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.health_per_stage(1), 
            {1: 5, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
        )


    def test_get_valid_difficulty(self):
        """
        Tests that the correct difficulty is retrieved for a valid
        session ID.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.get_difficulty(1), Difficulty.EASY)
    

    def test_get_invalid_difficulty(self):
        """
        Tests that an error is raised when getting difficulty for a
        non-existent session.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        with self.assertRaises(RuntimeError) as error:
            self.logic_engine.get_difficulty(4)
        self.assertEqual(
            str(error.exception), 
            "No session start event for provided session ID: 4"
        )
    

    def test_get_sessionIDs_of_difficulty(self):
        """
        Tests that session IDs are correctly filtered by difficulty
        level.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.get_sessionIDs_of_difficulty(Difficulty.EASY), 
            {1}
        )


    def test_get_health_per_stage_by_difficulty(self):
        """
        Tests that health per stage data is correctly grouped by
        difficulty level.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.get_health_per_stage_by_difficulty(Difficulty.EASY), 
            [{1: 5, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}]
        )
    

    def test_compare_health_per_stage_per_difficulty(self):
        """
        Tests that health data is correctly organized across all
        difficulty levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_health_per_stage_per_difficulty(), 
            {
            Difficulty.EASY: [{1: 5, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}], 
            Difficulty.MEDIUM: [], 
            Difficulty.HARD: []
            }
        )


    def test_get_coins_gained_per_stage(self):
        """
        Tests that coins gained are correctly tracked per stage for a
        given session.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.get_coins_gained_per_stage(1),
            {1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
        )
    

    def test_get_coins_per_stage_by_difficulty(self):
        """
        Tests that coins per stage data is correctly grouped by
        difficulty level.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.get_coins_per_stage_by_difficulty(Difficulty.EASY), 
            [{1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}]
        )
        self.assertEqual(
            self.logic_engine.get_coins_per_stage_by_difficulty(Difficulty.MEDIUM), 
            []
        )
        self.assertEqual(
            self.logic_engine.get_coins_per_stage_by_difficulty(Difficulty.HARD), 
            []
        )


    def test_compare_coins_per_stage_per_difficulty(self):
        """
        Tests that coin data is correctly organized across all
        difficulty levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_coins_per_stage_per_difficulty(), 
            {
            Difficulty.EASY: [{1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}], 
            Difficulty.MEDIUM: [], 
            Difficulty.HARD: []
            }
        )
        