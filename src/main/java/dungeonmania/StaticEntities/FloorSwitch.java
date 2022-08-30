package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonMap;
import dungeonmania.StaticEntity;
import dungeonmania.Items.Bomb;
import dungeonmania.util.Position;

// I doubt this would happen but possible bug it that if bomb activated
// it may remvoe other things after it has exploded
// public class FloorSwitch extends StaticEntity implements LogicalEntity {
    public class FloorSwitch extends StaticEntity {
    private boolean isActive;
    private List<Bomb> nearbyBombs = new ArrayList<>();

    public FloorSwitch(String id, Position position) {
        super(id, "switch", position);
        this.setCanMoveOver(true);
    }

    public void addNearbyBomb(Bomb bomb) {
        this.nearbyBombs.add(bomb);
    }

    public void activate(DungeonMap dungeonMap) {        
        this.isActive = true;
        for (Bomb bomb: nearbyBombs) {
            bomb.explode(dungeonMap);
        }
    }

    public void deactivate() {
        this.isActive = false;
    }
    
    /**
     * @return boolean return the isActive
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


    /**
     * @return List<Bomb> return the nearbyBombs
     */
    public List<Bomb> getNearbyBombs() {
        return nearbyBombs;
    }

    /**
     * @param nearbyBombs the nearbyBombs to set
     */
    public void setNearbyBombs(List<Bomb> nearbyBombs) {
        this.nearbyBombs = nearbyBombs;
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