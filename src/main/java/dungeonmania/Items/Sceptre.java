package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class Sceptre extends Item{
    
    public Sceptre(String id, Position position) {
        super(id, "sceptre", position);
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        inventoryController.removeItemFromInventory(this);
        return true;
    }

}
