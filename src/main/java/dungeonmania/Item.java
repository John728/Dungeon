package dungeonmania;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class Item extends Entity {
    private int durability;
    // private boolean stackable; //Don't know if need this

    public Item(String id, String type, Position position) {
        super(id, type, position);
        // Most items don't have durability, appropriate items can set their own durability
        this.durability = 0;
        this.setCanMoveOver(true);
    }

    public int getDurability() {
        return durability;
    } 

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(this.getId(), this.getType());
    }

    public abstract boolean useItem(DungeonMap dungeonMap, InventoryController inventoryController);

}
