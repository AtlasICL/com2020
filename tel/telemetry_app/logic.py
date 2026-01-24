import os
from dataclasses import dataclass, field

from events import *
from parsing import parse_file, ValidEvent

class EventLogicEngine:
    def __init__(self):
        self.session_start_events: set[SessionStart] = set()
        self.end_session_events: set[EndSession] = set()
        self.normal_encounter_start_events: set[NormalEncounterStart] = set()
        self.normal_encounter_complete_events: set[NormalEncounterComplete] = set()
        self.normal_encounter_fail_events: set[NormalEncounterFail] = set()
        self.normal_encounter_retry_events: set[NormalEncounterRetry] = set()
        self.boss_encounter_start_events: set[BossEncounterStart] = set()
        self.boss_encounter_complete_events: set[BossEncounterComplete] = set()
        self.boss_encounter_fail_events: set[BossEncounterFail] = set()
        self.boss_encounter_retry_events: set[BossEncounterRetry] = set()
        self.gain_coin_events: set[GainCoin] = set()
        self.buy_upgrade_events: set[BuyUpgrade] = set()
        self.settings_change_events: set[SettingsChange] = set()
        self.kill_enemy_events: set[KillEnemy] = set()

        self._attributes = [ 
            self.session_start_events, 
            self.end_session_events,
            self.normal_encounter_start_events,
            self.normal_encounter_complete_events,
            self.normal_encounter_fail_events,
            self.normal_encounter_retry_events,
            self.boss_encounter_start_events,
            self.boss_encounter_complete_events,
            self.boss_encounter_fail_events,
            self.boss_encounter_retry_events,
            self.gain_coin_events,
            self.buy_upgrade_events,
            self.settings_change_events,
            self.kill_enemy_events
        ]

    def categorise_events(self, filename: str) -> None:
        """
        Creates objects from the json file provided. 

        :param filename: json file with custom schema.
        :type filename: str
        """
        events: list[ValidEvent] = parse_file(filename)
        for event in events:
            if isinstance(event, SessionStart):
                self.session_start_events.add(event)
            elif isinstance(event, EndSession):
                self.end_session_events.add(event)
            elif isinstance(event, NormalEncounterStart):
                self.normal_encounter_start_events.add(event)
            elif isinstance(event, NormalEncounterComplete):
                self.normal_encounter_complete_events.add(event)
            elif isinstance(event, NormalEncounterFail):
                self.normal_encounter_fail_events.add(event)
            elif isinstance(event, NormalEncounterRetry):
                self.normal_encounter_retry_events.add(event)
            elif isinstance(event, BossEncounterStart):
                self.boss_encounter_start_events.add(event)
            elif isinstance(event, BossEncounterComplete):
                self.boss_encounter_complete_events.add(event)
            elif isinstance(event, BossEncounterFail):
                self.boss_encounter_fail_events.add(event)
            elif isinstance(event, BossEncounterRetry):
                self.boss_encounter_retry_events.add(event)
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
    
    def get_unique_userIDs(self) -> set[int]:
        """
        Returns the set of unique user IDs.
        
        :return: set of unique user IDs.
        :rtype: set[int]
        """
        uniqueIDs = set()
        for event in self.session_start_events:
            uniqueIDs.add(event.userID)
        return uniqueIDs
    
    def funnel_view(self) -> dict[int, int]:
        """
        Output number of players passing a given stage.
        
        :return: dictionary of key stage number and value number of 
        players left.
        :rtype: dict[int, int]
        """
        funnel = {stage_number: 0 for stage_number in range(1,11)}
        players_remaining: int = len(self.get_unique_userIDs())
        for stage, number_of_fails in self.fail_difficulty_spikes().items():
            players_remaining -= number_of_fails
            funnel[stage] = players_remaining
        return funnel
    
    def health_per_stage(self, sessionID: int) -> dict[int, int]:
        """
        Output the health a session has per stage.
        
        :param sessionID: session stage to check.
        :type sessionID: int
        :return: dictionary of key stage number and value player HP
        remaining.
        :rtype: dict[int, int]
        """
        health_remaining_per_stage = {stage_number: 0 for stage_number in range(1,11)}
        for event in self.boss_encounter_complete_events:
            if event.sessionID == sessionID:
                health_remaining_per_stage[event.stage_number] = event.player_HP_remaining
        for event in self.boss_encounter_complete_events:
            if event.sessionID == sessionID:
                health_remaining_per_stage[event.stage_number] = event.player_HP_remaining
        return health_remaining_per_stage
    
    def get_difficulty(self, sessionID: int) -> Difficulty:
        """
        Get the difficulty for a given session.
        
        :param sessionID: sessionID of the session to get difficulty of.
        :type sessionID: int
        :return: Difficulty value of the session.
        :rtype: Difficulty
        """
        for start_event in self.session_start_events:
            if start_event.sessionID == sessionID:
                return start_event.difficulty
        raise RuntimeError("No session start event for provided " \
        f"session ID: {sessionID}")
    
    def get_sessionIDs_of_difficulty(self, difficulty: Difficulty) -> list[int]:
        """
        Get the list of all sessionIDs of a given difficulty.
        
        :param difficulty: The difficulty level to search for.
        :type difficulty: Difficulty
        :return: The list of sessionIDs with the given difficulty.
        :rtype: list[int]
        """
        difficulty_sessionIDs: list[int] = []
        for start_event in self.session_start_events:
            if start_event.difficulty == difficulty:
                difficulty_sessionIDs.append(start_event.sessionID)
        return difficulty_sessionIDs


def main():
    LogicEngine = EventLogicEngine()
    LogicEngine.categorise_events("example_data.json")
    print(LogicEngine.fail_difficulty_spikes())
    print(LogicEngine.funnel_view())
    # for attr in LogicEngine._attributes:
    #     for event in attr:
    #         print(repr(event))


if __name__ == "__main__":
    main()