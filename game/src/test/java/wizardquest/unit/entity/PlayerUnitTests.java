package wizardquest.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerUnitTests {

    private PlayerInterface player;

    /**
     * Create a Player object to be used in each test.
     */
    @BeforeEach
    void setUp() {
        player = new Player(DifficultyEnum.MEDIUM);
    }

    /**
     * Players can gain or lose magic points.
     * They should not be able to gain or lose a negative amount of magic. If this
     * is attempted, IllegalArgumentException should be thrown.
     * Their magic should always be in the range of 0 to their maxMagic.
     * Resetting their magic should equate it to 0.
     */
    @Test
    @DisplayName("Player - Magic Points validated")
    void magic_validated() {
        // If the Player tries to gain or lose a negative amount of magic, then
        // IllegalArgumentException should be thrown.
        assertThrows(IllegalArgumentException.class, () -> {
            player.gainMagic(-10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            player.loseMagic(-10);
        });
        // If the player attempts to gain an amount of magic that would see
        // them exceed their maxMagic, their magic should be equated to maxMagic.
        player.gainMagic(player.getMaxMagic() + 10);
        assertEquals(player.getMaxMagic(), player.getMagic());
        // If the player attempts to lose an amount of magic that would see
        // them fall below 0, their magic should be equated to 0.
        int magicToLose =  player.getMagic() + 10;
        player.loseMagic(magicToLose);
        assertEquals(0, player.getMagic());
        // If the player's magic is reset, it should be equated to 0.
        player.gainMagic(10);
        player.resetMagic();
        assertEquals(0, player.getMagic());
    }

    /**
     * Players can lose health.
     * They should not be able to lose a negative amount of health. If this
     * is attempted, IllegalArgumentException should be thrown.
     * Their health should always be in the range of 0 to their maxHealth.
     * Resetting their health should equate it to 0.
     */
    @Test
    @DisplayName("Player - Health validated")
    void health_validated() {
        // If the Player tries to lose a negative amount of health, then
        // IllegalArgumentException should be thrown.
        assertThrows(IllegalArgumentException.class, () -> {
            player.loseHealth(-10, DamageEnum.ABSOLUTE);
        });
        // If the player attempts to lose an amount of health that would see
        // them fall below 0, their health should be equated to 0.
        int healthToLose =  player.getHealth() + 10;
        player.loseHealth(healthToLose, DamageEnum.ABSOLUTE);
        assertEquals(0, player.getHealth());
        // If the player's health is reset, it should be equated to their maxHealth.
        player.resetHealth();
        assertEquals(player.getMaxHealth(), player.getHealth());
    }

    /**
     * Players can gain or lose coins.
     * They should not be able to gain or lose a negative amount of coins. If this
     * is attempted, IllegalArgumentException should be thrown.
     * Their coins should always be non-negative.
     */
    @Test
    @DisplayName("Player - Coins validated")
    void coins_validated() {
        // If the Player tries to gain or lose a negative amount of coins, then
        // IllegalArgumentException should be thrown.
        assertThrows(IllegalArgumentException.class, () -> {
            player.gainCoins(-10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            player.loseCoins(-10);
        });
        // If the player attempts to lose an amount of health that would see
        // them fall below 0, their health should be equated to 0.
        int coinsToLose =  player.getCoins() + 10;
        player.loseCoins(coinsToLose);
        assertEquals(0, player.getCoins());
    }

    /**
     * Players can lose lives.
     * They should not be able to lose a negative amount of lives. If this
     * is attempted, IllegalArgumentException should be thrown.
     * Their lives should always be non-negative.
     */
    @Test
    @DisplayName("Player - Lives validated")
    void lives_validated() {
        // If the Player tries to lose a negative amount of lives, then
        // IllegalArgumentException should be thrown.
        assertThrows(IllegalArgumentException.class, () -> {
            player.loseLives(-10);
        });
        // If the player attempts to lose an amount of lives that would see
        // them fall below 0, their lives should be equated to 0.
        int livesToLose =  player.getLives() + 10;
        player.loseLives(livesToLose);
        assertEquals(0, player.getLives());
    }

    /**
     * When a Player is instantiated, they should have the Punch ability by default.
     * Without any abilities from the start, they would be unable to play the game.
     */
    @Test
    @DisplayName("Player - Punch ability owned by default")
    void getUpgrades_punchAlwaysPresent() {
        // Other than instantiation in the BeforeEach method, nothing has happened
        // to this Player object - it is a base Player.
        assertTrue(player.getAbilities().contains(AbilityEnum.PUNCH));
    }
}
