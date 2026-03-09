package wizardquest.unit.abilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.AbsolutePulseUnlockUpgrade;
import wizardquest.abilities.DamageEnum;
import wizardquest.abilities.FireDamageResistanceUpgrade;
import wizardquest.abilities.ImprovedFireDamageUpgrade;
import wizardquest.abilities.UpgradeBase;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.Player;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpgradeUnitTests {

    private PlayerInterface player;

    /**
     * Creates a Player, who will be used to test the impact of different abilities.
     */
    @BeforeEach
    void setUp() {
        player = new Player(DifficultyEnum.MEDIUM);
    }

    /**
     * When accessing the list of upgrades through an implementation of UpgradeBase,
     * that
     * implementation should be present.
     * AbsolutePulseUnlockUpgrade is the implementation used as an example for this
     * test.
     */
    @Test
    @DisplayName("UpgradeBase - Upgrade present in list")
    void getUpgrades_upgradePresentInList() {
        // Apply the AbsolutePulseUnlockUpgrade to the player.
        UpgradeBase unlockUpgrade = new AbsolutePulseUnlockUpgrade(player);
        assertTrue(unlockUpgrade.getUpgrades().contains(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK));
    }

    /**
     * When accessing the list of abilities through an implementation of
     * UpgradeBase, that
     * implementation should be present.
     * AbsolutePulseUnlockUpgrade is the implementation used as an example for this
     * test.
     */
    @Test
    @DisplayName("UpgradeBase - Ability present in list")
    void getAbilities_abilityPresentInList() {
        // Apply the AbsolutePulseUnlockUpgrade to the player.
        UpgradeBase unlockUpgrade = new AbsolutePulseUnlockUpgrade(player);
        assertTrue(unlockUpgrade.getAbilities().contains(AbilityEnum.ABSOLUTE_PULSE));
    }

    /**
     * When an improved damage upgrade is applied to a player, any attacks they
     * inflict of the corresponding damage type should deal double damage.
     * ImprovedFireDamageUpgrade is the implementation used as an example for this
     * test.
     */
    @Test
    @DisplayName("UpgradeBase - Damage Upgrade increases damage inflicted")
    void calcDamage_damageUpgradeIncreasesDamage() {
        // Apply the ImprovedFireDamageUpgrade to the player.
        UpgradeBase damageUpgrade = new ImprovedFireDamageUpgrade(player);

        // A fire attack inflicted by the player should deal double damage as a result.
        int baseDamage = 10;
        assertEquals(baseDamage * 2, damageUpgrade.calcDamage(baseDamage, DamageEnum.FIRE));

        // However, attacks of other types should still deal standard damage.
        assertEquals(baseDamage, damageUpgrade.calcDamage(baseDamage, DamageEnum.ABSOLUTE));
    }

    /**
     * When a damage resistance upgrade is applied to a player, any attacks
     * inflicted upon them of the corresponding damage type should deal half damage.
     * FireDamageResistanceUpgrade is the implementation used as an example for this
     * test.
     */
    @Test
    @DisplayName("UpgradeBase - Resistance Upgrade reduces damage taken")
    void loseHealth_resistanceUpgradeReducesDamage() {
        // Apply the FireDamageResistanceUpgrade to the player.
        UpgradeBase resistanceUpgrade = new FireDamageResistanceUpgrade(player);
        int playerHealthBeforeAttack = player.getHealth();

        // A fire attack inflicted on the player should deal half damage as a result.
        int baseDamage = 10;
        resistanceUpgrade.loseHealth(baseDamage, DamageEnum.FIRE);
        assertEquals(playerHealthBeforeAttack - (baseDamage / 2), player.getHealth());

        // However, attacks of other types should still inflict standard damage.
        playerHealthBeforeAttack = player.getHealth();
        resistanceUpgrade.loseHealth(baseDamage, DamageEnum.ABSOLUTE);
        assertEquals(playerHealthBeforeAttack - baseDamage, player.getHealth());
    }

    /**
     * When an upgrade is applied to a player, this should be represented in their
     * list of owned upgrades.
     * AbsolutePulseUnlockUpgrade is the implementation used as an example for this
     * test.
     */
    @Test
    @DisplayName("UpgradeEnum - Upgrade applied to Player")
    void applyUpgrade_upgradeAppliedToPlayer() {
        // Initially, the upgrade should not belong to the player.
        assertFalse(player.getUpgrades().contains(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK));

        // Apply the AbsolutePulseUnlockUpgrade to the player.
        UpgradeEnum upgrade = UpgradeEnum.ABSOLUTE_PULSE_UNLOCK;

        // Decorate the Player object with this upgrade. This should now appear as an
        // upgrade that belongs to them.
        player = upgrade.applyUpgrade(player);
        assertTrue(player.getUpgrades().contains(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK));
    }
}