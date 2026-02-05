public class AbsolutePulseUnlockUpgrade extends UpgradeBase {
    public AbsolutePulseUnlockUpgrade(PlayerInterface player){
        super(player);
    }

    @Override
    public List<UpgradeType> getUpgrades(){
        List u = super.player.getUpgrades();
        if (!u.contains(UpgradeType.ABSOLUTE_PULSE_UNLOCK)){
            u.add(UpgradeType.ABSOLUTE_PULSE_UNLOCK);
        }
        return u;
    }

    @Override
    public List<AbilityType> getAbilities(){
        List a = super.player.getAbilities();
        if (!a.contains(AbilityType.ABSOLUTE_PULSE)){
            a.add(AbilityType.ABSOLUTE_PULSE);
        }
        return a;
    }
}