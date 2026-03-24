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
    Speed,
    CoinHold,
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
            if event.lives_left == 0:
                difficulty_output[event.stage_number] += 1
        for event in self.boss_encounter_fail_events:
            if event.lives_left == 0:
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
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            for event in self.boss_encounter_fail_events:
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            result[diff] = spikes
        return result


    def fail_difficulty_spikes_speed(
            self
    ) -> dict[Speed, dict[int, int]]:
        """
        Output failure rate by stage, separated by speed.

        :return: Dictionary of speed level to dictionary of stage
        number to number of failures.
        :rtype: dict[Speed, dict[int, int]]
        """
        result: dict[Speed, dict[int, int]] = {}
        for speed in Speed:
            session_ids = self.get_sessionIDs_of_speed(speed)
            spikes = {stage: 0 for stage in range(1, 11)}
            for event in self.normal_encounter_fail_events:
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            for event in self.boss_encounter_fail_events:
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            result[speed] = spikes
        return result
    

    def fail_difficulty_spikes_coin_hold(
            self
    ) -> dict[CoinHold, dict[int, int]]:
        """
        Output failure rate by stage, separated by coin hold.

        :return: Dictionary of coin hold level to dictionary of stage
        number to number of failures.
        :rtype: dict[CoinHold, dict[int, int]]
        """
        result: dict[CoinHold, dict[int, int]] = {}
        for coin_hold in CoinHold:
            session_ids = self.get_sessionIDs_of_coin_hold(coin_hold)
            spikes = {stage: 0 for stage in range(1, 11)}
            for event in self.normal_encounter_fail_events:
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            for event in self.boss_encounter_fail_events:
                if event.sessionID in session_ids and event.lives_left == 0:
                    spikes[event.stage_number] += 1
            result[coin_hold] = spikes
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
    
    def get_unique_sessionIDs(self) -> set[int]:
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
                if fail_event.lives_left == 0:
                    fail_count += fail_event.stage_number == stage_number
            return fail_count
        for fail_event in self.normal_encounter_fail_events:
            if fail_event.lives_left == 0:
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
                        and event.stage_number == stage \
                        and event.lives_left == 0:
                            fails += 1
                else:
                    for event in self.normal_encounter_start_events:
                        if event.sessionID in session_ids \
                        and event.stage_number == stage:
                            starts += 1
                    for event in self.normal_encounter_fail_events:
                        if event.sessionID in session_ids \
                        and event.stage_number == stage \
                        and event.lives_left == 0:
                            fails += 1
                funnel[stage] = starts - fails
            result[diff] = funnel
        return result
    

    def funnel_view_speed(
            self
    ) -> dict[Speed, dict[int, int]]:
        """
        Output number of players passing a given stage, separated by
        speed.

        :return: Dictionary of speed level to dictionary of stage
        number to number of players left.
        :rtype: dict[Speed, dict[int, int]]
        """
        result: dict[Speed, dict[int, int]] = {}
        for speed in Speed:
            session_ids = self.get_sessionIDs_of_speed(speed)
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
                                and event.stage_number == stage \
                                and event.lives_left == 0:
                            fails += 1
                else:
                    for event in self.normal_encounter_start_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            starts += 1
                    for event in self.normal_encounter_fail_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage \
                                and event.lives_left == 0:
                            fails += 1
                funnel[stage] = starts - fails
            result[speed] = funnel
        return result
    

    def funnel_view_coin_hold(
            self
    ) -> dict[CoinHold, dict[int, int]]:
        """
        Output number of players passing a given stage, separated by
        coin hold.

        :return: Dictionary of coin hold level to dictionary of stage
        number to number of players left.
        :rtype: dict[CoinHold, dict[int, int]]
        """
        result: dict[CoinHold, dict[int, int]] = {}
        for coin_hold in CoinHold:
            session_ids = self.get_sessionIDs_of_coin_hold(coin_hold)
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
                                and event.stage_number == stage \
                                and event.lives_left == 0:
                            fails += 1
                else:
                    for event in self.normal_encounter_start_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage:
                            starts += 1
                    for event in self.normal_encounter_fail_events:
                        if event.sessionID in session_ids \
                                and event.stage_number == stage \
                                and event.lives_left == 0:
                            fails += 1
                funnel[stage] = starts - fails
            result[coin_hold] = funnel
        return result
    

    def health_per_stage(self, sessionID: int) -> dict[int, int]:
        """
        Output the health a session has per stage.
        
        :param sessionID: session stage to check.
        :type sessionID: int
        :return: dictionary of key stage number and value player 
        health remaining.
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
        :rtype: set[int]
        """
        difficulty_sessionIDs: set[int] = set()
        for start_event in self.start_session_events:
            if start_event.difficulty == difficulty:
                difficulty_sessionIDs.add(start_event.sessionID)
        return difficulty_sessionIDs
    

    def get_sessionIDs_of_speed(self, speed: Speed) -> set[int]:
        """
        Get the set of all sessionIDs of a given play speed.
        
        :param speed: The play speed to search for.
        :type speed: Speed
        :return: The list of sessionIDs with the given speed.
        :rtype: set[int]
        """
        # Get start times for each stage
        session_avg_times: dict[int, float] = {}
        for sessionID in self.get_unique_sessionIDs():
            start_times: dict[int, datetime] = {}
            for event in self.normal_encounter_start_events:
                if event.sessionID == sessionID:
                    start_times[event.stage_number] = event.timestamp
            for event in self.boss_encounter_start_events:
                if event.sessionID == sessionID:
                    start_times[event.stage_number] = event.timestamp

            # Get completion times per stage
            stage_times: dict[int, list[float]] = {
                stage: [] for stage in range(1, 11)
            }

            # Calculate total time for each stage
            for event in self.normal_encounter_complete_events:
                if event.sessionID == sessionID:
                    if event.stage_number in start_times:
                        duration = (
                            event.timestamp - start_times[event.stage_number]
                        ).total_seconds()
                        stage_times[event.stage_number].append(duration)
            for event in self.boss_encounter_complete_events:
                if event.sessionID == sessionID:
                    if event.stage_number in start_times:
                        duration = (
                            event.timestamp - start_times[event.stage_number]
                        ).total_seconds()
                        stage_times[event.stage_number].append(duration)

            # Calculate averages per stage
            averages: dict[int, float] = {}
            for stage in range(1, 11):
                times = stage_times[stage]
                averages[stage] = (sum(times) / len(times)) if times else 0.0

            # Average of all stage averages for this session
            played_stages = [averages[s] for s in averages
                             if stage_times[s]]
            if played_stages:
                session_avg_times[sessionID] = sum(played_stages) / len(played_stages)

        if not session_avg_times:
            return set()

        # Find median to split Fast/Slow
        sorted_times = sorted(session_avg_times.values())
        mid = len(sorted_times) // 2
        if len(sorted_times) % 2 == 0:
            median = (sorted_times[mid - 1] + sorted_times[mid]) / 2
        else:
            median = sorted_times[mid]

        # Add to result only if it means passed in Speed parameter
        result: set[int] = set()
        for sid, avg_time in session_avg_times.items():
            if speed == Speed.FAST and avg_time <= median:
                result.add(sid)
            elif speed == Speed.SLOW and avg_time > median:
                result.add(sid)
        return result
    

    def get_sessionIDs_of_coin_hold(self, coin_hold: CoinHold) -> set[int]:
        """
        Get the set of all sessionIDs of a given coin holding type.
        
        :param coin_hold: The coin holding type to search for.
        :type coin_hold: coin_hold
        :return: The list of sessionIDs with the given CoinHold.
        :rtype: list[int]
        """
        session_avg_coins: dict[int, float] = {}
        for sessionID in self.get_unique_sessionIDs():
            coins_gained: dict[int, int] = {}
            
            # Coins gained in each stage
            for event in sorted(self.gain_coin_events, key = lambda e: e.timestamp):
                if event.sessionID == sessionID:
                    coins_gained[event.stage_number] = event.coins_gained
            
            coins_held: dict[int, int] = {
                stage: 0 for stage in range(1,11)
            }

            # Calculates coins held each stage, cumulative 
            for event in sorted(
                self.buy_upgrade_events, key = lambda e: e.timestamp):
                if event.sessionID == sessionID:
                    if event.stage_number != 1:
                        coin_holding = coins_gained[event.stage_number] \
                            - event.coins_spent
                    else:
                        coin_holding = coins_held[event.stage_number] \
                            + coins_gained[event.stage_number] - event.coins_spent
                    coins_held[event.stage_number] = coin_holding

            # For stages with coins gained but no upgrades bought
            for stage in coins_gained:
                if coins_held[stage] == 0:
                    coins_held[stage] = coins_gained[stage]

            # Average of all stage coin holds for this session
            played_stages = [coins_held[s] for s in coins_held
                             if coins_held[s] != 0]
            if played_stages:
                session_avg_coins[sessionID] = sum(played_stages) / len(played_stages)

        if not session_avg_coins:
            return set()

        # Find median to split Long/Short
        sorted_coins = sorted(session_avg_coins.values())
        mid = len(sorted_coins) // 2
        if len(sorted_coins) % 2 == 0:
            median = (sorted_coins[mid - 1] + sorted_coins[mid]) / 2
        else:
            median = sorted_coins[mid]

        # Add to result only if it means passed in coin hold parameter
        result: set[int] = set()
        for sid, avg_coins in session_avg_coins.items():
            if coin_hold == CoinHold.LONG and avg_coins >= median:
                result.add(sid)
            elif coin_hold == CoinHold.SHORT and avg_coins < median:
                result.add(sid)
        return result

    
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
    

    def compare_health_speed(
            self
    ) -> dict[Speed, dict[int, int]]:
        """
        Get a dictionary with Speed type as the key, and the list
        of the health_per_stage objects of all sessions with that 
        speed level as the value.

        :return: Dictionary with Speed type as the key, and the list of 
        health_per_stage dictionaries of all sessions with the given
        speed level.
        :rtype: dict[Speed, dict[int, int]]
        """
        result: dict[Speed, dict[int, int]] = {}
        for speed in Speed:
            session_ids = self.get_sessionIDs_of_speed(speed)
            health_remaining_per_stage = {stage_number: 0 
                                            for stage_number in range(1,11)}
            for event in self.normal_encounter_complete_events:
                if event.sessionID in session_ids:
                    health_remaining_per_stage[
                        event.stage_number
                    ] = event.player_HP_remaining
            for event in self.boss_encounter_complete_events:
                if event.sessionID in session_ids:
                    health_remaining_per_stage[
                        event.stage_number
                    ] = event.player_HP_remaining
            result[speed] = health_remaining_per_stage
        return result
    

    def compare_health_coin_hold(
            self
    ) -> dict[CoinHold, dict[int, int]]:
        """
        Get a dictionary with CoinHold type as the key, and the list
        of the health_per_stage objects of all sessions with that 
        difficulty level as the value.

        :return: Dictionary with CoinHold type as the key, and the list of 
        health_per_stage dictionaries of all sessions with the given
        speed level.
        :rtype: dict[CoinHold, dict[int, int]]
        """
        result: dict[CoinHold, dict[int, int]] = {}
        for coin_hold in CoinHold:
            session_ids = self.get_sessionIDs_of_coin_hold(coin_hold)
            health_remaining_per_stage = {stage_number: 0 
                                            for stage_number in range(1,11)}
            for event in self.normal_encounter_complete_events:
                if event.sessionID in session_ids:
                    health_remaining_per_stage[
                        event.stage_number
                    ] = event.player_HP_remaining
            for event in self.boss_encounter_complete_events:
                if event.sessionID in session_ids:
                    health_remaining_per_stage[
                        event.stage_number
                    ] = event.player_HP_remaining
            result[coin_hold] = health_remaining_per_stage
        return result
    

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
  

    def compare_coins_speed(
            self
    ) -> dict[Speed, dict[int, int]]:
        """
        Get a dictionary with Speed level as the key, and the list 
        of the coins gained per stage dictionary objects of all sessions
        with that difficulty level as the value.

        :return: Dictionary of Speed level to list of all 
        coins gained per stage dictionaries of all sessions with the 
        given Speed level.
        :rtype: dict[Speed, dict[int, int]]
        """
        result: dict[Speed, dict[int, int]] = {}
        for speed in Speed:
            session_ids = self.get_sessionIDs_of_speed(speed)
            coins_gained_per_stage: dict[int, int] = {i: 0 for i in range(1, 11)}
            for event in self.gain_coin_events:
                if event.sessionID in session_ids:
                    coins_gained_per_stage[event.stage_number] += event.coins_gained
            result[speed] = coins_gained_per_stage
        return result
    

    def compare_coins_coin_hold(
            self
    ) -> dict[CoinHold, dict[int, int]]:
        """
        Get a dictionary with CoinHold level as the key, and the list 
        of the coins gained per stage dictionary objects of all sessions
        with that difficulty level as the value.

        :return: Dictionary of CoinHold level to list of all 
        coins gained per stage dictionaries of all sessions with the 
        given CoinHold level.
        :rtype: dict[CoinHold, dict[int, int]]
        """
        result: dict[CoinHold, dict[int, int]] = {}
        for coin_hold in CoinHold:
            session_ids = self.get_sessionIDs_of_coin_hold(coin_hold)
            coins_gained_per_stage: dict[int, int] = {i: 0 for i in range(1, 11)}
            for event in self.gain_coin_events:
                if event.sessionID in session_ids:
                    coins_gained_per_stage[event.stage_number] += event.coins_gained
            result[coin_hold] = coins_gained_per_stage
        return result


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


    def average_time_to_complete_per_stage_speed(
        self
    ) -> dict[Speed, dict[int, float]]:
        """
        Calculate the average time (in seconds) to complete each stage,
        separated by speed.

        :return: Dictionary of speed level to dictionary of stage
        number to average completion time in seconds.
        :rtype: dict[Speed, dict[int, float]]
        """
        result: dict[Speed, dict[int, float]] = {}
        for speed in Speed:
            session_ids = self.get_sessionIDs_of_speed(speed)

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
            result[speed] = averages
        return result
    
    
    def average_time_to_complete_per_stage_coin_hold(
            self
    ) -> dict[CoinHold, dict[int, float]]:
        """
        Calculate the average time (in seconds) to complete each stage,
        separated by coin hold.

        :return: Dictionary of coin hold level to dictionary of stage
        number to average completion time in seconds.
        :rtype: dict[CoinHold, dict[int, float]]
        """
        result: dict[CoinHold, dict[int, float]] = {}
        for coin_hold in CoinHold:
            session_ids = self.get_sessionIDs_of_coin_hold(coin_hold)

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
            result[coin_hold] = averages
        return result
