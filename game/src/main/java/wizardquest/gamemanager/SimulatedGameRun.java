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

    private final EncounterInterface[] phase1NormalEncounters;
    private final EncounterInterface[] phase2NormalEncounters;
    private final EncounterInterface[] phase3NormalEncounters;

    private final EncounterInterface phase1Boss;
    private final EncounterInterface phase2Boss;
    private final EncounterInterface phase3Boss;
    private final EncounterInterface finalBoss;

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

    private PlayerInterface player;
    private int currentStage;
    private final DifficultyEnum difficulty;
    private final LocalDateTime startTime;
    private final Random random;
    private int deathCount;
    private final int sessionID;

    public SimulatedGameRun() {
        phase1NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.GOBLIN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.FISHMAN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.PYROMANCER_ENCOUNTER, difficulty)
        };
        phase2NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.GOBLIN_DUO_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.GOBLIN_FISHMAN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.ARMOURED_GOBLIN_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.PYROMANCER_FISHMAN_ENCOUNTER, difficulty)
        };
        phase3NormalEncounters = new EncounterInterface[] {
        new Encounter(EncounterEnum.ARMOURED_GOBLIN_PYROMANCER_ENCOUNTER, difficulty),
        new Encounter(EncounterEnum.GOBLIN_FISHMAN_PYROMANCER_ENCOUNTER, difficulty)
        };
        phase1Boss = new Encounter(EncounterEnum.EVIL_WIZARD_ENCOUNTER, difficulty);
        phase2Boss = new Encounter(EncounterEnum.GHOST_ENCOUNTER, difficulty);
        phase3Boss = new Encounter(EncounterEnum.BLACK_KNIGHT_ENCOUNTER, difficulty);
        finalBoss = new Encounter(EncounterEnum.DRAGON_ENCOUNTER, difficulty);
        shopUpgrades = UpgradeEnum.values();
        this.player = new Player(difficulty);
        this.currentStage = 1;
        this.difficulty = difficulty;
        this.startTime = LocalDateTime.now();
        this.random = new Random();
        this.deathCount = 0;
        this.sessionID = sessionID;
    }

    @Override
    public EncounterInterface pickEncounter() throws IllegalStateException {
        return switch (this.currentStage) {
            case 1, 2 -> pickEncounterFrom(this.phase1NormalEncounters);
            case 3 -> this.phase1Boss;
            case 4, 5 -> pickEncounterFrom(this.phase2NormalEncounters);
            case 6 -> this.phase2Boss;
            case 7, 8 -> pickEncounterFrom(this.phase3NormalEncounters);
            case 9 -> this.phase3Boss;
            case 10 -> this.finalBoss;
            default -> null;
        };
    }

    private EncounterInterface pickEncounterFrom(EncounterInterface[] encounters) throws IllegalStateException {
    shuffleArray(encounters);
    for (EncounterInterface encounter : encounters) {
        if (!encounter.isComplete()) {
            return encounter;
        }
    }
    throw new IllegalStateException("Out of Encounters for stage: " + this.currentStage);
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
