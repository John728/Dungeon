package dungeonmania.MovingEntities.MovementStrategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.MovementHandler;
import dungeonmania.MovingEntities.Move;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderMovement implements Move, Serializable {
    
    private List<Position> path;
    private boolean clockwiseRotation = true;
    
    @Override
    public Direction move(Dungeon dungeon, Position curPos) {

        DungeonMap dungeonMap = dungeon.getMap();
        
        if (path == null) {
            path = generatePath(curPos);
        }

        if (!path.contains(curPos)) {
            // We aren't on the 'path' yet, try to move to first item
            List<Entity> entitiesInfront = dungeonMap.getEntitiesAt(curPos.translateBy(Direction.UP));
            if (MovementHandler.checkBoulder(entitiesInfront)) {
                // If infront is a boulder remain still
                return Direction.STAY;
            } else {
                return Direction.UP;
            }
        } else {
            Position newPos = getNextPos(curPos);
            Direction travelDir = MovementHandler.getTravelDir(curPos, newPos);
            List<Entity> entitiesInfront = dungeonMap.getEntitiesAt(curPos.translateBy(travelDir));
            if (MovementHandler.checkBoulder(entitiesInfront)) {
                // If infront is a boulder change rotation 
                clockwiseRotation = !clockwiseRotation;
                // Try to move other way
                newPos = getNextPos(curPos);
                travelDir = MovementHandler.getTravelDir(curPos, newPos);
                entitiesInfront = dungeonMap.getEntitiesAt(curPos.translateBy(travelDir));
                if (MovementHandler.checkBoulder(entitiesInfront)) {
                    return Direction.STAY;
                } else {
                    return travelDir;
                }
            } else {
                // Otherwise aim to continue around the path
                return travelDir; 
            }
        }
    }

    private List<Position> generatePath(Position curPos) {
        // reset the path
        // clockwise path is UP RIGHT DOWN DOWN LEFT LEFT UP UP RIGHT
        List<Position> path = new ArrayList<>();
        Position pos = curPos.translateBy(Direction.UP);
        path.add(pos);
        pos = pos.translateBy(Direction.RIGHT);
        path.add(pos);
        pos = pos.translateBy(Direction.DOWN);
        path.add(pos);
        pos = pos.translateBy(Direction.DOWN);
        path.add(pos);
        pos = pos.translateBy(Direction.LEFT);
        path.add(pos);
        pos = pos.translateBy(Direction.LEFT);
        path.add(pos);
        pos = pos.translateBy(Direction.UP);
        path.add(pos);
        pos = pos.translateBy(Direction.UP);
        path.add(pos);
        return path;
    }

    private Position getNextPos(Position curPos) {
        int posIndex = path.indexOf(curPos);
        // Calculate the next spot in the path to move to 
        if (clockwiseRotation && posIndex == 7) {
            posIndex = 0;
        } else if (!clockwiseRotation && posIndex == 0) {
            posIndex = 7;
        } else {
            if (clockwiseRotation) {
                posIndex += 1;
            } else {
                posIndex -= 1;
            }
        }

        return path.get(posIndex);
    }

}
