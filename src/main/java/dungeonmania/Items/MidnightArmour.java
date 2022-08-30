package dungeonmania.Items;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.Settings;
import dungeonmania.util.Position;

public class MidnightArmour extends Item implements Weapon{
    
    private int durability = Integer.MAX_VALUE;

    public MidnightArmour(String id, Position position) {
        super(id, "midnight_armour", position);
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }

    @Override
    public int getDamage() {
        return Config.getSetting(Settings.midnight_armour_attack);
    }

    @Override
    public int getDefence() {
        return Config.getSetting(Settings.midnight_armour_defence);
    }

    @Override
    public void decreaseDurability() {
        this.durability = Integer.MAX_VALUE;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }
}
