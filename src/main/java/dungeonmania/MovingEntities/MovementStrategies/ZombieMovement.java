package dungeonmania.MovingEntities.MovementStrategies;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.MovementHandler;
import dungeonmania.MovingEntities.Move;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieMovement implements Move, Serializable {
    /**
     *  Zombie movement pattern. Move in random directions 
     *  Same movement contraints as player
     *  Portals have no effect on zombies
     *  Can't push boulders
     */

    private List<Class> canMoveOver = Arrays.asList(FloorSwitch.class,
        Item.class, Exit.class, Portal.class, SwampTile.class);


    @Override
    public Direction move(Dungeon dungeon, Position curPos) {
        DungeonMap dungeonMap = dungeon.getMap();
        // Attemp to move in random direction
        Direction newDir = Direction.randomDirection();
        // New position to check
        Position newPos = curPos.translateBy(newDir);
        // Zombie won't move if random option is invalid
        Direction movePos = Direction.STAY;
        List<Entity> entities = dungeonMap.getEntitiesAt(newPos);

        if (MovementHandler.checkEntsOnlyContains(entities, canMoveOver)) {
            movePos = newDir;
        } else if (MovementHandler.checkDoor(entities)) {
            // can go through open doors
            Door door = (Door) MovementHandler.getDoorAtPos(entities);
            if (door.isOpen()) {
                movePos = newDir;
            }
        }

        return movePos;
    }
}
