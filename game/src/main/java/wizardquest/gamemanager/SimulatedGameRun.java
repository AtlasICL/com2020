package wizardquest.gamemanager;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import wizardquest.abilities.UpgradeEnum;
import wizardquest.entity.PlayerInterface;
import wizardquest.settings.DifficultyEnum;

/**
 */
public class SimulatedGameRun implements GameRunInterface {

    public SimulatedGameRun() {

    }

    @Override
    public EncounterInterface pickEncounter() throws IllegalStateException {

        throw new UnsupportedOperationException();
    }

    @Override
    public UpgradeEnum[] viewShop() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException {

        throw new UnsupportedOperationException();
    }

    @Override
    public PlayerInterface getPlayer() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void nextStage() {

        throw new UnsupportedOperationException();
    }

    @Override
    public int getStage() {

        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime getRunStartTime() {

        throw new UnsupportedOperationException();
    }

    @Override
    public int getDeathCount() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementDeathCount() {

        throw new UnsupportedOperationException();
    }

    @Override
    public DifficultyEnum getDifficulty() {

        throw new UnsupportedOperationException();
    }

    @Override
    public int getSessionID() {

        throw new UnsupportedOperationException();
    }

    private Instant getTimestamp() {

        throw new UnsupportedOperationException();
    }
}
