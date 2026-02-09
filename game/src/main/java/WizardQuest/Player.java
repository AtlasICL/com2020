package WizardQuest;

import java.util.LinkedList;
import java.util.List;

public class Player implements PlayerInterface {
    private int maxMagic;
    private int maxHealth;
    private int health;
    private int magic;
    private int coins;
    private int lives;
    private List<AbilityType> abilities;
    private Difficulty difficulty;

    public Player(Difficulty difficulty) {
        SettingsInterface settings = SettingsSingleton.getInstance();
        this.maxMagic = settings.getMaxMagic(difficulty);
        this.maxHealth = settings.getPlayerMaxHealth(difficulty);
        this.health = maxHealth;
        this.magic = 0;
        this.coins = 0;
        this.lives = settings.getStartingLives(difficulty);
        this.abilities = new LinkedList<AbilityType>();
        this.abilities.add(AbilityType.PUNCH);
    }

    @Override
    public void loseHealth(int amount, DamageType type) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player lose a negative amount of health: %d", amount));
        }
        this.health -= amount;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int calcDamage(int base, DamageType type) {
        return base;
    }

    @Override
    public List<AbilityType> getAbilities() {
        return abilities;
    }

    @Override
    public void resetHealth() {
        this.health = this.maxHealth;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void loseCoins(int amount) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player lose a negative amount of coins: %d", amount));
        }
        this.coins -= amount;
    }

    @Override
    public void gainCoins(int amount) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player gain a negative amount of coins: %d", amount));
        }
        this.coins += amount;
    }

    @Override
    public int getMagic() {
        return magic;
    }

    @Override
    public int getMaxMagic() {
        return maxMagic;
    }

    @Override
    public int getMagicRegenRate() {
        return SettingsSingleton.getInstance().getMagicRegenRate(this.difficulty);
    }

    @Override
    public void gainMagic(int amount) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player gain a negative amount of magic: %d", amount));
        }
        this.magic += amount;
    }

    @Override
    public void loseMagic(int amount) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player lose a negative amount of magic: %d", amount));
        }
        this.magic -= amount;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void loseLives(int amount) throws IllegalArgumentException {
        if (amount < 0){
            throw new IllegalArgumentException(String.format("Tried to make the player lose a negative amount of lives: %d", amount));
        }
        this.lives -= amount;
    }

    @Override
    public List<UpgradeType> getUpgrades() {
        return new LinkedList<UpgradeType>();
    }
}

