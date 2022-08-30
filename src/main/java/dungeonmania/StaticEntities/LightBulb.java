// package dungeonmania.StaticEntities;

// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.List;

// import dungeonmania.LogicalEntity;
// import dungeonmania.StaticEntity;
// import dungeonmania.util.Position;

// public class LightBulb extends StaticEntity implements LogicalEntity {
    
//     private boolean isOn = false;
//     private List<Wire> adjacentWires = new ArrayList<>();

//     public LightBulb(String id, Position position) {
//         super(id, "light_bulb_off", position);
//         this.setCanMoveOver(true);
//     }   

//     public void removeAdjacentWire(Wire wire) {
//         //TODO make stream to remove particular wire
//         adjacentWires.removeIf(item -> item.equals(wire));
//     }

//     public void checkIfTurnOnOn() {
        
//         int onWires = 0;
//         adjacentWires.stream().forEach(wire -> {
//             if (wire.isIsOn()) {
                
//             }
//         });

//     }

//     /**
//      * @return boolean return the isOn
//      */
//     public boolean isIsOn() {
//         return isOn;
//     }

//     /**
//      * @param isOn the isOn to set
//      */
//     public void setIsOn(boolean isOn) {
//         this.isOn = isOn;
//     }

//     /**
//      * @return List<Wire> return the adjacentWires
//      */
//     public List<Wire> getAdjacentWires() {
//         return adjacentWires;
//     }

//     /**
//      * @param adjacentWires the adjacentWires to set
//      */
//     public void setAdjacentWires(List<Wire> adjacentWires) {
//         this.adjacentWires = adjacentWires;
//     }

//     @Override
//     public boolean getIsOn() {
//         // TODO Auto-generated method stub
//         return false;
//     }

//     @Override
//     public void checkIfCanTurnOn() {
//         // TODO Auto-generated method stub
        
//     }

//     @Override
//     public List<Wire> getAdjacentLogicEntities() {
//         // TODO Auto-generated method stub
//         return null;
//     }

//     @Override
//     public String getLogic() {
//         // TODO Auto-generated method stub
//         return null;
//     }

// }
