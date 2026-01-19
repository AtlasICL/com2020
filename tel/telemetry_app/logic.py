from dataclasses import dataclass, field

from telemetry_app.events import *
from telemetry_app.parsing import parse_file, ValidEvent

@dataclass
class EventCategories:
    start_telemetry_events: set[StartTelemetry] = field(default_factory=set)
    session_start_events: set[SessionStart] = field(default_factory=set)
    end_session_events: set[EndSession] = field(default_factory=set)
    normal_encounter_start_events: set[NormalEncounterStart] = field(default_factory=set)
    normal_encounter_complete_events: set[NormalEncounterComplete] = field(default_factory=set)
    normal_encounter_fail_events: set[NormalEncounterFail] = field(default_factory=set)
    normal_encounter_retry_events: set[NormalEncounterRetry] = field(default_factory=set)
    boss_encounter_start_events: set[BossEncounterStart] = field(default_factory=set)
    boss_encounter_complete_events: set[BossEncounterComplete] = field(default_factory=set)
    boss_encounter_fail_events: set[BossEncounterFail] = field(default_factory=set)
    boss_encounter_retry_events: set[BossEncounterRetry] = field(default_factory=set)
    gain_coin_events: set[GainCoin] = field(default_factory=set)
    buy_upgrade_events: set[BuyUpgrade] = field(default_factory=set)
    settings_change_events: set[SettingsChange] = field(default_factory=set)
    kill_enemy_events: set[KillEnemy] = field(default_factory=set)

def categorise_events(events: list[ValidEvent]) -> None:
    for event in events:
        if isinstance(event, StartTelemetry):
            EventCategories.start_telemetry_events.add(event)
        elif isinstance(event, SessionStart):
            EventCategories.session_start_events.add(event)
        elif isinstance(event, EndSession):
            EventCategories.end_session_events.add(event)
        elif isinstance(event, NormalEncounterStart):
            EventCategories.normal_encounter_start_events.add(event)
        elif isinstance(event, NormalEncounterComplete):
            EventCategories.normal_encounter_complete_events.add(event)
        elif isinstance(event, NormalEncounterFail):
            EventCategories.normal_encounter_fail_events.add(event)
        elif isinstance(event, NormalEncounterRetry):
            EventCategories.normal_encounter_retry_events.add(event)
        elif isinstance(event, BossEncounterStart):
            EventCategories.boss_encounter_start_events.add(event)
        elif isinstance(event, BossEncounterComplete):
            EventCategories.boss_encounter_complete_events.add(event)
        elif isinstance(event, BossEncounterFail):
            EventCategories.boss_encounter_fail_events.add(event)
        elif isinstance(event, BossEncounterRetry):
            EventCategories.boss_encounter_retry_events.add(event)
        elif isinstance(event, GainCoin):
            EventCategories.gain_coin_events.add(event)
        elif isinstance(event, BuyUpgrade):
            EventCategories.buy_upgrade_events.add(event)
        elif isinstance(event, SettingsChange):
            EventCategories.settings_change_events.add(event)
        elif isinstance(event, KillEnemy):
            EventCategories.kill_enemy_events.add(event)
        


