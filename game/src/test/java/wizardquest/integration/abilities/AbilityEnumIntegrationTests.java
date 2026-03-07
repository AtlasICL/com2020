package wizardquest.integration.abilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.FireDamageResistanceUpgrade;
import wizardquest.entity.Dragon;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbilityEnumIntegrationTests {

    /**
     * When a player - who has a damage resistance upgrade for a particular damage type - is
     * attacked by the same damage type, the impact on their health should be reduced.
     * Fire is the damage type used as an example for this test.
     *
     * @throws LackingResourceException if the source entity lacks the magic points to
     * execute an ability.
     */
    @Test
    @DisplayName("AbilityEnum - Executed attack on target entity with Damage Resistance upgrade has reduced damage")
    void execute_upgradedTargetTakesReducedDamage() throws LackingResourceException {
        // Creates two entities: one Player for the source, and one Dragon for the target.
        // Also creates an ability - the Fire Ball, which is of the Fire damage type.
        EntityInterface source = new Dragon(DifficultyEnum.MEDIUM);
        PlayerInterface target = new Player(DifficultyEnum.MEDIUM);
        AbilityEnum ability = AbilityEnum.FIRE_BALL;
        // Decorate the target Player object with a Fire Damage Resistance upgrade.
        PlayerInterface upgradedTarget = new FireDamageResistanceUpgrade(target);
        // As a result, the damage they take from a Fire attack should be half of its base damage.
        int targetHealthBeforeExecution = upgradedTarget.getHealth();
        ability.execute(source, upgradedTarget);
        assertEquals(targetHealthBeforeExecution - (ability.getBaseDamage() / 2), upgradedTarget.getHealth());
    }
}