"""
Docstring for telemetry.core.logic

This module provides the logic for aggregating and analysing the data
parsed by the parsing module, namely sorting by difficulty, getting 
averages by difficulty, etc...

This functionality is provided by the EventLogicEngine class.
"""

from datetime import datetime
from pathlib import Path

from core.events import (
    BossEncounterComplete,
    BossEncounterFail,
    BossEncounterStart,
    BuyUpgrade,
    Difficulty,
    EndSession,
    GainCoin,
    KillEnemy,
    NormalEncounterComplete,
    NormalEncounterFail,
    NormalEncounterStart,
    SettingsChange,
    StartSession,
)
from core.parsing import parse_file, ValidEvent


class EventLogicEngine:
    def __init__(self):
        self.start_session_events: set[
            StartSession
        ] = set()
        self.end_session_events: set[
            EndSession
        ] = set()
        self.normal_encounter_start_events: set[
            NormalEncounterStart
        ] = set()
        self.normal_encounter_complete_events: set[
            NormalEncounterComplete
        ] = set()
        self.normal_encounter_fail_events: set[
            NormalEncounterFail
        ] = set()
        self.boss_encounter_start_events: set[
            BossEncounterStart
        ] = set()
        self.boss_encounter_complete_events: set[
            BossEncounterComplete
        ] = set()
        self.boss_encounter_fail_events: set[
            BossEncounterFail
        ] = set()
        self.gain_coin_events: set[
            GainCoin
        ] = set()
        self.buy_upgrade_events: set[
            BuyUpgrade
        ] = set()
        self.settings_change_events: set[
            SettingsChange
        ] = set()
        self.kill_enemy_events: set[
            KillEnemy
        ] = set()

        self._attributes: list[set] = [ 
            self.start_session_events, 
            self.end_session_events,
            self.normal_encounter_start_events,
            self.normal_encounter_complete_events,
            self.normal_encounter_fail_events,
            self.boss_encounter_start_events,
            self.boss_encounter_complete_events,
            self.boss_encounter_fail_events,
            self.gain_coin_events,
            self.buy_upgrade_events,
            self.settings_change_events,
            self.kill_enemy_events
        ]


    def categorise_events(self, filename: Path) -> None:
        """
        Creates objects from the json file provided. 

        :param filename: json file with custom schema.
        :type filename: Path
        """
        for attr in self._attributes:
            attr.clear()
        
        events: list[ValidEvent] = parse_file(filename)
        for event in events:
            if isinstance(event, StartSession):
                self.start_session_events.add(event)
            elif isinstance(event, EndSession):
                self.end_session_events.add(event)
            elif isinstance(event, NormalEncounterStart):
                self.normal_encounter_start_events.add(event)
            elif isinstance(event, NormalEncounterComplete):
                self.normal_encounter_complete_events.add(event)
            elif isinstance(event, NormalEncounterFail):
                self.normal_encounter_fail_events.add(event)
            elif isinstance(event, BossEncounterStart):
                self.boss_encounter_start_events.add(event)
            elif isinstance(event, BossEncounterComplete):
                self.boss_encounter_complete_events.add(event)
            elif isinstance(event, BossEncounterFail):
                self.boss_encounter_fail_events.add(event)
            elif isinstance(event, GainCoin):
                self.gain_coin_events.add(event)
            elif isinstance(event, BuyUpgrade):
                self.buy_upgrade_events.add(event)
            elif isinstance(event, SettingsChange):
                self.settings_change_events.add(event)
            elif isinstance(event, KillEnemy):
                self.kill_enemy_events.add(event)


    def fail_difficulty_spikes(self) -> dict[int, int]:
        """
        Output failure rate by stage.

        :return: dictionary of key stage number and value number of 
        failures.
        :rtype: dict[int, int]
        """
        difficulty_output = {stage_number: 0 for stage_number in range(1,11)}
        for event in self.normal_encounter_fail_events:
            difficulty_output[event.stage_number] += 1
        for event in self.boss_encounter_fail_events:
            difficulty_output[event.stage_number] += 1
        return difficulty_output


    def fail_difficulty_spikes_per_difficulty(
            self
    ) -> dict[Difficulty, dict[int, int]]:
        """
        Output failure rate by stage, separated by difficulty.

        :return: Dictionary of difficulty level to dictionary of stage
        number to number of failures.
        :rtype: dict[Difficulty, dict[int, int]]
        """
        result: dict[Difficulty, dict[int, int]] = {}
        for diff in Difficulty:
            session_ids = self.get_sessionIDs_of_difficulty(diff)
            spikes = {stage: 0 for stage in range(1, 11)}
            for event in self.normal_encounter_fail_events:
                if event.sessionID in session_ids:
                    spikes[event.stage_number] += 1
            for event in self.boss_encounter_fail_events:
                if event.sessionID in session_ids:
                    spikes[event.stage_number] += 1
            result[diff] = spikes
        return result


    def get_number_of_session_starts(self) -> int:
        """
        Get the number of start session events.        

        :return: Returns the number of unique session start events.
        :rtype: int
        """
        return len(self.start_session_events)
    
    
    def get_unique_userIDs(self) -> set[int]:
        """
        Returns the set of unique user IDs.
        
        :return: set of unique user IDs.
        :rtype: set[int]
        """
        uniqueIDs = set()
        for event in self.start_session_events:
            uniqueIDs.add(event.userID)
        return uniqueIDs
    
    def get_unique_sessionIDs(self) -> set[str]:
        """
        Returns the set of unique session IDs.
        
        :return: set of unique session IDs.
        :rtype: set[int]
        """
        uniqueIDs = set()
        for event in self.start_session_events:
            uniqueIDs.add(event.sessionID)
        return uniqueIDs

    def count_starts(self, stage_number: int) -> int:
        """
        Counts the number of starts of a given stage.
        
        :param stage_number: stage number in question.
        :return: number of starts of that stage.
        :rtype: int
        """
        start_count = 0
        if stage_number in [3, 6, 9, 10]:
            for start_event in self.boss_encounter_start_events:
                start_count += start_event.stage_number == stage_number
            return start_count
        for start_event in self.normal_encounter_start_events:
            start_count += start_event.stage_number == stage_number
        return start_count     
       

    def count_fails(self, stage_number: int) -> int:
        """
        Counts the number of failures on a given stage.
        
        :param stage_number: stage number in question.
        :return: number of fails at that stage.
        :rtype: int
        """
        fail_count = 0
        if stage_number in [3, 6, 9, 10]:
            for fail_event in self.boss_encounter_fail_events:
                fail_count += fail_event.stage_number == stage_number
            return fail_count
        for fail_event in self.normal_encounter_fail_events:
            fail_count += fail_event.stage_number == stage_number
        return fail_count
    
    
    def funnel_view(self) -> dict[int, int]:
        """
        Output number of players passing a given stage.
        
        :return: dictionary of key stage number and value number of 
        players left.
        :rtype: dict[int, int]
        """
        return {
            stage_number: 
            self.count_starts(stage_number) - self.count_fails(stage_number) 
            for stage_number in range(1,11)
        }


    def funnel_view_per_difficulty(
            self
    ) -> dict[Difficulty, dict[int, int]]:
        """
        Output number of players passing a given stage, separated by
        difficulty.

        :return: Dictionary of difficulty level to dictionary of stage
        number to number of players left.
        :rtype: dict[Difficulty, dict[int, int]]
        """
        result: dict[Difficulty, dict[int, int]] = {}
        for diff in Difficulty:
            session_ids = self.get_sessionIDs_of_difficulty(diff)
            funnel: dict[int, int] = {}
            for stage in range(1, 11):
                starts = 0
                fails = 0
                if stage in [3, 6, 9, 10]:
                    for event in self.boss_encounter_start_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            starts += 1
                    for event in self.boss_encounter_fail_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            fails += 1
                else:
                    for event in self.normal_encounter_start_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            starts += 1
                    for event in self.normal_encounter_fail_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            fails += 1
                funnel[stage] = starts - fails
            result[diff] = funnel
        return result
    
    
    def health_per_stage(self, sessionID: int) -> dict[int, int]:
        """
        Output the health a session has per stage.
        
        :param sessionID: session stage to check.
        :type sessionID: int
        :return: dictionary of key stage number and value player HP
        remaining.
        :rtype: dict[int, int]
        """
        health_remaining_per_stage = {stage_number: 0 
                                      for stage_number in range(1,11)}
        for event in self.normal_encounter_complete_events:
            if event.sessionID == sessionID:
                health_remaining_per_stage[
                    event.stage_number
                ] = event.player_HP_remaining
        for event in self.boss_encounter_complete_events:
            if event.sessionID == sessionID:
                health_remaining_per_stage[
                    event.stage_number
                ] = event.player_HP_remaining
        return health_remaining_per_stage
    
    
    def get_difficulty(self, sessionID: int) -> Difficulty:
        """
        Get the difficulty for a given session.
        
        :param sessionID: sessionID of the session to get difficulty of.
        :type sessionID: int
        :return: Difficulty value of the session.
        :rtype: Difficulty
        """
        for start_event in self.start_session_events:
            if start_event.sessionID == sessionID:
                return start_event.difficulty
        raise RuntimeError("No session start event for provided " \
        f"session ID: {sessionID}")
    
    
    def get_sessionIDs_of_difficulty(self, difficulty: Difficulty) -> set[int]:
        """
        Get the set of all sessionIDs of a given difficulty.
        
        :param difficulty: The difficulty level to search for.
        :type difficulty: Difficulty
        :return: The list of sessionIDs with the given difficulty.
        :rtype: list[int]
        """
        difficulty_sessionIDs: set[int] = set()
        for start_event in self.start_session_events:
            if start_event.difficulty == difficulty:
                difficulty_sessionIDs.add(start_event.sessionID)
        return difficulty_sessionIDs
        

    def get_health_per_stage_by_difficulty(
            self,
            difficulty: Difficulty
    ) -> list[dict[int, int]]:
        """
        Returns a health_per_stage dictionary for all sessions with a
        given difficulty.
        
        :param difficulty: Given difficulty.
        :type difficulty: Difficulty
        :return: List of health per stage for each session with the
        specified difficulty level.
        :rtype: list[dict[int, int]]
        """
        health_per_stage = []
        for sessionID in self.get_sessionIDs_of_difficulty(difficulty):
            health_per_stage.append(self.health_per_stage(sessionID))
        return health_per_stage
    

    def compare_health_per_stage_per_difficulty(
            self
    ) -> dict[Difficulty, list[dict[int, int]]]:
        """
        Get a dictionary with difficulty level as the key, and the list 
        of the health_per_stage objects of all sessions with that 
        difficulty level as the value.
        
        :return: Dictionary of difficulty level to list of all 
        health_per_stage dictionaries of all sessions with the given
        difficulty level.
        :rtype: dict[Difficulty, list[dict[int, int]]]
        """
        return {diff: self.get_health_per_stage_by_difficulty(diff) 
                for diff in Difficulty}
    

    def get_coins_gained_per_stage(
            self, 
            sessionID: int
    ) -> dict[int, int]:
        """
        Get the number of coins gained at each stage for a given 
        session.
        
        :param sessionID: sessionID in question.
        :type sessionID: int
        :return: Stage number -> coins gained at that stage.
        :rtype: dict[int, int]
        """
        coins_gained_per_stage: dict[int, int] = {i: 0 for i in range(1, 11)}
        for event in self.gain_coin_events:
            if event.sessionID == sessionID:
                coins_gained_per_stage[event.stage_number] += event.coins_gained
        return coins_gained_per_stage
    

    def get_coins_per_stage_by_difficulty(
            self,
            difficulty: Difficulty
    ) -> list[dict[int, int]]:
        """
        Returns the coins gained per stage dictionary for all sessions 
        with a given difficulty.
        
        :param difficulty: Given difficulty.
        :type difficulty: Difficulty
        :return: List of coins per stage for each session with the
        specified difficulty level.
        :rtype: list[dict[int, int]]
        """
        coins_per_stage = []
        for sessionID in self.get_sessionIDs_of_difficulty(difficulty):
            coins_per_stage.append(self.get_coins_gained_per_stage(sessionID))
        return coins_per_stage
    

    def compare_coins_per_stage_per_difficulty(
            self
    ) -> dict[Difficulty, list[dict[int, int]]]:
        """
        Get a dictionary with difficulty level as the key, and the list 
        of the coins gained per stage dictionary objects of all sessions
        with that difficulty level as the value.
        
        :return: Dictionary of difficulty level to list of all 
        coins gained per stage dictionaries of all sessions with the 
        given difficulty level.
        :rtype: dict[Difficulty, list[dict[int, int]]]
        """
        return {diff: self.get_coins_per_stage_by_difficulty(diff) 
                for diff in Difficulty}


    def get_settings_change_events(self) -> list[SettingsChange]:
        """
        Returns a list of all settings change events, sorted by
        timestamp.

        :return: A list of the SettingsChange events.
        :rtype: list[SettingsChange]
        """
        return sorted(
            self.settings_change_events,
            key=lambda e: e.timestamp,
            reverse=True
        )


    def average_time_to_complete_per_stage(self) -> dict[int, float]:
        """
        Calculate the average time (in seconds) to complete each stage,
        across all sessions. Time to complete is the difference between
        the encounter start and encounter complete timestamps for a
        given session and stage.

        :return: Dictionary of stage number to average completion time
        in seconds.
        :rtype: dict[int, float]
        """
        # Build lookup of start timestamps keyed by (sessionID, stage)
        start_times: dict[tuple[int, int], datetime] = {}
        for event in self.normal_encounter_start_events:
            key = (event.sessionID, event.stage_number)
            start_times[key] = event.timestamp
        for event in self.boss_encounter_start_events:
            key = (event.sessionID, event.stage_number)
            start_times[key] = event.timestamp

        # Get completion times per stage
        stage_times: dict[int, list[float]] = {
            stage: [] for stage in range(1, 11)
        }
        for event in self.normal_encounter_complete_events:
            key = (event.sessionID, event.stage_number)
            if key in start_times:
                duration = (
                    event.timestamp - start_times[key]
                ).total_seconds()
                stage_times[event.stage_number].append(duration)
        for event in self.boss_encounter_complete_events:
            key = (event.sessionID, event.stage_number)
            if key in start_times:
                duration = (
                    event.timestamp - start_times[key]
                ).total_seconds()
                stage_times[event.stage_number].append(duration)

        # Calculate averages per stage
        averages: dict[int, float] = {}
        for stage in range(1, 11):
            times = stage_times[stage]
            averages[stage] = (sum(times) / len(times)) if times else 0.0
        return averages


    def average_time_to_complete_per_stage_per_difficulty(
            self
    ) -> dict[Difficulty, dict[int, float]]:
        """
        Calculate the average time (in seconds) to complete each stage,
        separated by difficulty.

        :return: Dictionary of difficulty level to dictionary of stage
        number to average completion time in seconds.
        :rtype: dict[Difficulty, dict[int, float]]
        """
        result: dict[Difficulty, dict[int, float]] = {}
        for diff in Difficulty:
            session_ids = self.get_sessionIDs_of_difficulty(diff)

            start_times: dict[tuple[int, int], datetime] = {}
            for event in self.normal_encounter_start_events:
                if event.sessionID in session_ids:
                    key = (event.sessionID, event.stage_number)
                    start_times[key] = event.timestamp
            for event in self.boss_encounter_start_events:
                if event.sessionID in session_ids:
                    key = (event.sessionID, event.stage_number)
                    start_times[key] = event.timestamp

            stage_times: dict[int, list[float]] = {
                stage: [] for stage in range(1, 11)
            }
            for event in self.normal_encounter_complete_events:
                key = (event.sessionID, event.stage_number)
                if key in start_times:
                    duration = (
                        event.timestamp - start_times[key]
                    ).total_seconds()
                    stage_times[event.stage_number].append(duration)
            for event in self.boss_encounter_complete_events:
                key = (event.sessionID, event.stage_number)
                if key in start_times:
                    duration = (
                        event.timestamp - start_times[key]
                    ).total_seconds()
                    stage_times[event.stage_number].append(duration)

            averages: dict[int, float] = {}
            for stage in range(1, 11):
                times = stage_times[stage]
                averages[stage] = \
                    (sum(times) / len(times)) if times else 0.0
            result[diff] = averages
        return result
