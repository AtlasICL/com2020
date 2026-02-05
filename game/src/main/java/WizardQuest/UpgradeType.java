package WizardQuest;

public enum UpgradeType { //"Upgrade" is referred to as "passive ability externally"
    //organized as price, class, telemetry name
    ABSOLUTE_PULSE(-1, null, "Absolute Pulse"), // PLACEHOLDER ARGS
    SLASH(-1, null, "Slash"), // PLACEHOLDER ARGS
    WATER_JET(-1, null, "Water Jet"), // PLACEHOLDER ARGS
    THUNDER_STORM(-1, null, "Thunder Storm"), // PLACEHOLDER ARGS
    FIRE_BALL(-1, null, "Fire Ball"), // PLACEHOLDER ARGS
    PHYSICAL_DAMAGE_RESISTANCE(-1, null, "Physical Damage Resistance"), // PLACEHOLDER ARGS
    FIRE_DAMAGE_RESISTANCE(-1, null, "Fire Damage Resistance"), // PLACEHOLDER ARGS
    WATER_DAMAGE_RESISTANCE(-1, null, "Water Damage Resistance"), // PLACEHOLDER ARGS
    THUNDER_DAMAGE_RESISTANCE(-1, null, "Thunder Damage Resistance"), // PLACEHOLDER ARGS
    IMPROVED_PHYSICAL_DAMAGE(-1, null, "Improved Physical Damage"), // PLACEHOLDER ARGS
    IMPROVED_FIRE_DAMAGE(-1, null, "Improved Fire Damage"), // PLACEHOLDER ARGS
    IMPROVED_WATER_DAMAGE(-1, null, "Improved Water Damage"), // PLACEHOLDER ARGS
    IMPROVED_THUNDER_DAMAGE(-1, null, "Improved Thunder Damage"),; // PLACEHOLDER ARGS

    private final int price;
    private final Class<? extends PlayerInterface> upgradeClass;
    private final String telemetryName;

    private UpgradeType(int price, Class<? extends PlayerInterface> upgradeClass, String telemetryName) {
        this.price = price;
        this.upgradeClass = upgradeClass;
        this.telemetryName = telemetryName;
    }

    public int getPrice(){
        return price;
    }

    public PlayerInterface applyUpgrade(PlayerInterface player){
        try {
            return upgradeClass.getConstructor(PlayerInterface.class).newInstance(player);
        } catch (ReflectiveOperationException e) {
            throw new WrapperException();
        }
    }

    public String getTelemetryName(){
        return telemetryName;
    }
}
