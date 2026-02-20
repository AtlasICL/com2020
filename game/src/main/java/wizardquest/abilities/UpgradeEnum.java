package wizardquest.abilities;

import wizardquest.entity.PlayerInterface;

public enum UpgradeEnum { // "Upgrade" is referred to as "passive ability externally"
    // organised as price, class, telemetry name
    // add abilities
    PHYSICAL_DAMAGE_RESISTANCE(
            20,
            PhysicalDamageResistanceUpgrade.class,
            "PhysicalDamageResistance", "Physical Damage Resistance"),
    FIRE_DAMAGE_RESISTANCE(
            10,
            FireDamageResistanceUpgrade.class,
            "FireDamageResistance", "Fire Damage Resistance"),
    WATER_DAMAGE_RESISTANCE(
            5,
            WaterDamageResistanceUpgrade.class,
            "WaterDamageResistance", "Water Damage Resistance"),
    THUNDER_DAMAGE_RESISTANCE(
            5,
            ThunderDamageResistanceUpgrade.class,
            "ThunderDamageResistance", "Thunder Damage Resistance"),
    // IMPROVED_PHYSICAL_DAMAGE(20, ImprovedPhysicalDamageUpgrade.class,
    // "ImprovedPhysicalDamage"),
    // IMPROVED_FIRE_DAMAGE(15, ImprovedFireDamageUpgrade.class,
    // "ImprovedFireDamage"),
    // IMPROVED_WATER_DAMAGE(10, ImprovedWaterDamageUpgrade.class,
    // "ImprovedWaterDamage"),
    // IMPROVED_THUNDER_DAMAGE(10, ImprovedThunderDamageUpgrade.class,
    // "ImprovedThunderDamage"),
    SLASH_UNLOCK(
            15,
            SlashUnlockUpgrade.class,
            "SlashUnlock", "Slash"),
    ABSOLUTE_PULSE_UNLOCK(
            10,
            AbsolutePulseUnlockUpgrade.class,
            "AbsolutePulseUnlock", "Absolute Pulse"),
    WATER_JET_UNLOCK(
            20,
            WaterJetUnlockUpgrade.class,
            "WaterJetUnlock", "Water Jet"),
    FIRE_BALL_UNLOCK(
            30,
            FireBallUnlockUpgrade.class,
            "FireBallUnlock", "Fire Ball"),
    THUNDER_STORM_UNLOCK(
            25,
            ThunderStormUnlockUpgrade.class,
            "ThunderStormUnlock", "Thunder Storm");

    private final int price;
    private final Class<? extends UpgradeBase> upgradeClass;
    private final String telemetryName;
    private final String displayName;

    private UpgradeEnum(
            int price, Class<? extends UpgradeBase> upgradeClass, String telemetryName, String displayName) {
        this.price = price;
        this.upgradeClass = upgradeClass;
        this.telemetryName = telemetryName;
        this.displayName = displayName;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Applies an upgrade to a given player.
     * 
     * @param player the player to decorate with the upgrade.
     * @return a reference to the decorated player.
     * @throws IllegalStateException if the decoration fails due to reflection
     *                               failing.
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
     * 
     * @return the name of the upgrade.
     */
    public String getTelemetryName() {
        return telemetryName;
    }

    /**
     * Get the human readable name for the upgrade.
     * 
     * @return the display name.
     */
    public String getDisplayName() {
        return this.displayName;
    }
}
