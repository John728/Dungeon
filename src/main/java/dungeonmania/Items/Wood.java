package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class Wood extends Item{
    
    public Wood(String id, Position position) {
        super(id, "wood", position);
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }

}
