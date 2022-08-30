package dungeonmania.StaticEntities;

import dungeonmania.StaticEntity;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    
    private int key;
    private boolean open = false;

    public Door(String id, Position position, int key) {
        super(id, "door", position);
        this.key = key;
    } 

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    public int getKey() {
        return this.key;
    }

}