package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.StaticEntity;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity {
    
    public Boulder(String id, Position position) {
        super(id, "boulder", position);
    }

    // need to make mathod that ckecks if floorswitch on same
    // tile and then activate it
    public void checkAndActivateFloorSwitch(DungeonMap dungeonMap, Position newPos) {
        List<Entity> entities = dungeonMap.getEntitiesAt(newPos);
        FloorSwitch floorSwitch = null;
        for (Entity entity: entities) {
            if (entity instanceof FloorSwitch) {
                floorSwitch = (FloorSwitch) entity;
            }
        }
        if(floorSwitch != null) {
            floorSwitch.activate(dungeonMap);
        }
    }

    public void checkAndDeactivateFloorSwitch(DungeonMap dungeonMap, Position oldPos) {
        List<Entity> entities = dungeonMap.getEntitiesAt(oldPos);
         
        for (Entity entity: entities) {
            if (entity instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) entity;
                floorSwitch.deactivate();
            }
        }
    }
    
}
