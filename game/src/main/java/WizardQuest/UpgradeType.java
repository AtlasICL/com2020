public enum UpgradeType{ //"Upgrade" is refered to as "passive ability externally"
    //organised as price, class, telemetry name
    //add abilities
    PHYSICAL_DAMAGE_RESISTANCE(20, PhysicalDamageResistance.class, "PhysicalDamageResistance"),
    FIRE_DAMAGE_RESISTANCE(10, FireDamageResistance.class, "FireDamageResistance"),
    WATER_DAMAGE_RESISTANCE(5, WaterDamageResistance.class, "WaterDamageResistance"),
    THUNDER_DAMAGE_RESISTANCE(5, ThunderDamageResistance.class, "ThunderDamageResistance"),
    IMPROVED_PHYSICAL_DAMAGE(20, ImprovedPhysicalDamage.class, "ImprovedPhysicalDamage"),
    IMPROVED_FIRE_DAMAGE(15, ImprovedFireDamage.class, "ImprovedFireDamage"),
    IMPROVED_WATER_DAMAGE(10, ImprovedWaterDamage.class, "ImprovedWaterDamage"),
    IMPROVED_THUNDER_DAMAGE(10, ImprovedThunderDamage.class, "ImprovedThunderDamage");

    private final int price;
    private final Class<? extends PlayerInterface> upgradeClass;
    private final String telemetryName;

    private UpgradeType(
            int price, Class<? extends PlayerInterface> upgradeClass, String telemetryName
    ){
        this.price = price;
        this.upgradeClass = upgradeClass;
        this.telemetryName = telemetryName;
    }

    public int getPrice(){
        return price;
    }

    public PlayerInterface applyUpgrade(playerInterface player){
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
