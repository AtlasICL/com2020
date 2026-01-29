package WizardQuest;

import java.util.List;

public class Player implements PlayerInterface {
    private int maxMagic;
    private int maxHealth;
    private int health;
    private int magic;
    private int coins;
    private int lives;
    private List<AbilityInterface> abilities;

    public Player() {}

    @Override
    public void loseHealth(int amount, DamageType type) throws IllegalArgumentException {}

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
        return -1;
    }

    @Override
    public List<AbilityInterface> getAbilities() {
        return abilities;
    }

    @Override
    public void resetHealth() {}

    @Override
    public EntityType getType() {
        return null;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void loseCoins(int amount) throws IllegalArgumentException {}

    @Override
    public void gainCoins(int amount) throws IllegalArgumentException {}

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
        return magic;
    }

    @Override
    public void gainMagic(int amount) throws IllegalArgumentException {
        magic += amount;
    }

    @Override
    public void loseMagic(int amount) throws IllegalArgumentException {
        magic -= amount;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void gainLives(int amount) throws IllegalArgumentException {}

    @Override
    public void loseLives(int amount) throws IllegalArgumentException {}

    @Override
    public List<UpgradeType> getUpgrades() {
        return null;
    }
}

