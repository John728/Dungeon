package dungeonmania.goals.goalEvaluation;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

public class Exit implements EvaluationInterface {
    
    private final String name = "exit";

    // This function will return true when the exit goal is met.
    public boolean evaluate(Dungeon dungeon) {
        DungeonMap dungeonMap = dungeon.getMap();
        return dungeonMap.getAllOfType("exit").stream().anyMatch(exit -> exit.getPosition().equals(dungeon.getPlayer().getPosition()));
    }

    public String getName() {
        return name;
    }
}
