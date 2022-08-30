package dungeonmania.StaticEntities;

import dungeonmania.StaticEntity;
import dungeonmania.util.Position;

public class Wall extends StaticEntity {
    
    public Wall(String id, Position position) {
        super(id, "wall", position);
    } 
}
