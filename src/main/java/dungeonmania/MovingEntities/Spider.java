package dungeonmania.MovingEntities;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.MovingEntity;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.MovementStrategies.SpiderMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity implements Enemy {

    private int direction;
    private int cycle;
    private Move move_strategy;
    private double health = Config.getSetting(Settings.spider_health);

    public Spider(String id, Position position) {
        super(id, "spider", position);
        this.direction = 1;
        this.cycle = 0;
        this.setMoveStrategy(new SpiderMovement());
    }
    
    @Override
    public Direction move(Dungeon dungeon) {
        return move_strategy.move(dungeon, this.getPosition());
    }

    public void setMoveStrategy(Move move_strategy) {
        this.move_strategy = move_strategy;
    }

    public int getDamage() {
        return Config.getSetting(Settings.spider_attack);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
