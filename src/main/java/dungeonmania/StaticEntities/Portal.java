package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.DungeonMap;
import dungeonmania.StaticEntity;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    
    private String portalColour; 
    private Portal corrPortal = null; 

    public Portal(String id, Position position, String portalColour) {
        super(id, "portal", position);
        this.portalColour = portalColour;
    }
    
    public Portal getLinkedPortal(DungeonMap dm){
        if (corrPortal != null) {
            return corrPortal;
        }else { // loop through to find portal
            List<Portal> portals = dm.getPortals();
            for (Portal portal: portals) {
                if (portal.getPortalColour().equals(this.portalColour) && (!portal.equals(this)) ) {
                    corrPortal = portal;
                    portal.setCorrPortal(this);
                }
            }
            return corrPortal;
        }
    }
    

    /**
     * @return int return the portalColour
     */
    public String getPortalColour() {
        return portalColour;
    }

    /**
     * @param portalColour the portalColour to set
     */
    public void setPortalColour(String portalColour) {
        this.portalColour = portalColour;
    }

    /**
     * @return Portal return the corrPotal
     */
    public Portal getCorrPortal() {
        return corrPortal;
    }

    /**
     * @param corrPotal the corrPotal to set
     */
    public void setCorrPortal(Portal corrPotal) {
        this.corrPortal = corrPotal;
    }

}
