package dungeonmania;

import java.io.Serializable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity implements Serializable {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private boolean canMoveOver;

    public Entity(String id, String type, Position position) {
        this.id = id;
        this.type = type;
        this.position = position;
        // Assume entity is not interacable, change in instance if required
        this.isInteractable = false;
        // Assume every entity can not be moved over by anything by default
        // Some items can change from being allowed to move over to not
        this.canMoveOver = false;
    }


    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return String return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Position return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @param isInteractable the isInteractable to set
     */
    public void setIsInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, type, position, isInteractable);
    }

    public void setCanMoveOver(boolean canMoveOver) {
        this.canMoveOver = canMoveOver;
    }

    public boolean getCanMoveOver() {
        return this.canMoveOver;
    }

}
