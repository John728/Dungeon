package dungeonmania.MovingEntities.MovementStrategies;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Dungeon;
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

public class ScaredEnemyMovement implements Move, Serializable {
    /**
     * The zombie and mercenary can be scared.
     * Moves entity away from the player.
     * Movement pattern for when invibility potion is on.
     */
    @Override
    public Direction move(Dungeon dungeon, Position curPos) {
        List<Class> allowed = Arrays.asList(FloorSwitch.class,
            Item.class, Exit.class, Portal.class, Player.class, SwampTile.class);

        List<Position> neighbours = MovementHandler.getAdjacentPos(dungeon.getMap(), curPos, allowed);
        Position playerPos = dungeon.getPlayer().getPosition();
        int longestDistance = 0; 
        Position furthestPos = null;

        // Check all adjacent tiles
        for (Position neighbour : neighbours) {
            int distance = MovementHandler.getManhattanDistance(neighbour, playerPos);
            if (distance >= longestDistance) {
                longestDistance = distance;
                furthestPos = neighbour;
            }
        }

        // Check current tile to see if furthest
        int distance = MovementHandler.getManhattanDistance(curPos, playerPos);
        if (distance >= longestDistance) {
            longestDistance = distance;
            furthestPos = curPos;
        }

        if (furthestPos == null) {
            return Direction.STAY;
        } else {
            return MovementHandler.getTravelDir(curPos, furthestPos);
        }
    }
    
}
