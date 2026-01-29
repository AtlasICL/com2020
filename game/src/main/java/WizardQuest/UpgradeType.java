package WizardQuest;

public enum UpgradeType {
    UPGRADE_1(),
    UPGRADE_2(),
    UPGRADE_3();

    private final int price;
    private final Class<? extends PlayerInterface> upgradeClass;
    private final String telemetryName;

    private UpgradeType(int price, Class<? extends PlayerInterface> upgradeClass, String telemetryName) {
        this.price = price;
        this.abiilityClass = abiilityClass;
        this.telemetryName = telemetryName;
    }

    public int getPrice() {
        return price;
    }

    public PlayerInterface applyUpgrade(PlayerInterface player) {
        return null;
    }

    private String getTelemetryName() {
        return telemetryName;
    }
}