public abstract class ConcreteAbility implements AbilityInterface{
    protected static String description;
    protected static int getNumberOfTargets;//to be removed
    protected static AbilityType type;

    protected ConcreteAbility() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getNumberOfTargets() {
        return getNumberOfTargets;
    }

    @Override
    public AbilityType getType() {
        return type;
    }

    protected final void useAbility(EntityInterface source, EntityInterface target, int baseDamage, int magicCost, DamageType damageType) throws LackingResourceException{
        if (source instanceof PlayerInterface player) {
            if (player.getMagic() >= baseMagicPoints) {
                player.loseMagic(baseMagicPoints);
            } else {
                throw new LackingResourceException("Not enough magic points");
            }
        } //no magic consumed if enemy uses attack
        target.loseHealth(baseDamage, damageType);
    }
}