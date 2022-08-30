package dungeonmania;

import dungeonmania.MovingEntities.Move;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    
    public MovingEntity(String id, String type, Position position) {
        super(id, type, position);
    }

    public abstract Direction move(Dungeon dungeon);
    public abstract void setMoveStrategy(Move movement_strat);
}
