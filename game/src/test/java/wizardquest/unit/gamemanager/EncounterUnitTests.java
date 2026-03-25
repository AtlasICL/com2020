package wizardquest.unit.gamemanager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import wizardquest.gamemanager.Encounter;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public class EncounterUnitTests {

    /**
     * When an encounter's markComplete method is invoked, its status should
     * be changed from false (starting state) to true.
     */
    @Test
    @DisplayName("Encounter - Encounter marked as complete correctly")
    public void markComplete_setsCorrectly() {
        // Instantiate a new encounter. This should have completion status of false.
        Encounter encounter = new Encounter(EncounterEnum.GOBLIN_ENCOUNTER, DifficultyEnum.MEDIUM);
        assertFalse(encounter.isComplete());
        // After invoking this method, the status should change to true.
        encounter.markComplete();
        assertTrue(encounter.isComplete());
    }
}
