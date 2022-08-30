package dungeonmania.goals.goalEvaluation;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.Settings;

public class Enemies implements EvaluationInterface {
    
    private final String name = "enemies";

    // This function will return true when the enemies goal is met.
    public boolean evaluate(Dungeon dungeon) {
        return (dungeon.getEnemiesKilled() >= Config.getSetting(Settings.enemy_goal));
    }

    public String getName() {
        return name;
    }
}
