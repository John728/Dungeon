package dungeonmania.MovingEntities;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.MovingEntity;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.MovementStrategies.ZombieMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements Enemy {

    private Move move_strategy;
    private double health = Config.getSetting(Settings.hydra_health);

    public Hydra(String id, Position position) {
        super(id, "hydra", position);
        this.setMoveStrategy(new ZombieMovement());
    }

    public int getDamage() {
        return Config.getSetting(Settings.hydra_attack);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public Direction move(Dungeon dungeon) {
        return move_strategy.move(dungeon, this.getPosition());
    }

    public void setMoveStrategy(Move move_strategy) {
        this.move_strategy = move_strategy;
    }
    
    
}
