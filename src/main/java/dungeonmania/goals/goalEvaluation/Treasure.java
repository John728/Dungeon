package dungeonmania.goals.goalEvaluation;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.Settings;

public class Treasure implements EvaluationInterface {
    
    private final String name = "treasure";

    // This function will return true when the treasure goal is met.
    public boolean evaluate(Dungeon dungeon) {
        return dungeon.getPlayer()
                      .getInventoryController()
                      .getNumOfTreasure() >= Config.getSetting(Settings.treasure_goal);
    }

    public String getName() {
        return name;
    }
    
}
