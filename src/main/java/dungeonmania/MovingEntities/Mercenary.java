package dungeonmania.MovingEntities;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.MovingEntity;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.MovementStrategies.DefaultMercenaryMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Enemy {

    private Move move_strategy;
    private double health = Config.getSetting(Settings.mercenary_health);
    private boolean isBribed;
    private Move prev_move_strategy = null;
    private int mindControlTimer = 0;
    protected int bribeAmount;

    public Mercenary(String id, Position position, int bribeAmount) {
        super(id, "mercenary", position);
        this.setIsInteractable(true);
        this.setMoveStrategy(new DefaultMercenaryMovement());
        this.bribeAmount = bribeAmount;
        setCanMoveOver(true);
    }

    public Mercenary(String id, Position position, double health, String type, int bribeAmount) {
        super(id, type, position);
        this.setIsInteractable(true);
        this.setMoveStrategy(new DefaultMercenaryMovement());
        setCanMoveOver(true);
        this.health = health;
        this.bribeAmount = bribeAmount;
    }

    public void decreaseMindControllTimer() {
        this.mindControlTimer--;
        if (this.mindControlTimer == 0) {
            setMoveStrategy(this.prev_move_strategy);
            setPrevMoveStrategy(null);
        }
    }

    @Override
    public Direction move(Dungeon dungeon) {
        return move_strategy.move(dungeon, this.getPosition());
    }

    public boolean isBribed() {
        return isBribed;
    }

    public void setBribed(boolean isBribed) {
        this.isBribed = isBribed;
    }

    /**
     * @return Move return the pmove_strategy
     */
    public Move getMoveStrategy() {
        return move_strategy;
    }
    
    public void setMoveStrategy(Move move_strategy) {
        this.move_strategy = move_strategy;
    }

    public int getDamage() {
        return Config.getSetting(Settings.mercenary_attack);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * @return Move return the prev_move_strategy
     */
    public Move getPrevMoveStrategy() {
        return prev_move_strategy;
    }

    /**
     * @param temp_move_strategy the prev_move_strategy to set
     */
    public void setPrevMoveStrategy(Move prev_move_strategy) {
        this.prev_move_strategy = prev_move_strategy;
    }

    public int getMindControlTimer() {
        return this.mindControlTimer;
    }

    public void setMindControlTimer(int mindControlTimer) {
        this.mindControlTimer = mindControlTimer;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }
}
