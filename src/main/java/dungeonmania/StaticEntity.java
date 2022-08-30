package dungeonmania;

import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {

    public StaticEntity(String id, String type, Position position) {
        super(id, type, position);
    }

}
