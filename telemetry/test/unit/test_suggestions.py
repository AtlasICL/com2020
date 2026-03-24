import unittest
from pathlib import Path
from datetime import datetime

from core.logic import EventLogicEngine
from core.suggestions import SuggestionGenerator


class TestLogic(unittest.TestCase):
    def setUp(self):
        """
        Setup / bootstrapping method to set up environment and
        resources for testing.
        """
        self.resource_file_path = Path(__file__).resolve().parents[1] \
            / "resources"
        self.logic_engine = EventLogicEngine()
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_example_events.json"
        )
        self.suggestion_generator = SuggestionGenerator(self.logic_engine)
    
    def test_generate_spike_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_spike_suggestion(),
                        [{'difficulty': 'Easy',
                        'problem': 'Low pass rate.',
                        'stages': '1, 2, 3, 4',
                        'suggestion': 'Increase starting lives.'},
                        {'difficulty': 'Medium',
                        'problem': 'Low pass rate.',
                        'stages': '1, 2',
                        'suggestion': 'Increase starting lives.'}]
                        )
    
    def test_generate_empty_spike_suggestion(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_spike_suggestion(), [])

    def test_generate_low_health_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_low_health_suggestion(), 
                         [{'difficulty': 'Easy',
                        'problem': 'High health loss.',
                        'stages': '4, 5, 6, 7, 8, 9, 10',
                        'suggestion': 'Increase max health.'},
                        {'difficulty': 'Medium',
                        'problem': 'High health loss.',
                        'stages': '4, 5, 6',
                        'suggestion': 'Increase max health.'}]
                        )
    
    def test_generate_low_health_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_low_health_suggestion(), [])

    def test_generate_high_pass_rate_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_high_pass_rate_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'High pass rate.',
                           'stages': '6, 7, 8, 9, 10',
                           'suggestion': 'Decrease starting lives.'},
                          {'difficulty': 'Medium',
                           'problem': 'High pass rate.',
                           'stages': '4, 5, 6',
                           'suggestion': 'Decrease starting lives.'}]
                         )

    def test_generate_high_pass_rate_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_high_pass_rate_suggestion(), [])

    def test_generate_high_health_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_high_health_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'Low health loss.',
                           'stages': '1, 2, 3',
                           'suggestion': 'Decrease max health.'},
                          {'difficulty': 'Medium',
                           'problem': 'Low health loss.',
                           'stages': '1, 2, 3',
                           'suggestion': 'Decrease max health.'}]
                         )

    def test_generate_high_health_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_high_health_suggestion(), [])

    def test_generate_low_coin_gain_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_low_coin_gain_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'Low coin gain.',
                           'stages': '6, 7, 8, 9, 10',
                           'suggestion': 'Increase coins gained per level.'},
                          {'difficulty': 'Medium',
                           'problem': 'Low coin gain.',
                           'stages': '4, 5, 6',
                           'suggestion': 'Increase coins gained per level.'}]
                         )

    def test_generate_low_coin_gain_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_low_coin_gain_suggestion(), [])

    def test_generate_high_coin_gain_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_high_coin_gain_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'High coin gain.',
                           'stages': '1, 2, 3, 4',
                           'suggestion': 'Decrease coins gained per level.'},
                          {'difficulty': 'Medium',
                           'problem': 'High coin gain.',
                           'stages': '1, 2',
                           'suggestion': 'Decrease coins gained per level.'}]
                         )

    def test_generate_high_coin_gain_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_high_coin_gain_suggestion(), [])

    def test_generate_fast_average_time_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_fast_average_time_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'Fast average completion time.',
                           'stages': '1, 2, 3, 8, 9, 10',
                           'suggestion': 'Increase number of enemies per stage.'},
                          {'difficulty': 'Medium',
                           'problem': 'Fast average completion time.',
                           'stages': '2, 6',
                           'suggestion': 'Increase number of enemies per stage.'}]
                         )

    def test_generate_fast_average_time_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_fast_average_time_suggestion(), [])

    def test_generate_slow_average_time_suggestion(self):
        self.assertEqual(self.suggestion_generator.generate_slow_average_time_suggestion(),
                         [{'difficulty': 'Easy',
                           'problem': 'Slow average completion time.',
                           'stages': '4, 5, 7',
                           'suggestion': 'Decrease number of enemies per stage.'},
                          {'difficulty': 'Medium',
                           'problem': 'Slow average completion time.',
                           'stages': '5',
                           'suggestion': 'Decrease number of enemies per stage.'}]
                         )

    def test_generate_slow_average_time_suggestion_empty(self):
        self.logic_engine.categorise_events(
            self.resource_file_path / "test_events_empty.json"
        )
        self.assertEqual(self.suggestion_generator.generate_slow_average_time_suggestion(), [])
