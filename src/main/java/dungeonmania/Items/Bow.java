package dungeonmania.Items;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.Settings;
import dungeonmania.util.Position;

public class Bow extends Item implements Weapon {
    
    private int durability = Config.getSetting(Settings.bow_durability);

    public Bow(String id, Position position) {
        super(id, "bow", position);
        // this.setDurability(Config.getSetting(Settings.bow_durability));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }

    @Override
    public int getDamage() {
        return 2; // not a config setting
    }

    @Override
    public int getDefence() {
        return 0;
    }

    @Override
    public void decreaseDurability() {
        this.durability--;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }
}
