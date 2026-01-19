import os
from dataclasses import dataclass, field

from events import *
from parsing import parse_file, ValidEvent


class LogicEngine:
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
    le = LogicEngine()
    le.categorise_events("example_data.json")

    for e in le.boss_encounter_complete_events:
        print(repr(e))


if __name__ == "__main__":
    main()