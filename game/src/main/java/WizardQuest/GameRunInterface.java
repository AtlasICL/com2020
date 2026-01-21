package com.thespecialcharacters.WizardQuest;

public interface GameRunInterface {
    /**
     * Picks a random encounter for the current stage that is not yet complete.
     * 
     * @return the chosen encounter. 
     */
    public EncounterInterface pickEncounter();

    /**
     * Picks 3 upgrades that have not yet been purchased and returns them.
     * 
     * @return the 3 upgrades in the shop. 
     */
    public UpgradeType[] viewShop();

    /**
     * Informs the run that the given upgrade has been purchased and should be applied to the player.
     * 
     * @param upgrade the upgrade being bough that is to be applied to the player
     */
    public void purchaseUpgrade(UpgradeType upgrade);

    /**
     * Returns a reference to the run's player.
     */
    public PlayerInterface getPLayer();

    public void nextStage();

    public int getStage()
}
