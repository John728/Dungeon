package dungeonmania.StaticEntities;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.StaticEntity;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {

    private int movement_factor;
    private Map<Entity, Integer> swampStatus = new HashMap<>();
    
    public SwampTile(String id, Position position, int movement_factor) {
        super(id, "swamp_tile", position);
        this.movement_factor = movement_factor;
        this.setCanMoveOver(true);
    }

    public void stepOnSwamp(Entity entity) {
        swampStatus.put(entity, movement_factor);
    }

    public void tickSwamp(Entity entity) {
        if (swampStatus.containsKey(entity)) {
            // Reduce the number of ticks until we can move off, if < 0 remove from map
            if (swampStatus.get(entity).equals(0)) {
                swampStatus.remove(entity);
            } else {
                swampStatus.put(entity, swampStatus.get(entity) - 1);
            }
        } 
    }

    public boolean canStepOffSwamp(Entity entity) {
        return !swampStatus.containsKey(entity);
    }

    public int getMovementFactor() {
        return movement_factor;
    }
}
