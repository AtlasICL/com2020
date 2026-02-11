package WizardQuest;

import java.util.List;

public abstract class UpgradeBase implements PlayerInterface {
    protected final PlayerInterface player;

    /**
     * Constructs an upgrade from a player, decorating them.
     *
     * @param player the player to decorate.
     * @throws IllegalArgumentException if the player is null.
     */
    protected UpgradeBase(PlayerInterface player) throws IllegalArgumentException {
        if (player != null) {
            this.player = player;
        } else {
            throw new IllegalArgumentException();
        }
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
    public void loseHealth(int amount, DamageEnum type) throws IllegalArgumentException {
        player.loseHealth(amount, type);
    }

    @Override
    public void resetHealth(){
        player.resetHealth();
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
    public void loseLives(int amount) throws IllegalArgumentException {
        player.loseLives(amount);
    }

    @Override
    public List<UpgradeEnum> getUpgrades() {
        return player.getUpgrades();
    }

    @Override
    public int calcDamage(int amount, DamageEnum type){
        return player.calcDamage(amount, type);
    }

    @Override
    public EntityEnum getType(){
        return player.getType();
    }

    @Override
    public List<AbilityEnum> getAbilities(){
        return player.getAbilities();
    }
}