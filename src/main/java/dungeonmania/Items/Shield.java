package dungeonmania.Items;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Item;
import dungeonmania.Settings;
import dungeonmania.util.Position;

public class Shield extends Item implements Weapon {

    private int durability = Config.getSetting(Settings.shield_durability);

    public Shield(String id, Position position) {
        super(id, "shield", position);
        // this.setDurability(Config.getSetting(Settings.shield_durability));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        return false;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public int getDefence() {
        return Config.getSetting(Settings.shield_defence);
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
