package wizardquest.integration.gamemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wizardquest.gamemanager.GameRun;
import wizardquest.gamemanager.GameRunInterface;
import wizardquest.settings.DifficultyEnum;

import static org.junit.jupiter.api.Assertions.*;

public class GameRunIntegrationTests {
    private GameRunInterface run;

    /**
     * Instantiate a GameRun object.
     */
    @BeforeEach
    void setUp() {
        run = new GameRun(DifficultyEnum.EASY, -10);
    }

    /**
     * TODO
     */
    @Test
    @DisplayName("GameRun - TODO")
    void upgradePurchaased_appliedToPlayerAndRemovedFromShop(){

    }

    /**
     * TODO
     */
    @Test
    @DisplayName("GameRun - TODO")
    void playerDeath_resetsHealthAndLosesOneLife(){
        
    }
}
