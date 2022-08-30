package dungeonmania.MovingEntities.MovementStrategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.MovementHandler;
import dungeonmania.Player;
import dungeonmania.MovingEntities.Move;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DefaultMercenaryMovement implements Move, Serializable {

    private List<Class> canMoveOver = Arrays.asList(FloorSwitch.class,
        Item.class, Exit.class, Portal.class, Player.class, SwampTile.class);

    /** 
     * Default mercenary movement. Moves towards palyer 
     * only stopping if it can't move any closer
     */
    @Override
    public Direction move(Dungeon dungeon, Position curPos) {

        // Map to track all visited entities
        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();

        // Nodes already fully expanded
        List<Position> closed = new ArrayList<>();
        // Nodes to be expanded
        List<Position> open = new ArrayList<>();

        // sets starting position to have itself in the list
        dist.put(curPos, 0);
        prev.put(curPos, null);
        open.add(curPos);

        Position playerPos = dungeon.getPlayer().getPosition();
        DungeonMap dungeonMap = dungeon.getMap();

        // Calc path for all nodes
        while (open.size() > 0) {

            // Find the position in open list with lowest distance
            Position openPos = open.get(0);
            for (Position pos : open) {
                if (dist.get(pos) < dist.get(openPos)) {
                    openPos = pos;
                }
            }
            open.remove(openPos);

            Integer curDist = dist.get(openPos);

            // We reached the player
            if (openPos.equals(playerPos)) {
                break;
            }

            List<Position> neighbours = MovementHandler.getAdjacentPos(dungeonMap, openPos, canMoveOver);

            // Add neighbours to open and update prevs if shorter
            for (Position neighbour : neighbours) {
                Integer neighbourDist = dist.get(neighbour);
                int neighbourWeight = dungeonMap.getTileWeight(neighbour);
                if ((neighbourDist == null) || (neighbourDist > curDist + neighbourWeight)) {
                    // Neighbour not yet reached or current path is shorter
                    dist.put(neighbour, curDist + neighbourWeight);
                    prev.put(neighbour, openPos);
                }
                if (!closed.contains(neighbour) && !open.contains(neighbour)) {
                    open.add(neighbour);
                }
            }
            closed.add(openPos);
        }

        // Find the direction to move to get to the player
        Position prevPos;
        if (prev.containsKey(playerPos)) {
            prevPos = playerPos;
        } else {
            // Find the position in dist with the lowest distance
            Position closest = curPos;
            for (Map.Entry<Position, Integer> entry : dist.entrySet()) {
                if (MovementHandler.getEuclidianDistance(entry.getKey(), playerPos) < MovementHandler.getEuclidianDistance(closest, playerPos)) {
                    closest = entry.getKey();
                }
            }
            prevPos = closest;
        }

        // Traverse like a linked list to find the direction we should move in 
        Position nextPos = prev.get(prevPos);
        if (nextPos == null) {
            // We are as close as we can get
            return Direction.STAY;
        } else {
            // Linked list search for first step in the path to the player
            while (!nextPos.equals(curPos)) {
                prevPos = nextPos;
                nextPos = prev.get(nextPos);
            }
            return MovementHandler.getTravelDir(curPos, prevPos);
        }
    }
}
