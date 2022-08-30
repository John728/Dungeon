package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Items.InvincibilityPotion;
import dungeonmania.Items.InvisibilityPotion;
import dungeonmania.Items.Key;
import dungeonmania.Items.Potion;
import dungeonmania.Items.SunStone;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity {
    private double health;
    private int maxHealth;
    private int atkDmg;
    private Position lastPos;

    // private List<Item> inventory = new ArrayList<>();
    private InventoryController inventoryController = null;
    private List<Mercenary> allies = new ArrayList<>();

    private Queue<Potion> activePotions = new LinkedList<>();
    

    public Player(String id, Position position) {
        super(id, "player", position); 
        this.maxHealth = Config.getSetting(Settings.player_health);
        this.health = maxHealth;
        this.atkDmg = Config.getSetting(Settings.player_attack);
        // this.inventory = new ArrayList<>();
        this.inventoryController = new InventoryController();
        setCanMoveOver(true);
    }

    public void movePlayer(DungeonMap dm, Direction direction) {

        // get old position of player and get position the player wants to move to
        Position oldPos = new Position(this.getPosition());
        Position newPos = oldPos.translateBy(direction);

        List<Entity> ent = dm.getEntitiesAt(newPos);
        // list of things the player can move over
        List<Class> canMoveOver = Arrays.asList(FloorSwitch.class,
        Item.class, Exit.class, Mercenary.class, SwampTile.class);

        if (MovementHandler.checkEntsOnlyContains(ent, canMoveOver) || MovementHandler.checkEnemy(ent)) {
            // move player, if grid is null, empty or we can move over that entity
            dm.moveEntity(this.getId(), oldPos, newPos);
        } else if (MovementHandler.checkBoulder(ent)) {
            // if new position has a boulder then make a newnew position behind that boulder and get the entities there

            if (MovementHandler.isBoulderMoveable(dm, ent, direction)) {
                // get boulder at the new position for its id
                Boulder boulder = (Boulder) MovementHandler.getBoulderAtPos(ent);

                Position boulderOldPos = boulder.getPosition();
                // move boulder from new position to newnew position and move player from old position to new position
                dm.moveEntity(boulder.getId(), newPos, newPos.translateBy(direction));
                dm.moveEntity(this.getId(), oldPos, newPos);
                
                Position boulderNewPos = boulder.getPosition();

                boulder.checkAndDeactivateFloorSwitch(dm, boulderOldPos);
                boulder.checkAndActivateFloorSwitch(dm, boulderNewPos);

            }         
        } else if (MovementHandler.checkPortal(ent)) {
            MovementHandler.sendThroughPortal(dm, oldPos, newPos, direction, this);

        } else if (MovementHandler.checkDoor(ent)) {
            Entity entity = MovementHandler.getDoorAtPos(ent);
            Door door = (Door) entity;
            if (door.isOpen()) {
                dm.moveEntity(this.getId(), oldPos, newPos);
            } else {
                Item sunStoneItem = inventoryController.getInventory().stream().filter(SunStone.class::isInstance).findFirst().orElse(null);
                if (sunStoneItem !=null) {
                    door.setOpen(true);
                    dm.moveEntity(this.getId(), oldPos, newPos);
                }
                else {
                    int keynum = door.getKey();
                    List<Item> inventory = this.inventoryController.getInventory();
                    Item keyItem = inventory.stream().filter(Key.class::isInstance).findFirst().orElse(null);
                    if (keyItem != null) {
                        Key key = (Key) keyItem;
                        if (key.getKey() == keynum) {
                            door.setOpen(true);
                            dm.moveEntity(this.getId(), oldPos, newPos);
                            inventoryController.removeItemFromInventory(keyItem);
                        }
                    }
                }
                
            }
        }

        // The player moved so update its last position
        this.setLastPos(oldPos);
    }
    
    /**
     * @return double return the health
     */
    public double getHealth() {
        return health;
    }

    public List<Item> getInventory() {
        return inventoryController.getInventory();
    }

    /**
     * @param health the health to set
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * @return InventoryController return the inventoryController
     */
    public InventoryController getInventoryController() {
        return inventoryController;
    }

    /**
     * @return boolean return the invisible
     */
    public boolean getInvisible() {
        return getActivePotion() instanceof InvisibilityPotion;
    }

    /**
     * @return boolean return the invincible
     */
    public boolean getInvincible() {
        return getActivePotion() instanceof InvincibilityPotion;
    }

    public int getDamage() {
        return atkDmg;
    }

    public void addAlly(Mercenary ally) {
        allies.add(ally);
    }
    public void removeAllies(Mercenary ally) {
        allies.remove(ally);
    }

    public List<Mercenary> getAllis() {
        return allies;
    }

    public void setLastPos(Position lastPos) {
        this.lastPos = lastPos;
    }

    public Position getLastPos() {
        return lastPos;
    }

    public void addActivePotion(Potion potion) {
        activePotions.add(potion);
    }

    /**
     * @param dungeon - the current dungeon
     * 
     * Needs to be called once per tick
     */
    public void checkActivePotions(DungeonMap dungeonMap) {

        // get the active potion at head of queue
        Potion activePotion = activePotions.peek();
        if (activePotion == null) {
            return;
        }

        // count down the dutation
        activePotion.setDuration(activePotion.getDuration() - 1);

        // if finished, end effects, remove, and set effects of next
        if (activePotion.getDuration() == 0) {
            activePotion.endEffect(dungeonMap, this);
            activePotions.poll();
        }

        // get the effects of the next potion going
        activePotion = activePotions.peek();
        if (activePotion == null) {
            return;
        }
        activePotion.applyEffect(dungeonMap, this);
    }

    public Potion getActivePotion() {
        return activePotions.peek();
    }
}
