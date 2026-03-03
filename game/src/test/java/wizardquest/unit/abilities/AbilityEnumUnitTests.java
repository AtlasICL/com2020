package wizardquest.unit.abilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import wizardquest.abilities.AbilityEnum;
import wizardquest.entity.Dragon;
import wizardquest.entity.EntityInterface;
import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.*;

public class AbilityEnumUnitTests {

    private PlayerInterface source;
    private EntityInterface target;
    private AbilityEnum freeAbility;
    private AbilityEnum magicAbility;

    /**
     * Creates two entities: one Player for the source, and one Dragon for the target.
     * Also creates two abilities: one being the Punch, which is free (costs 0 magic points),
     * and the other being the Absolute Pulse, which costs 20 magic points.
     */
    @BeforeEach
    public void setUp() {
        source = new Player(DifficultyEnum.MEDIUM);
        target = new Dragon(DifficultyEnum.MEDIUM);
        freeAbility = AbilityEnum.PUNCH;
        magicAbility = AbilityEnum.ABSOLUTE_PULSE;
    }

    /**
     * When an entity tries to execute an ability, they should have enough magic points to
     * do so. If not, LackingResourceException should be thrown.
     */
    @Test
    @DisplayName("AbilityEnum - Magic Points validated before execution")
    void execute_magicPointsValidated() {
        // The player is instantiated with 0 magic points.
        // No LackingResourceException should be thrown for executing the Punch ability,
        // since it is free.
        assertDoesNotThrow(() -> {
            freeAbility.execute(source, target);
        });
        // However, attempting to execute the Absolute Pulse ability should throw the
        // LackingResourceException, as the player is 20 magic points short.
        assertThrows(LackingResourceException.class, () -> {
            magicAbility.execute(source, target);
        });
        // Increase the player's magic points, so that they can afford the Absolute Pulse
        // ability. Now, no LackingResourceException should be thrown for executing it.
        source.gainMagic(magicAbility.getMagicCost());
        assertDoesNotThrow(() -> {
            magicAbility.execute(source, target);
        });
    }

    /**
     * When an entity executes an ability, their magic points should be reduced by the magic
     * cost of the ability used.
     *
     * @throws LackingResourceException if the source entity lacks the magic points to
     * execute an ability.
     */
    @Test
    @DisplayName("AbilityEnum - Magic Points reduced after ability execution")
    void execute_magicPointsReduced() throws LackingResourceException {
        // This test begins with the player on enough magic points to afford the Absolute
        // Pulse ability.
        source.gainMagic(magicAbility.getMagicCost());
        // Executing the Punch ability should not impact the player's magic points, since
        // it is free.
        int magicBeforeExecution = source.getMagic();
        freeAbility.execute(source, target);
        assertEquals(magicBeforeExecution, source.getMagic());
        // Executing the Absolute Pulse ability should reduce the player's magic points by
        // the magic cost of the ability.
        magicAbility.execute(source, target);
        assertEquals(magicBeforeExecution - magicAbility.getMagicCost(), source.getMagic());
    }

    @Test
    @DisplayName("AbilityEnum - Source Entity validated before execution")
    void execute_sourceEntityValidated() {
        // Instantiate a null Player object. This should throw a RuntimeException if it tries to
        // execute an ability.
        Player nullSource = null;
        assertThrows(RuntimeException.class, () -> {
            freeAbility.execute(nullSource, target);
        });
        // No RuntimeException should be thrown when a non-null Player object tries to execute an
        // ability.
        assertDoesNotThrow(() -> {
            freeAbility.execute(source, target);
        });
    }
}