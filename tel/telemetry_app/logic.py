import os
from dataclasses import dataclass, field

from events import *
from parsing import parse_file, ValidEvent

class EventLogicEngine:
    def __init__(self):
        self.start_telemetry_events: set[StartTelemetry] = set()
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
            self.start_telemetry_events, 
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
        events: list[ValidEvent] = parse_file(filename)
        for event in events:
            if isinstance(event, StartTelemetry):
                self.start_telemetry_events.add(event)
            elif isinstance(event, SessionStart):
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

def main():
    LogicEngine = EventLogicEngine()
    LogicEngine.categorise_events("example_data.json")
    for attr in LogicEngine._attributes:
        for event in attr:
            print(repr(event))



if __name__ == "__main__":
    main()