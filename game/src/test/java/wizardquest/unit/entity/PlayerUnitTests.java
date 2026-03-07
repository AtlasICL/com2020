package wizardquest.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerUnitTests {

    private PlayerInterface player;

    @BeforeEach
    void setUp() {
        player = new Player(DifficultyEnum.MEDIUM);
    }

    @Test
    @DisplayName("Player - Magic Points validated")
    void magic_validated() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.gainMagic(-10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            player.loseMagic(-10);
        });

        int magicBeforeIncrease =  player.getMagic();
        player.gainMagic(10);
        assertEquals(magicBeforeIncrease + 10, player.getMagic());

        int magicToLose =  player.getMagic() + 10;
        player.loseMagic(magicToLose);
        assertEquals(0, player.getMagic());

        player.gainMagic(10);
        player.resetMagic();
        assertEquals(0, player.getMagic());
    }

    @Test
    @DisplayName("Player - Health validated")
    void health_validated() {}

    @Test
    @DisplayName("Player - Coins validated")
    void coins_validated() {}

    @Test
    @DisplayName("Player - Lives validated")
    void lives_validated() {}

    @Test
    @DisplayName("Player - Punch ability owned by default")
    void getUpgrades_punchAlwaysPresent() {}
}
