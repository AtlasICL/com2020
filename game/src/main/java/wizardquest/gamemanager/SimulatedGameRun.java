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

    private final UpgradeEnum[] shopUpgrades = new UpgradeEnum[] {
            UpgradeEnum.PHYSICAL_DAMAGE_RESISTANCE,
            UpgradeEnum.FIRE_DAMAGE_RESISTANCE,
            UpgradeEnum.WATER_DAMAGE_RESISTANCE,
            UpgradeEnum.THUNDER_DAMAGE_RESISTANCE,
            UpgradeEnum.IMPROVED_PHYSICAL_DAMAGE,
            UpgradeEnum.IMPROVED_FIRE_DAMAGE,
            UpgradeEnum.IMPROVED_WATER_DAMAGE,
            UpgradeEnum.IMPROVED_THUNDER_DAMAGE,
            UpgradeEnum.SLASH_UNLOCK,
            UpgradeEnum.ABSOLUTE_PULSE_UNLOCK,
            UpgradeEnum.WATER_JET_UNLOCK,
            UpgradeEnum.FIRE_BALL_UNLOCK,
            UpgradeEnum.THUNDER_STORM_UNLOCK
    };

    public SimulatedGameRun() {

    }

    @Override
    public EncounterInterface pickEncounter() throws IllegalStateException {

        throw new UnsupportedOperationException();
    }

    @Override
    public UpgradeEnum[] viewShop() {

        shuffleArray(this.shopUpgrades);

        int shopSize = Math.min(3, shopUpgrades.length); // placeholder for design parameter
        UpgradeEnum[] shop = new UpgradeEnum[shopSize];

        int i = 0;

        for (UpgradeEnum u : shopUpgrades) {
            if (u != null) {
                shop[i] = u;
                i++;

                if (i == shopSize) {
                    break;
                }
            }
        }

        return shop;
    }ow new UnsupportedOperationException();
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
