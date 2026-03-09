package wizardquest.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.abilities.DamageEnum;
import wizardquest.entity.Dragon;
import wizardquest.entity.EntityInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemyUnitTests {

    private EntityInterface enemy;
    private int startHealth;
    private int baseDamage;

    /**
     * Creates an Enemy object, using the Dragon implementation as an example for
     * these tests.
     * Also assigns the value of the base damage to be inflicted upon the Dragon, to
     * demonstrate
     * how this value varies with different damage types.
     */
    @BeforeEach
    void setUp() {
        enemy = new Dragon(DifficultyEnum.MEDIUM);
        startHealth = enemy.getHealth();
        baseDamage = 10;
    }

    /**
     * If a Dragon has an Absolute attack inflicted upon them, double damage should
     * be dealt.
     */
    @Test
    @DisplayName("Enemy - Relevant damage types deal increased damage")
    void loseHealth_increasedDamageWhereNecessary() {
        enemy.loseHealth(baseDamage, DamageEnum.ABSOLUTE);
        assertEquals(startHealth - (baseDamage * 2), enemy.getHealth());
    }

    /**
     * If a Dragon has a Fire or Thunder attack inflicted upon them, half damage
     * should be dealt.
     * A Fire attack is used in this test to demonstrate this.
     */
    @Test
    @DisplayName("Enemy - Relevant damage types deal reduced damage")
    void loseHealth_reducedDamageWhereNecessary() {
        enemy.loseHealth(baseDamage, DamageEnum.FIRE);
        assertEquals(startHealth - (baseDamage / 2), enemy.getHealth());
    }

    /**
     * If a Dragon has any other damage type inflicted upon them, standard damage
     * should be dealt.
     * A Water attack is used in this test to demonstrate this.
     */
    @Test
    @DisplayName("Enemy - Relevant damage types deal standard damage")
    void loseHealth_standardDamageWhereNecessary() {
        enemy.loseHealth(baseDamage, DamageEnum.WATER);
        assertEquals(startHealth - baseDamage, enemy.getHealth());
    }
}