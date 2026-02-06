/**
 * Holds the player instance and forwards method calls to them
 * 
 * @param player player to decorate
 * 
 * @throws IllegalArgumentException if player doesn't exist
 * */
public abstract class UpgradeBase implements PlayerInterface {
    protected final PlayerInterface player;
    protected UpgradeBase(PlayerInterface player) throws IllegalArgumentException {
        if (player != null) {
            this.player = player;
        } else 
        throw new IllegalArgumentException();
    }
    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public int getMaxHealth() {
        return player.getMaxHealth();
    }

    @Override
    public void takeDamage(int amount) {
        player.takeDamage(amount);
    }

    @Override
    public int getCoins() {
        return player.getCoins();
    }

    @Override
    public void loseCoins(int amount) throws IllegalArgumentException {
        player.loseCoins(amount);
    }

    @Override
    public void gainCoins(int amount) throws IllegalArgumentException  {
        player.gainCoins(amount);
    }

    @Override
    public int getMagic() {
        return player.getMagic();
    }

    @Override
    public int getMaxMagic() {
        return player.getMaxMagic();
    }

    @Override
    public int getMagicRegenRate() {
        return player.getMagicRegenRate();
    }

    @Override
    public void gainMagic(int amount) throws IllegalArgumentException {
        player.gainMagic(amount);
    }

    @Override
    public void loseMagic(int amount) throws IllegalArgumentException {
        player.loseMagic(amount);
    }

    @Override
    public int getLives() {
        return player.getLives();
    }

    @Override
    public void loseLives(int amount) {
        player.loseLives(amount);
    }

    @Override
    public List<UpgradeType> getUpgrades() {
        return player.getUpgrades();
    }
}