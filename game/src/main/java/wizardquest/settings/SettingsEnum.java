package wizardquest.settings;

/**
 * Enumerates all settings types in the game.
 */
public enum SettingsEnum {
    TELEMETRY_ENABLED("TelemetryEnabled"),
    PLAYER_MAX_HEALTH("PlayerMaxHealth"),
    ENEMY_DAMAGE_MULTIPLIER("EnemyDamageMultiplier"),
    ENEMY_MAX_HEALTH_MULTIPLIER("EnemyMaxHealthMultiplier"),
    STARTING_LIVES("StartingLives"),
    MAX_MAGIC("MaxMagic"),
    MAGIC_REGEN_RATE("MagicRegenRate"),
    SHOP_ITEM_COUNT("ShopItemCount");

    private final String telemetryName;

    private SettingsEnum(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}