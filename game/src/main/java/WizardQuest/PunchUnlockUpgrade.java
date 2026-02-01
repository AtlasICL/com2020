public class PunchUnlockUpgrade implements PlayerInterface {
    public PunchUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.PUNCH_UNLOCK)){
            u.add(UpgradeType.PUNCH_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = player.getAbilities();
        if (!a.contains(AbilityType.PUNCH)){
            a.add(AbilityType.PUNCH);
        }
        return a;
    }
}