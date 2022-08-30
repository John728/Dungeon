package dungeonmania.Items;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.InventoryController;
import dungeonmania.Player;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.Hydra;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.MovingEntities.MovementStrategies.DefaultMercenaryMovement;
import dungeonmania.MovingEntities.MovementStrategies.ScaredEnemyMovement;
import dungeonmania.MovingEntities.MovementStrategies.ZombieMovement;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    public InvincibilityPotion(String id, Position position) {
        super(id, "invincibility_potion", position, Config.getSetting(Settings.invincibility_potion_duration));
    }

    @Override
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        dungeonMap.getPlayer().addActivePotion(this);
        return true;
    }

    @Override
    public void endEffect(DungeonMap dungeonMap, Player player) {
        List<Mercenary> mercs = dungeonMap.getAllOfType(Mercenary.class);
        List<ZombieToast> zombies = dungeonMap.getAllOfType(ZombieToast.class);
        List<Hydra> hydras = dungeonMap.getAllOfType(Hydra.class);
        mercs.stream().forEach(m -> m.setMoveStrategy(new DefaultMercenaryMovement()));
        zombies.stream().forEach(m -> m.setMoveStrategy(new ZombieMovement()));
        hydras.stream().forEach(m -> m.setMoveStrategy(new ZombieMovement()));
    }

    @Override
    public void applyEffect(DungeonMap dungeonMap, Player player) {
        List<Mercenary> mercs = dungeonMap.getAllOfType(Mercenary.class);
        List<ZombieToast> zombies = dungeonMap.getAllOfType(ZombieToast.class);
        List<Hydra> hydras = dungeonMap.getAllOfType(Hydra.class);
        mercs.stream().forEach(m -> m.setMoveStrategy(new ScaredEnemyMovement()));
        zombies.stream().forEach(m -> m.setMoveStrategy(new ScaredEnemyMovement()));
        hydras.stream().forEach(m -> m.setMoveStrategy(new ScaredEnemyMovement()));
    }
}
