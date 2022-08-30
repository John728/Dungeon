package dungeonmania.Items;

import dungeonmania.*;
import dungeonmania.util.Position;

public abstract class Potion extends Item { 
    private int duration;

    public Potion(String id, String type, Position position, int duration) {
        super(id, type, position);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public abstract void applyEffect(DungeonMap dungeonMap, Player player);

    public abstract void endEffect(DungeonMap dungeonMap, Player player);
}
