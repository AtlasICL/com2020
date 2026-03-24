package wizardquest.unit.gamemanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.gamemanager.Encounter;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.gamemanager.EncounterInterface;
import wizardquest.gamemanager.GameManagerSingleton;
import wizardquest.gamemanager.GameRun;
import wizardquest.gamemanager.GameRunInterface;
import wizardquest.gamemanager.LackingResourceException;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsSingleton;

import static org.junit.jupiter.api.Assertions.*;

public class GameRunUnitTests {

    GameRunInterface run;

    /**
     * Instantiate a GameRun object.
     */
    @BeforeEach
    void setUp() {
        run = new GameRun(DifficultyEnum.MEDIUM, -10);
    }

    /**
     * Checks the pick encounter function provides encounters from the correct pools
     * for the game
     */
    @Test
    @DisplayName("GameRun - Encounters picked from the correct pool")
    void pickEncounter_picksFromCorrectPool() {
        List<EncounterInterface> phase1Encounters = new ArrayList<>();
        List<EncounterInterface> phase2Encounters = new ArrayList<>();
        List<EncounterInterface> phase3Encounters = new ArrayList<>();

        List<EncounterEnum> expectedPhase1 = Arrays.asList(new EncounterEnum[] {
                EncounterEnum.GOBLIN_ENCOUNTER,
                EncounterEnum.FISHMAN_ENCOUNTER,
                EncounterEnum.PYROMANCER_ENCOUNTER });

        List<EncounterEnum> expectedPhase2 = Arrays.asList(new EncounterEnum[] {
                EncounterEnum.GOBLIN_DUO_ENCOUNTER,
                EncounterEnum.GOBLIN_FISHMAN_ENCOUNTER,
                EncounterEnum.ARMOURED_GOBLIN_ENCOUNTER,
                EncounterEnum.PYROMANCER_FISHMAN_ENCOUNTER });

        List<EncounterEnum> expectedPhase3 = Arrays.asList(new EncounterEnum[] {
                EncounterEnum.ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER,
                EncounterEnum.GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER });

        for (int i = 0; i < 3; i++) {
            phase1Encounters.add(run.pickEncounter());
            phase1Encounters.get(phase1Encounters.size() - 1).markComplete();
        }

        // Check that trying to access another encounter causes the system to throw an
        // error
        assertThrows(IllegalStateException.class, () -> run.pickEncounter());

        // Advance from stage 1 to 3
        run.nextStage();
        run.nextStage();

        // Check stage 3 boss is the evil wizard
        assertEquals(EncounterEnum.EVIL_WIZARD_ENCOUNTER, run.pickEncounter().getType());

        // Advance to stage 4
        run.nextStage();

        for (int i = 0; i < 4; i++) {
            phase2Encounters.add(run.pickEncounter());
            phase2Encounters.get(phase2Encounters.size() - 1).markComplete();
        }

        // Advance from stage 4 to 6
        run.nextStage();
        run.nextStage();

        // Check stage 6 boss
        assertEquals(EncounterEnum.GHOST_ENCOUNTER, run.pickEncounter().getType());

        // Advance to stage 7
        run.nextStage();

        for (int i = 0; i < 2; i++) {
            phase3Encounters.add(run.pickEncounter());
            phase3Encounters.get(phase3Encounters.size() - 1).markComplete();
        }

        // Advance from stage 7 to 9
        run.nextStage();
        run.nextStage();

        // Check stage 9 boss
        assertEquals(EncounterEnum.BLACK_KNIGHT_ENCOUNTER, run.pickEncounter().getType());

        run.nextStage();

        // Check final boss
        assertEquals(EncounterEnum.DRAGON_ENCOUNTER, run.pickEncounter().getType());

        // Check normal encounters
        for (EncounterInterface e : phase1Encounters) {
            assertTrue(expectedPhase1.contains(e.getType()));
        }

        for (EncounterInterface e : phase2Encounters) {
            assertTrue(expectedPhase2.contains(e.getType()));
        }

        for (EncounterInterface e : phase3Encounters) {
            assertTrue(expectedPhase3.contains(e.getType()));
        }
    }

    /**
     * Checks the number of upgrades in the shop is correct
     */
    @Test
    @DisplayName("GameRun - Number of items in the shop is correct")
    void shopItemCount_isCorrectAmount() {
        for (int i = 0; i < 10; i++) {
            assertEquals(SettingsSingleton.getInstance().getShopItemCount(DifficultyEnum.MEDIUM),
                    run.viewShop().length);
        }
    }

    /**
     * Checks that purchasing an upgrade from the shop reduces the number of coins
     * the player has by its price
     */
    @Test
    @DisplayName("GameRun - Player's coins are reduced by the upgrade prices")
    void upgradePurchased_reducedPlayerCoins() {
        run.getPlayer().gainCoins(100);
        try {
            run.purchaseUpgrade(UpgradeEnum.ABSOLUTE_PULSE_UNLOCK);
        } catch (LackingResourceException e) {
            fail("Threw exception" + e.getMessage());
        }
        // Check purchasing one upgrade has the correct price
        assertEquals(90, run.getPlayer().getCoins());

        try {
            run.purchaseUpgrade(UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE);
            run.purchaseUpgrade(UpgradeEnum.IMPROVED_FIRE_DAMAGE);
            run.purchaseUpgrade(UpgradeEnum.FIRE_BALL_UNLOCK);
        } catch (LackingResourceException e) {
            fail("Threw exception" + e.getMessage());
        }

        // Check purchasing multiple upgrades has the correct price
        assertEquals(25, run.getPlayer().getCoins());
    }

    /**
     * Checks that if a player cant afford an upgrade, the game throws an exception.
     */
    @Test
    @DisplayName("GameRun - The player attempting to buy unaffordable upgrade throws exception")
    void unaffordableUpgradePurchased_thorwsException() {
        // check player starts with 0 coins
        assertEquals(0, run.getPlayer().getCoins());

        // Check player cant afford an upgrade with 0 coins
        assertThrows(LackingResourceException.class, () -> run.purchaseUpgrade(UpgradeEnum.THUNDER_STORM_UNLOCK));

        // Check player cant afford 15 coin upgrade with 14 coins
        run.getPlayer().gainCoins(14);
        assertThrows(LackingResourceException.class, () -> run.purchaseUpgrade(UpgradeEnum.SLASH_UNLOCK));

    }

}
