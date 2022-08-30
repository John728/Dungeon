package dungeonmania.Items;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.Settings;
import dungeonmania.util.Position;

public class Sword extends Item implements Weapon {
    
    private int durability = Config.getSetting(Settings.sword_durability);

    public Sword(String id, Position position) {
        super(id, "sword", position);
        // this.setDurability(Config.getSetting(Settings.sword_durability));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }

    @Override
    public int getDamage() {
        return Config.getSetting(Settings.sword_attack);
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
