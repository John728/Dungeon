package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.MovingEntities.Enemy;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovementHandler {
    private static List<Class> basicAllowed = Arrays.asList(FloorSwitch.class, Item.class, Exit.class, Mercenary.class);

    public static void moveMovingEntities(Dungeon dungeon) {
        DungeonMap dungeonMap = dungeon.getMap();
        List<Entity> all = dungeonMap.getEntities();
        for (Entity entity : all) {
            if (entity instanceof MovingEntity) {
                MovingEntity movingentity = (MovingEntity) entity;
                Position oldPos = new Position(movingentity.getPosition());
                Position newPos = new Position(movingentity.getPosition().translateBy(movingentity.move(dungeon)));
                List<Entity> oldPosEnts = dungeonMap.getEntitiesAt(oldPos);
                List<Entity> newPosEnts = dungeonMap.getEntitiesAt(newPos);

                // Sometimes we won't be able to move off of the current tile if it is a swamp
                Boolean canMove = true;
                if (checkSwamp(oldPosEnts)) {
                    // Check/update status of swamp tile if we are on one
                    SwampTile swamp = getSwamp(oldPosEnts);
                    swamp.tickSwamp(entity);
                    if (!swamp.canStepOffSwamp(entity)) {
                        canMove = false;
                    } 
                } 
                if (canMove) {
                    if (checkPortal(newPosEnts) && entity instanceof Mercenary) {
                        sendThroughPortal(dungeonMap, oldPos, newPos, movingentity.move(dungeon), entity);
                    } else if (checkSwamp(newPosEnts)) {
                        // We are moving onto a swamp tile
                        SwampTile swamp = getSwamp(newPosEnts);
                        swamp.stepOnSwamp(entity);
                        dungeonMap.moveEntity(movingentity.getId(), oldPos, newPos);
                    } else if (entity instanceof Spider) {
                        dungeonMap.moveEntity(movingentity.getId(), oldPos, newPos);
                    } else if (!checkPortal(newPosEnts)) {
                        dungeonMap.moveEntity(movingentity.getId(), oldPos, newPos);
                    }
                }
            }
        } 
    }

    /**
     * 
     * @param posEntities list of all entites on the tile
     * @param allowed list classes that are allowed to be moved over by the caller
     * @return true if all entities are allowed to be moved over and can be be moved over
     */
    public static boolean checkEntsOnlyContains (List<Entity> posEntities, List<Class> allowed) {
        if (posEntities == null || posEntities.size() == 0) {
            return true;
        }
        // Checks all entites match the allowed entity types
        return posEntities.stream().allMatch(e -> allowed.stream().anyMatch(a -> a.isInstance(e))) &&
            canMoveOverAllEnts(posEntities);
    }

    // /**
    //  * 
    //  * @param posEntities list of all entites on the tile
    //  * @param allowed list classes that are allowed to be moved over by the caller
    //  * @return true if all entities are allowed to be moved over and can be be moved over
    //  */
    // public static boolean checkEntitiesForPlayerMovmenent (List<Entity> posEntities, List<Class> allowed) {
    //     if (posEntities == null || posEntities.size() == 0) {
    //         return true;
    //     }
        
    //     // Checks all entites match the allowed entity types
    //     for (Entity entity: posEntities) {
    //         if (!(entity instanceof Enemy)) {
    //             if (!(entity.getCanMoveOver())) {
    //                 return false;
    //             }
    //         }
            
    //     }
    //     return true;
    // }

    /**
     * @param entities list of all entities on the tile
     * @return true if all entites say they can be moved over
     */
    public static boolean canMoveOverAllEnts(List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return true;
        }

        // Checks all entites in the list to see if they can be moved over
        return entities.stream().allMatch(e -> e.getCanMoveOver());
    }

    public static boolean isBoulderMoveable(DungeonMap dm, List<Entity> list, Direction direction) {
        // If the boulder can be moved in the direction return true
        Entity boulder = getBoulderAtPos(list);
        return checkEntsOnlyContains(dm.getEntitiesAt(boulder.getPosition().translateBy(direction)), basicAllowed);
    }

    /**
     * Returns a list of all adjacent positions that can be moved to.
     * @param dungeon
     * @param curPos
     * @param allowed types of objects that are allowed to be moved over
     * @return
     */
    public static List<Position> getAdjacentPos(DungeonMap dm, Position curPos, List<Class> allowed) {
        List<Position> neighbours = new ArrayList<>();

        // Adjacent directions list
        List<Direction> adjDirs = new ArrayList<>(Arrays.asList(Direction.DOWN,
                Direction.UP, Direction.RIGHT, Direction.LEFT));

        // Find adjacent cells we can move to
        for (Direction dir : adjDirs) {
            Position newPos = curPos.translateBy(dir);
            List<Entity> newPosEntities = dm.getEntitiesAt(newPos);
            if(MovementHandler.checkEntsOnlyContains(newPosEntities, allowed)) {
                neighbours.add(curPos.translateBy(dir));
            }
        }

        return neighbours;
    }

    /**
     * @pre the start and end position must be cardianlly adjacent
     * @param start the starting position
     * @param end the neighbouring end position
     * @return the direction to get from start to end
     */
    public static Direction getTravelDir(Position start, Position end) {
        Direction dir = Direction.STAY;
        for (Direction d : Direction.values()) {
            if (d != Direction.STAY && start.translateBy(d).equals(end)) {
                dir = d;
            }
        }
        return dir;
    }

    /**
     * @param start
     * @param end
     * @return the manhattan distance between the two points
     */
    public static int getManhattanDistance(Position start, Position end) {
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        return dx + dy;
    }

    public static double getEuclidianDistance(Position start, Position end) {
        double dx = Math.pow(start.getX() - end.getX(), 2);
        double dy = Math.pow(start.getY() - end.getY(), 2);
        return Math.sqrt(dx + dy);
    }

    public static boolean checkPortal(List<Entity> list) {
        if (list == null) return false;
        return list.stream().anyMatch(e -> e instanceof Portal);
    }

    public static boolean checkSwamp(List<Entity> list) {
        if (list == null) return false;
        return list.stream().anyMatch(SwampTile.class::isInstance);
    }

    public static SwampTile getSwamp(List<Entity> list) {
        return (SwampTile) list.stream().filter(SwampTile.class::isInstance).findFirst().orElse(null);
    }

    public static boolean checkBoulder(List<Entity> list) {
        if (list == null) return false;
        return list.stream().anyMatch(e -> e instanceof Boulder);
    }

    public static boolean checkDoor(List<Entity> list) {
        if (list == null) return false;
        return list.stream().anyMatch(e -> e instanceof Door);
    }

    public static boolean checkEnemy(List<Entity> list) {
        if (list == null) return false;
        return list.stream().anyMatch(e -> e instanceof Enemy);
    }

    public static Entity getBoulderAtPos(List<Entity> list) {
        return list.stream().filter(e -> e instanceof Boulder).findAny().orElse(null);
    }

    public static Entity getPortalAtPos(List<Entity> list) {
        return list.stream().filter(e -> e instanceof Portal).findAny().orElse(null);
    }

    public static Entity getDoorAtPos(List<Entity> list) {
        return list.stream().filter(e -> e instanceof Door).findAny().orElse(null);
    }

    public static void sendThroughPortal(DungeonMap dm, Position oldPos, Position newPos, Direction direction, Entity entity) {
        List<Entity> posList = dm.getEntitiesAt(newPos);
            
        while (MovementHandler.checkPortal(posList)) {
            // get the portal at position
            Entity portalEntity = MovementHandler.getPortalAtPos(posList);

            // get the linked portal
            Portal startPortal = (Portal) portalEntity;
            Portal endPortal = startPortal.getLinkedPortal(dm);

            // get the position of the linked portal
            Position endPortalPosition = endPortal.getPosition().translateBy(direction);
            posList = dm.getEntitiesAt(endPortalPosition);
            if (MovementHandler.checkEntsOnlyContains(posList, basicAllowed)) {
                // move player to pos
                dm.moveEntity(entity.getId(), oldPos, endPortalPosition);
                return;
            } else if (!MovementHandler.checkPortal(posList)) {
                // find next available pos
                List<Position> neighbours = getAdjacentPos(dm, endPortal.getPosition(), basicAllowed);
                if (neighbours == null || neighbours.size() == 0) {
                    return;
                } else {
                    posList = dm.getEntitiesAt(neighbours.get(0));
                    if (!MovementHandler.checkPortal(posList)) {
                        dm.moveEntity(entity.getId(), oldPos, neighbours.get(0));
                    }
                }
            }
        }
    }


}
