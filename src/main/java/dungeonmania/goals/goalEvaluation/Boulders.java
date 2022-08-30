package dungeonmania.goals.goalEvaluation;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;

public class Boulders implements EvaluationInterface {
    
    private final String name = "boulders";

    // This function will return true when the boulders goal is met.
    public boolean evaluate(Dungeon dungeon) {
        // get each boulder in the dungeon
        DungeonMap dungeonMap = dungeon.getMap();
        List<Entity> floorSwitches = dungeonMap.getEntities().stream().filter(entity -> entity.getType().equals("switch")).collect(Collectors.toList());
        
        // check that each boulder is on a switch, otherwise, return false
        for (Entity entity : floorSwitches) {
            if (!dungeonMap.getEntitiesAt(entity.getPosition()).stream().anyMatch(e -> e.getType().equals("boulder"))) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }
}
