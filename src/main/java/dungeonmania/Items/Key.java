package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class Key extends Item{
    
    private int key;

    public Key(String id, Position position, int key) {
        super(id, "key", position);
        this.key = key;
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }


    public int getKey() {
        return this.key;
    }


}
