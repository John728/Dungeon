package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.DungeonManiaController;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Settings;
import dungeonmania.StaticEntity;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    
    int spawnClock = 0;
    int spawnRate = Config.getSetting(Settings.zombie_spawn_rate);

    public ZombieToastSpawner(String id, Position position) {
        super(id, "zombie_toast_spawner", position);
        this.setIsInteractable(true);
    }
    
    public void spawnZombiesTick(DungeonMap dungeonMap) {
        if (spawnRate == 0) {
            return;
        }
        
        spawnClock++;

        if (spawnClock == spawnRate) {
            spawnClock = 0;
           
            Position spawnPosition = getSpawnLocation(dungeonMap);
            if (spawnPosition != null) {
                ZombieToast zombie = new ZombieToast(DungeonManiaController.getNewId(dungeonMap, ZombieToast.class), spawnPosition);
                dungeonMap.addEntity(zombie);
            }
           
        }
    }

    public Position getSpawnLocation (DungeonMap dungeonMap) {
        Position spawnPos = null;

        List<Entity> entities = dungeonMap.getEntitiesAt(getPosition().translateBy(Direction.UP));

        if (entities != null){
            for (Entity entity: entities) {
                if (!(entity instanceof Wall)) {
                    spawnPos = getPosition().translateBy(Direction.UP);
                    return spawnPos;
                }
            }
        }
        else {
            return getPosition().translateBy(Direction.UP);
        }

        entities = dungeonMap.getEntitiesAt(getPosition().translateBy(Direction.RIGHT));

        if (entities != null){
            for (Entity entity: entities) {
                if (!(entity instanceof Wall)) {
                    spawnPos = getPosition().translateBy(Direction.RIGHT);
                    return spawnPos;
                }
            }
        }
        else {
            return getPosition().translateBy(Direction.RIGHT);
        }

        entities = dungeonMap.getEntitiesAt(getPosition().translateBy(Direction.DOWN));

        if (entities != null){
            for (Entity entity: entities) {
                if (!(entity instanceof Wall)) {
                    spawnPos = getPosition().translateBy(Direction.DOWN);
                    return spawnPos;
                }
            }
        }
        else {
            return getPosition().translateBy(Direction.DOWN);
        }

        entities = dungeonMap.getEntitiesAt(getPosition().translateBy(Direction.LEFT));

        if (entities != null){
            for (Entity entity: entities) {
                if (!(entity instanceof Wall)) {
                    spawnPos = getPosition().translateBy(Direction.LEFT);
                    return spawnPos;
                }
            }
        }
        else {
            return getPosition().translateBy(Direction.LEFT);
        }
        
        return null;
    }
}
