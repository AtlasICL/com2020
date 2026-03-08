package wizardquest.integration.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.entity.EntityEnum;
import wizardquest.entity.EntityInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityEnumIntegrationTests {

    /**
     * Create a new instance of an Enemy using reflection. Upon creation, their starting
     * health should equate to their max health.
     */
    @Test
    @DisplayName("EntityEnum - Enemy starts with max health after creation")
    void createEnemy_enemyStartsWithMaxHealth() {
        EntityInterface enemy = EntityEnum.DRAGON.createEnemy(DifficultyEnum.MEDIUM);
        assertEquals(enemy.getHealth(), enemy.getMaxHealth());
    }
}
