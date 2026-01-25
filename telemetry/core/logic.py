from core.events import *
from core.parsing import parse_file, ValidEvent

class EventLogicEngine:
    def __init__(self):
        self.session_start_events: set[
            SessionStart
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

        self._attributes = [ 
            self.session_start_events, 
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


    def categorise_events(self, filename: str) -> None:
        """
        Creates objects from the json file provided. 

        :param filename: json file with custom schema.
        :type filename: str
        """
        # TODO: We would need to implement __eq__ methods for our 
        # event classes if we want the set() functionality of 
        # avoiding duplicates. Until then, temporary fix: clear the 
        # attributes before reading them back in again.
        for attr in self._attributes:
            attr.clear()
        
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
    

    def get_number_of_session_starts(self) -> int:
        """
        Get the number of session start events.        

        :return: Returns the number of unique session start events.
        :rtype: int
        """
        return len(self.session_start_events)
    
    
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
    

    def count_starts(self, stage_number) -> int:
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
       

    def count_fails(self, stage_number) -> int:
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


def main():
    LogicEngine = EventLogicEngine()
    LogicEngine.categorise_events("example_data3.json")
    print(LogicEngine.fail_difficulty_spikes())
    print(LogicEngine.funnel_view())
    # for attr in LogicEngine._attributes:
    #     for event in attr:
    #         print(repr(event))


if __name__ == "__main__":
    main()