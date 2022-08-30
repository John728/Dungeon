package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class SunStone extends Item{
    
    public SunStone(String id, Position position) {
        super(id, "sun_stone", position);
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }
}
