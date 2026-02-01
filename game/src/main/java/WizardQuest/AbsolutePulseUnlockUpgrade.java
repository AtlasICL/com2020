public class AbsolutePulseUnlockUpgrade extends ConcreteUpgrade {
    public AbsolutePulseUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = player.getUpgrades();
        if (!u.contains(UpgradeType.ABSOLUTE_PULSE_UNLOCK)){
            u.add(UpgradeType.ABSOLUTE_PULSE_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = player.getAbilities();
        if (!a.contains(AbilityType.ABSOLUTE_PULSE)){
            a.add(AbilityType.ABSOLUTE_PULSE);
        }
        return a;
    }
}