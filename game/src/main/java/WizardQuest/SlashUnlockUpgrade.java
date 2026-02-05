public class SlashUnlockUpgrade extends UpgradeBase {
    public SlashUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override //TODO change getters so they don't modify state.
    public List<UpgradeType> getUpgrades(){
        List u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.SLASH_UNLOCK)){
            u.add(UpgradeType.SLASH_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = super.player.getAbilities();
        if (!a.contains(AbilityType.SLASH)){
            a.add(AbilityType.SLASH);
        }
        return a;
    }
}