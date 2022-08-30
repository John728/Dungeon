package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class Treasure extends Item {
    
    public Treasure(String id, Position position) {
        super(id, "treasure", position);
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }
}
