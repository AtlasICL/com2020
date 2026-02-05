public class WaterJetUnlockUpgrade extends UpgradeBase {
    public WaterJetUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.WATER_JET_UNLOCK)){
            u.add(UpgradeType.WATER_JET_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = player.getAbilities();
        if (!a.contains(AbilityType.WATER_JET)){
            a.add(AbilityType.WATER_JET);
        }
        return a;
    }
}