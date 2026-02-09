package WizardQuest;

public enum UpgradeType { // "Upgrade" is refered to as "passive ability externally"
    // organised as price, class, telemetry name
    // add abilities
    PHYSICAL_DAMAGE_RESISTANCE(
            20,
            PhysicalDamageResistanceUpgrade.class,
            "PhysicalDamageResistance"),
    FIRE_DAMAGE_RESISTANCE(
            10,
            FireDamageResistanceUpgrade.class,
            "FireDamageResistance"),
    WATER_DAMAGE_RESISTANCE(
            5,
            WaterDamageResistanceUpgrade.class,
            "WaterDamageResistance"),
    THUNDER_DAMAGE_RESISTANCE(
            5,
            ThunderDamageResistanceUpgrade.class,
            "ThunderDamageResistance"),
    // MPROVED_PHYSICAL_DAMAGE(20, ImprovedPhysicalDamageUpgrade.class,
    // "ImprovedPhysicalDamage"),
    // IMPROVED_FIRE_DAMAGE(15, ImprovedFireDamageUpgrade.class,
    // "ImprovedFireDamage"),
    // IMPROVED_WATER_DAMAGE(10, ImprovedWaterDamageUpgrade.class,
    // "ImprovedWaterDamage"),
    // IMPROVED_THUNDER_DAMAGE(10, ImprovedThunderDamageUpgrade.class,
    // "ImprovedThunderDamage"),
    PUNCH_UNLOCK(
            0, //not applicable
            PunchUnlockUpgrade.class,
            "PunchUnlock"),
    SLASH_UNLOCK(
            15,
            SlashUnlockUpgrade.class,
            "SlashUnlock"),
    ABSOLUTE_PULSE_UNLOCK(
            10,
            AbsolutePulseUnlockUpgrade.class,
            "AbsolutePulseUnlock"),
    WATER_JET_UNLOCK(
            20,
            WaterJetUnlockUpgrade.class,
            "WaterJetUnlock"),
    FIRE_BALL_UNLOCK(
            30,
            FireBallUnlockUpgrade.class,
            "FireBallUnlock"),
    THUNDER_STORM_UNLOCK(
            25,
            ThunderStormUnlockUpgrade.class,
            "ThunderStormUnlock");

    private final int price;
    private final Class<? extends UpgradeBase> upgradeClass;
    private final String telemetryName;

    private UpgradeType(
            int price, Class<? extends UpgradeBase> upgradeClass, String telemetryName) {
        this.price = price;
        this.upgradeClass = upgradeClass;
        this.telemetryName = telemetryName;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Applies an upgrade to a given player.
     * @param player the player to decorate with the upgrade.
     * @return a reference to the decorated player.
     * @throws IllegalStateException if the decoration fails due to reflection failing.
     */
    public PlayerInterface applyUpgrade(PlayerInterface player) throws IllegalStateException, IllegalArgumentException {
        try {
            return upgradeClass.getConstructor(PlayerInterface.class).newInstance(player);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns the name of the upgrade following the telemetry specification.
     * @return the name of the upgrade.
     */
    public String getTelemetryName() {
        return telemetryName;
    }
}
