from core.logic import EventLogicEngine
   
class SuggestionGenerator:
    def __init__(self, event_logic_engine: EventLogicEngine):
        self.logic_engine = event_logic_engine

    def generate_spike_suggestion(self) -> str:
        """
        Generates a difficulty change suggestion for levels with a
        low pass rate.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.funnel_view_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, averages in spikes.items():
            # Filter to only stages which aren't 0 spikes
            # (i.e. not played yet)
            active_spike_values = \
                [spike for spike in averages.values() if spike > 0]
            if not active_spike_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_spike_values) / len(active_spike_values)
            # Add stages less than mean until 0 spikes stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] > mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")")
        # Return full suggestion
        if suggestion_parts:
            return "Low pass rate.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Increase starting lives.\n"
        return ""


    def generate_low_health_suggestion(self) -> str:
        """
        Generates a health change suggestion for low health.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.compare_health_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, hp_list in spikes.items():
            # Iterate for each difficulty level
            totals = {}
            counts = {}
            for health_per_stage in hp_list:
                for stage, hp_loss in health_per_stage.items():
                    totals[stage] = totals.get(stage, 0) + hp_loss
                    counts[stage] = counts.get(stage, 0) + 1
            averages = {}
            for stage in totals:
                # Calculate average health remaining per stage
                averages[stage] = totals[stage] / counts[stage]

            # Filter to only stages which aren't 0 hp (i.e. not played yet)
            active_hp_values = [hp for hp in averages.values() if hp > 0]
            if not active_hp_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_hp_values) / len(active_hp_values)
            # Add stages less than mean until 0hp stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] < mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")")
        # Return full suggestion
        if suggestion_parts:
            return "High health loss.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Increase max health.\n"
        return ""


    def generate_high_pass_rate_suggestion(self) -> str:
        """
        Generates a difficulty change suggestion for levels with a
        high pass rate.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.funnel_view_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, averages in spikes.items():
            # Filter to only stages which aren't 0 spikes
            # (i.e. not played yet)
            active_spike_values = \
                [spike for spike in averages.values() if spike > 0]
            if not active_spike_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_spike_values) / len(active_spike_values)
            # Add stages less than mean until 0 spikes stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] < mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")")
        # Return full suggestion
        if suggestion_parts:
            return "High pass rate.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Decrease starting lives.\n"
        return ""


    def generate_high_health_suggestion(self) -> str:
        """
        Generates a health change suggestion for maintained high
        health.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.compare_health_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, hp_list in spikes.items():
            # Iterate for each difficulty level
            totals = {}
            counts = {}
            for health_per_stage in hp_list:
                for stage, hp_loss in health_per_stage.items():
                    totals[stage] = totals.get(stage, 0) + hp_loss
                    counts[stage] = counts.get(stage, 0) + 1
            averages = {}
            for stage in totals:
                # Calculate average health remaining per stage
                averages[stage] = totals[stage] / counts[stage]

            # Filter to only stages which aren't 0 hp
            # (i.e. not played yet)
            active_hp_values = [hp for hp in averages.values() if hp > 0]
            if not active_hp_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_hp_values) / len(active_hp_values)
            # Add stages less than mean until 0hp stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] > mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")" )
        # Return full suggestion
        if suggestion_parts:
            return "Low health loss.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Decrease max health.\n"
        return ""


    def generate_low_coin_gain_suggestion(self) -> str:
        """
        Generates a coins change suggestion for low coin gain.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.compare_coins_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, coins_list in spikes.items():
            # Iterate for each difficulty level
            totals = {}
            counts = {}
            for coins_per_stage in coins_list:
                for stage, coins_loss in coins_per_stage.items():
                    totals[stage] = totals.get(stage, 0) + coins_loss
                    counts[stage] = counts.get(stage, 0) + 1
            averages = {}
            for stage in totals:
                # Calculate average coins gained per stage
                averages[stage] = totals[stage] / counts[stage]

            # Filter to only stages which aren't 0 coins gained
            # (i.e. not played yet)
            active_coins_values = \
                [coins for coins in averages.values() if coins > 0]
            if not active_coins_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_coins_values) / len(active_coins_values)
            # Add stages less than mean until 0 coins stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] < mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")" )
        # Return full suggestion
        if suggestion_parts:
            return "Low coin gain.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Increase coins gained per level.\n"
        return ""


    def generate_high_coin_gain_suggestion(self) -> str:
        """
        Generates a coins change suggestion for high coin gain.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.compare_coins_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, coins_list in spikes.items():
            # Iterate for each difficulty level
            totals = {}
            counts = {}
            for coins_per_stage in coins_list:
                for stage, coins_loss in coins_per_stage.items():
                    totals[stage] = totals.get(stage, 0) + coins_loss
                    counts[stage] = counts.get(stage, 0) + 1
            averages = {}
            for stage in totals:
                # Calculate average coins gained per stage
                averages[stage] = totals[stage] / counts[stage]

            # Filter to only stages which aren't 0 coins gained
            # (i.e. not played yet)
            active_coins_values = \
                [coins for coins in averages.values() if coins > 0]
            if not active_coins_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_coins_values) / len(active_coins_values)
            # Add stages less than mean until 0 coins stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] > mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")" )
        # Return full suggestion
        if suggestion_parts:
            return "High coin gain.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Decrease coins gained per level.\n"
        return ""


    def generate_fast_average_time_suggestion(self) -> str:
        """
        Generates a health change suggestion for a fast average time
        to complete a given stage.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.average_time_to_complete_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, averages in spikes.items():
            # Filter to only stages which aren't 0 seconds
            # (i.e. not played yet)
            active_time_values = [time for time in averages.values() if time > 0]
            if not active_time_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_time_values) / len(active_time_values)
            # Add stages less than mean until 0 spikes stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] < mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")")
        # Return full suggestion
        if suggestion_parts:
            return "Fast average completion time.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Increase number of enemies per stage.\n"
        return ""



    def generate_slow_average_time_suggestion(self) -> str:
        """
        Generates a health change suggestion for a slow average time
        to complete a given stage.

        :return: Suggestion text.
        :rtype: str
        """
        spikes = self.logic_engine.average_time_to_complete_per_stage_per_difficulty()
        # Suggestion parts are the full suggestion
        suggestion_parts = []
        for difficulty, averages in spikes.items():
            # Filter to only stages which aren't 0 seconds
            # (i.e. not played yet)
            active_time_values = \
                [time for time in averages.values() if time > 0]
            if not active_time_values:
                continue
            # Calculate average based on filtered values
            mean = sum(active_time_values) / len(active_time_values)
            # Add stages less than mean until 0 spikes stage
            stages_flagged = []
            for stage in averages.keys():
                if averages[stage] > mean:
                    stages_flagged.append(str(stage))
                if averages[stage] == 0:
                    break

            # Create list of difficulties to stages string
            if stages_flagged:
                suggestion_parts.append(f"{str(difficulty.value)} (" +
                                        ", ".join(stages_flagged) + ")")
        # Return full suggestion
        if suggestion_parts:
            return "Slow average completion time.\nImpacted Stages: " + \
                   "".join(suggestion_parts) + \
                   "\nSuggestion: Decrease number of enemies per stage.\n"
        return ""
        