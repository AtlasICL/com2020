package wizardquest.entity;

import java.util.LinkedList;
import java.util.List;

import wizardquest.abilities.AbilityEnum;
import wizardquest.abilities.DamageEnum;
import wizardquest.abilities.UpgradeEnum;
import wizardquest.settings.DifficultyEnum;
import wizardquest.settings.SettingsInterface;
import wizardquest.settings.SettingsSingleton;

public class Player implements PlayerInterface {
    private final int maxMagic;
    private final int maxHealth;
    private int health;
    private int magic;
    private int coins;
    private int lives;
    private DifficultyEnum difficulty;

    public Player(DifficultyEnum difficulty) {
        SettingsInterface settings = SettingsSingleton.getInstance();
        this.maxMagic = settings.getMaxMagic(difficulty);
        this.maxHealth = settings.getPlayerMaxHealth(difficulty);
        this.health = maxHealth;
        this.magic = 0;
        this.coins = 0;
        this.lives = settings.getStartingLives(difficulty);
        this.difficulty = difficulty;
    }

    @Override
    public void loseHealth(int amount, DamageEnum type) throws IllegalArgumentException {
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
    public int calcDamage(int base, DamageEnum type) {
        return base;
    }

    @Override
    public List<AbilityEnum> getAbilities() {
        List<AbilityEnum> l =  new LinkedList<AbilityEnum>();
        l.add(AbilityEnum.PUNCH);
        return l;
    }

    @Override
    public void resetHealth() {
        this.health = this.maxHealth;
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.PLAYER;
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
    public List<UpgradeEnum> getUpgrades() {
        return new LinkedList<UpgradeEnum>();
    }

    @Override
    public void resetMagic(){
        this.magic = 0;
    }
}

