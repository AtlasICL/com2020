package WizardQuest;

import java.util.List;

/**
 * Goblin: beginner level enemy
 * They have low health and use only physical attacks
 * Phase 1 enemy
 */
public class Goblin extends Enemy{


    public Goblin(){
        super((int) Math.round(50 * 1.0f));
    }
    @Override
    public List<AbilityType> getAbilities(){
        return List.of(AbilityType.SLASH, AbilityType.PUNCH);
    }
    @Override
    public EntityType getType(){
        return EntityType.GOBLIN;
    }
}
