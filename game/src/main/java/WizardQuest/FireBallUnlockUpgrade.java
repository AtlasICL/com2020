public class FireBallUnlockUpgrade extends ConcreteUpgrade {
    public FireBallUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.FIRE_BALL_UNLOCK)){
            u.add(UpgradeType.FIRE_BALL_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = player.getAbilities();
        if (!a.contains(AbilityType.FIRE_BALL)){
            a.add(AbilityType.FIRE_BALL);
        }
        return a;
    }
}