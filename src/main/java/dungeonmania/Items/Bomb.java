package dungeonmania.Items;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Config;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.InventoryController;
import dungeonmania.Item;
// import dungeonmania.LogicalEntity;
import dungeonmania.Settings;
import dungeonmania.StaticEntities.FloorSwitch;
// import dungeonmania.StaticEntities.Wire;
import dungeonmania.util.Position;

public class Bomb extends Item {
    private int explodeRadius;
    private boolean placed;

    public Bomb(String id, Position position) {
        super(id, "bomb", position);
        this.explodeRadius = Config.getSetting(Settings.bomb_radius);
        this.placed = false;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    @Override // maybe just pass it player pos insted
    public boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController) {
        // add to dungeon at player position
        Position newPos = dungeonMap.getPlayerPos();
        int layer = newPos.getLayer(); // make layer one lower
        newPos = newPos.asLayer(layer);
        setPosition(newPos);   // this may cause a problem not sure don't think there will ever be
        setCanMoveOver(false);
                                // case where we can place bomb on top of something 
        dungeonMap.addEntity(this);
        this.placed = true;

        // subscribe to any nearby switches
        List<Entity> entities = new ArrayList<>();
        entities = dungeonMap.getAdjacentEntities(getPosition());

        for (Entity entity: entities) {
            if (entity instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) entity;
                floorSwitch.addNearbyBomb(this);
                if (floorSwitch.getIsActive()) {
                    explode(dungeonMap);
                }
            }
        }

        return true;
    }

    // This should be called by either a switch or after each movement tick
    public void explode(DungeonMap dungeonMap) {
        Position pos = getPosition();

        // can't use this due to radius changing
        // List<Position> positions = pos.getAdjacentPositions();
        
        int x = pos.getX();
        int y = pos.getY();
        for (int i = -explodeRadius; i < explodeRadius + 1; i++) {
            for (int j = -explodeRadius; j < explodeRadius + 1; j++ ) {
                Position newPos = new Position(x+i, y+j);
                dungeonMap.removeEntitiesAtXYCoordinates(newPos);
            }
        }
    }

    // @Override
    // public boolean getIsOn() {
    //     // TODO Auto-generated method stub
    //     return false;
    // }

    // @Override
    // public void checkIfCanTurnOn() {
    //     // TODO Auto-generated method stub
        
    // }

    // @Override
    // public List<Wire> getAdjacentLogicEntities() {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    // @Override
    // public String getLogic() {
    //     // TODO Auto-generated method stub
    //     return null;
    // }
}
