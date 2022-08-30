package dungeonmania.Items;

import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.util.Position;

public class Arrows extends Item{

    public Arrows(String id, Position position) {
        super(id, "arrow", position);
        //this.setPosition(position.asLayer(1));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }
}
