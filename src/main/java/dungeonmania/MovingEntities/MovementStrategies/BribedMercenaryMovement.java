package dungeonmania.MovingEntities.MovementStrategies;

import java.io.Serializable;

import dungeonmania.Dungeon;
import dungeonmania.MovementHandler;
import dungeonmania.MovingEntities.Move;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BribedMercenaryMovement implements Move, Serializable {
    /**
     * Movement strategy for bribed mercenary, moves towards player 
     * and then follows player when in same square. 
     * May be same as default movement.
     */

    @Override
    public Direction move(Dungeon dungeon, Position curPos) {
        Position playerPos = dungeon.getPlayerPos();
        Position prevPlayerPos = dungeon.getPrevPlayerPos();
        Direction moveDir = Direction.STAY;
        if (MovementHandler.getEuclidianDistance(curPos, playerPos) < 2) {
            // if we are adjacent go to the previous player pos 
            // unless it is the same as the current player position
            if (!playerPos.equals(prevPlayerPos)) {
                moveDir = MovementHandler.getTravelDir(curPos, prevPlayerPos);
            }
        } else {
            // The player is too far away to follow using the previous position
            Move defaultMercenary = new DefaultMercenaryMovement();
            moveDir = defaultMercenary.move(dungeon, curPos);
        }
        return moveDir;
    }
}
