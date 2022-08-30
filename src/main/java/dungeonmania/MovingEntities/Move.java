package dungeonmania.MovingEntities;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Move {
    // Move function for all moving entities, sets default move strategy too
    public abstract Direction move(Dungeon dungeon, Position curPos);
}
