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
    }

@Override
    public void purchaseUpgrade(UpgradeEnum upgrade) throws LackingResourceException {

        if (upgrade.getPrice() > player.getCoins()) {

            int difference = upgrade.getPrice() - player.getCoins();

            throw new LackingResourceException(
                    "Not enough coins to purchase this upgrade. " + difference + " more coins needed.");

        } else {

            removeUpgradeFromPool(upgrade);

            player.loseCoins(upgrade.getPrice());

            player = upgrade.applyUpgrade(player);
        }
    }

    @Override
    public PlayerInterface getPlayer() {
        return player;
    }

    @Override
    public void nextStage() {
        currentStage++;
    }

    @Override
    public int getStage() {
        return currentStage;
    }

    @Override
    public LocalDateTime getRunStartTime() {
        return startTime;
    }

    @Override
    public int getDeathCount() {
        return deathCount;
    }

    @Override
    public void incrementDeathCount() {
        player.loseLives(1);
        deathCount++;
        player.resetHealth();
    }

    @Override
    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    @Override
    public int getSessionID() {
        return sessionID;
    }

    private Instant getTimestamp() {
        return Instant.now(); // not dynamic yet
    }

        private void removeUpgradeFromPool(UpgradeEnum upgrade) {

        for (int i = 0; i < shopUpgrades.length; i++) {

            if (shopUpgrades[i] == upgrade) {
                shopUpgrades[i] = null;
            }
        }
    }

    private <T> void shuffleArray(T[] arr) {

        for (int i = arr.length - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
