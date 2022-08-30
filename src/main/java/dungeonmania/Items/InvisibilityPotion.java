package dungeonmania.Items;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Player;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovementStrategies.DefaultMercenaryMovement;
import dungeonmania.MovingEntities.MovementStrategies.ZombieMovement;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    public InvisibilityPotion(String id, Position position) {
        super(id, "invisibility_potion", position, Config.getSetting(Settings.invisibility_potion_duration));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        dungeonMap.getPlayer().addActivePotion(this);
        return true;
    }

    @Override
    public void endEffect(DungeonMap dungeonMap, Player player) {
        List<Mercenary> mercs = dungeonMap.getAllOfType(Mercenary.class);
        mercs.stream().forEach(m -> m.setMoveStrategy(new DefaultMercenaryMovement()));
    }

    @Override
    public void applyEffect(DungeonMap dungeonMap, Player player) {
        List<Mercenary> mercs = dungeonMap.getAllOfType(Mercenary.class);
        mercs.stream().forEach(m -> m.setMoveStrategy(new ZombieMovement()));
    }
}
