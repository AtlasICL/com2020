"""
This submodule is responsible for testing the event interpretation and
analysis logic of the telemetry application, which is present in 
core.logic.

This submodule defines a suite of unit tests using Python's 
unittest library.
"""

import unittest
from pathlib import Path
from datetime import datetime

from core.events import (
    CoinHold,
    Difficulty,
    SettingName,
    EndSession,
    SettingsChange,
    Speed,
    StartSession,
)
from core.logic import EventLogicEngine


class TestLogic(unittest.TestCase):
    def setUp(self):
        """
        Setup / bootstrapping method to set up environment and
        resources for testing.
        """
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
                userID='1', 
                sessionID=1, 
                timestamp=datetime(2026, 3, 7, 0, 0, 0), 
                difficulty=Difficulty.EASY
            )
            }
        )
        self.assertEqual(
            self.logic_engine.end_session_events, 
            {
            EndSession(
                userID='1',
                sessionID=1, 
                timestamp=datetime(2026, 3, 7, 0, 0, 1)
            )
            }
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
            {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
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
        self.assertEqual(self.logic_engine.count_starts(1), 2)
        self.assertEqual(self.logic_engine.count_starts(2), 2)
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
        self.assertEqual(self.logic_engine.count_fails(2), 0)
        self.assertEqual(self.logic_engine.count_fails(3), 0)


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
            {1: 2, 2: 2, 3: 1, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
        )

    def test_funnel_view_per_difficulty(self):
        """
        Tests that the funnel view can be correctly separated 
        by difficulty.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.funnel_view_per_difficulty(),
                         {Difficulty.EASY: 
                          {1: 1, 2: 1, 3: 1, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                          Difficulty.MEDIUM: 
                          {1: 1, 2: 1, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                          Difficulty.HARD: 
                          {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
                          }
                        )

    def test_funnel_view_speed(self):
        """
        Tests that the funnel view can be correctly separated
        by speed.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.funnel_view_speed(),
                        {
                            Speed.FAST:
                            {1: 1, 2: 1, 3: 1, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                            Speed.SLOW:
                            {1: 1, 2: 1, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
                        } 
                        )
    
    def test_funnel_view_coin_hold(self):
        """
        Tests that the funnel view can be correctly separated
        by coin hold.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(self.logic_engine.funnel_view_coin_hold(),
                         {
                            CoinHold.SHORT: 
                            {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                            CoinHold.LONG:
                            {1: 2, 2: 2, 3: 1, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
                         }
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
            {1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
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
            [{1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}]
        )
    

    def test_compare_health_per_stage_per_difficulty(self):
        """
        Tests that health data is correctly organised across all
        difficulty levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_health_per_stage_per_difficulty(), 
            {
            Difficulty.EASY: 
            [{1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}], 
            Difficulty.MEDIUM: 
            [{1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}], 
            Difficulty.HARD: []
            }
        )

    def test_compare_health_speed(self):
        """
        Tests that health data is correctly organised across all
        speed levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_health_speed(),
            {
                Speed.FAST: 
                {1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                Speed.SLOW:
                {1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
            }
        )
    
    def test_compare_health_coin_hold(self):
        """
        Tests that health data is correctly organised across all
        coin hold levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_health_coin_hold(),
            {
                CoinHold.LONG:
                {1: 5, 2: 10, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0},
                CoinHold.SHORT:
                {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
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
            [{1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}]
        )
        self.assertEqual(
            self.logic_engine.get_coins_per_stage_by_difficulty(Difficulty.HARD), 
            []
        )


    def test_compare_coins_per_stage_per_difficulty(self):
        """
        Tests that coin data is correctly organised across all
        difficulty levels.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_coins_per_stage_per_difficulty(), 
            {
            Difficulty.EASY: [{1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}], 
            Difficulty.MEDIUM: [{1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}], 
            Difficulty.HARD: []
            }
        )
    
    def test_compare_coins_speed(self):
        """
        Tests that coin data is correctly organised across all speeds.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_coins_speed(),
            {
            Speed.FAST: 
            {1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}, 
            Speed.SLOW: 
            {1: 15, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}   
            }
        )
    
    def test_compare_coins_coin_hold(self):
        """
        Tests that coin data is correctly organised across all Coin Hold
        level.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.compare_coins_coin_hold(),
            {
            CoinHold.LONG:
            {1: 30, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0},
            CoinHold.SHORT:
            {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7:0, 8:0, 9:0, 10:0}
            }
        )
    
    def test_get_settings_change_events(self):
        """
        Tests that settings data can be correctly returned based on 
        parsed events.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.get_settings_change_events(),
            [
            SettingsChange(userID="1", timestamp=datetime(2026, 6, 6, 0, 0, 2),
                           setting=SettingName.TELEMETRY_ENABLED, value="false",
                           justification=""),
            SettingsChange(userID="1", timestamp=datetime(2026, 3, 6, 0, 0, 1),
                           setting=SettingName.STARTING_LIVES, value="MEDIUM: 1",
                           justification="No Justifications Made."),
            SettingsChange(userID="1", timestamp=datetime(2026, 3, 6, 0, 0, 0),
                            setting=SettingName.TELEMETRY_ENABLED, value="true",
                            justification="")
            ]
        )
    
    def test_average_time_to_complete_per_stage(self):
        """
        Tests that the average time to complete each stage can be
        determined.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.average_time_to_complete_per_stage(),
            {1:8, 2:13, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
        )

    def test_average_time_to_complete_per_stage_per_difficulty(self):
        """
        Tests that the average time to complete each stage can be
        determined separated by difficulty.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.average_time_to_complete_per_stage_per_difficulty(),
            {
                Difficulty.EASY: {1:2, 2:6, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0},
                Difficulty.MEDIUM: {1:14, 2:20, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0},
                Difficulty.HARD : {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
            }
        )
    
    def test_average_time_to_complete_per_stage_speed(self):
        """
        Tests that the average time to complete each stage can be
        determined separated by speed.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.average_time_to_complete_per_stage_speed(),
            {
                Speed.FAST: {1:2, 2:6, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0},
                Speed.SLOW: {1:14, 2:20, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
            }
        )

    def test_average_time_to_complete_per_stage_coin_hold(self):
        """
        Tests that the average time to complete each stage can be
        determined separated by Coin Hold.
        """
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_logic.json"
        )
        self.assertEqual(
            self.logic_engine.average_time_to_complete_per_stage_coin_hold(),
            {
                CoinHold.LONG: {1:8, 2:13, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0},
                CoinHold.SHORT: {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
            }
        )