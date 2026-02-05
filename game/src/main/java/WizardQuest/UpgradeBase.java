public abstract class UpgradeBase implements PlayerInterface {
    protected final PlayerInterface player;
        protected UpgradeBase(PlayerInterface player) {
        if (player != null) {
            this.player = player;
        } else 
        throw new IllegalArgumentException();
    }
    //getters?
}