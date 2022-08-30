package dungeonmania.MovingEntities;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.MovingEntity;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.MovementStrategies.ZombieMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements Enemy {
    
    private Move move_strategy;
    private double health = Config.getSetting(Settings.zombie_health);

    public ZombieToast(String id, Position position) {
        super(id, "zombie_toast", position);
        this.setMoveStrategy(new ZombieMovement());
    }

    @Override
    public Direction move(Dungeon dungeon) {
        return move_strategy.move(dungeon, this.getPosition());
    }

    public void setMoveStrategy(Move move_strategy) {
        this.move_strategy = move_strategy;
    }

    public int getDamage() {
        return Config.getSetting(Settings.zombie_attack);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
