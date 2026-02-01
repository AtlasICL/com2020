public class ThunderStormUnlockUpgrade implements PlayerInterface {
    public ThunderStormUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.THUNDER_STORM_UNLOCK)){
            u.add(UpgradeType.THUNDER_STORM_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = player.getAbilities();
        if (!a.contains(AbilityType.THUNDER_STORM)){
            a.add(AbilityType.THUNDER_STORM);
        }
        return a;
    }
}