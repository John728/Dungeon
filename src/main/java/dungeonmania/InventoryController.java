package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Items.Arrows;
import dungeonmania.Items.Bomb;
import dungeonmania.Items.Bow;
import dungeonmania.Items.Key;
import dungeonmania.Items.MidnightArmour;
import dungeonmania.Items.Sceptre;
import dungeonmania.Items.Shield;
import dungeonmania.Items.SunStone;
import dungeonmania.Items.Sword;
import dungeonmania.Items.Treasure;
import dungeonmania.Items.Weapon;
import dungeonmania.Items.Wood;

public class InventoryController implements Serializable {

    private List<Item> inventory = new ArrayList<>();

    public InventoryController() {
    }

    public boolean useItem(DungeonMap dungeonMap, Item item) throws IllegalArgumentException {
        // returns true if item was used, false if item was not used
        if (item.useItem(dungeonMap, this)) {
            removeItemFromInventory(item);
            return true;
        }
        // if not usable throw an exception
        throw new IllegalArgumentException();
    }

    public void addItemstoInventory (List<Entity> entities, DungeonMap dungeonMap) {
        
        if (entities == null) {return;}
        
        // get all item entities
        List<Entity> entitiesCopy = new ArrayList<Entity>();
        entitiesCopy.addAll(entities.stream().filter(e -> e instanceof Item).collect(Collectors.toList()));

        // Remove bomb is it is placed
        for (Entity item: entities) {
            if (item instanceof Bomb) {
                Bomb bomb = (Bomb) item;
                if (bomb.isPlaced()) {
                    entitiesCopy.remove(bomb);
                }
            }
        }

        for (Entity entity : entitiesCopy) {
            if (entity instanceof Key && this.inventory.stream().anyMatch(Key.class::isInstance)) {
                continue;
            }
            dungeonMap.removeEntity(entity.getId(), entity.getPosition());
            Item item = (Item) entity;
            addItemToInventory(item);
        }
    }

    public Item getItemInInventory(String type) {
        try {
            return this.inventory.stream().filter(item -> item.getType().equals(type)).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public Item getItemInInventoryFromId(String id) {
        for (Item item: inventory) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return this.inventory.stream().filter(item -> item.getId().equals(id)).findFirst().get();
    }


    public boolean addItemToInventory(Item item) {
        this.inventory.add(item);
        return true;
    }

    /**
     * @param type
     * @return
     * @pre type is a valid item type, and is a weapon or armor
     */
    public Weapon getWeapon(String type) {
        return (Weapon) this.getItemInInventory(type);
    }

    public boolean removeItemFromInventory(Item item) {
        if (this.inventory.contains(item)) {
            this.inventory.remove(item);
            return true;
        }
        else {
            return false;
        }
    }

    public int getNumOfItem(String type) {
        return (int) inventory.stream().filter(item -> item.getType().equals(type)).count();
    }

    public int getNumOfTreasure() {
        return (int) inventory.stream().filter(item -> item instanceof Treasure ||
                                                       item instanceof SunStone).count();
    }

    public boolean checkCanBuildBow() {
        
        int wood_num = 0;
        int arrows_num = 0;
        for (Item item: this.inventory) {
            if (item instanceof Wood) {
                wood_num ++;
            } 
            else if (item instanceof Arrows) {
                arrows_num ++;
            }
        }

        if (wood_num >= 1 && arrows_num >= 3) {
            return true;
        }
        
        return false;
    }

    public void buildBow (DungeonMap dungeonMap) {
        
        // Issue: if the player does not have all the stuff,
        // the things he has will be wiped out after running this

        int wood_num = 0;
        int arrows_num = 0;
        List<Item> inventoryCopy = new ArrayList<Item>();
        inventoryCopy.addAll(inventory);
        for (Item item: inventoryCopy) {
            if (item instanceof Wood && wood_num < 1) {
                this.inventory.remove(item);
                wood_num ++;
            } 
            else if (item instanceof Arrows & arrows_num < 3) {
                this.inventory.remove(item);
                arrows_num ++;
            }
        }
        String id = DungeonManiaController.getNewId(dungeonMap, Bow.class);

        inventory.add(new Bow(id, null)); // For now just adding position to null if in inventory
    }

    public boolean checkCanBuildShield() {
        
        int wood_num = 0;
        int treasure_num = 0;
        int key_num = 0;
        for (Item item: this.inventory) {
            if (item instanceof Wood) {
                wood_num ++;
            } 
            else if (item instanceof Treasure ) {
                treasure_num ++;
            }
            else if (item instanceof Key || item instanceof SunStone) {
                key_num ++;
            }
        }

        if (wood_num >= 2 && (treasure_num >= 1 || key_num >= 1)) {
            return true;
        }

        
        return false;
    }

    public void buildShield (DungeonMap dungeonMap) {
        
        int wood_num = 0;
        int treasure_num = 0;
        int key_num = 0;
        int sun_stone_num = 0;
        List<Item> inventoryCopy = new ArrayList<Item>();
        inventoryCopy.addAll(inventory);
        for (Item item: inventoryCopy) {
            if (item instanceof Wood && wood_num < 2) {
                this.inventory.remove(item);
                wood_num ++;
            } 
            else if (item instanceof SunStone && treasure_num < 1 && key_num < 1 && sun_stone_num < 1) {
                this.inventory.remove(item);
                treasure_num ++;
            }
            else if ((item instanceof Treasure || item instanceof Key) && key_num < 1 && treasure_num < 1 && sun_stone_num < 1) {
                if (item instanceof Key) {
                    this.inventory.remove(item);
                    key_num ++;
                }
                if (item instanceof Treasure) {
                    this.inventory.remove(item);
                    treasure_num ++;
                }
                
            }
        }
        String id = DungeonManiaController.getNewId(dungeonMap, Shield.class);

        this.inventory.add(new Shield(id, null)); // For now just adding position to null if in inventory
    }

    public boolean checkCanBuildSceptre() {
        int wood_num = 0;
        int arrows_num = 0;
        int treasure_num = 0;
        int key_num = 0;
        int sun_stone_num = 0;
        for (Item item: this.inventory) {
            if (item instanceof Wood) {
                wood_num ++;
            } 
            else if (item instanceof Treasure ) {
                treasure_num ++;
            }
            else if (item instanceof Key) {
                key_num ++;
            }
            else if (item instanceof Arrows) {
                arrows_num ++;
            }
            else if (item instanceof SunStone) {
                sun_stone_num ++;
            }
        }

        // I don't think the remove during the loop should affect the loop but possible bug check back tom
        if ((wood_num >= 1 || arrows_num >= 2) && (treasure_num >= 1 || key_num >= 1 || sun_stone_num >= 2) && (sun_stone_num >= 1)) {
            return true;
        }

        
        return false;
    }

    public void buildSceptre(DungeonMap dungeonMap) {
        
        int wood_num = 0;
        int key_num = 0;
        int sun_stone_num = 0;
        int arrows_num = 0;
        Arrows arrows = null;
        List<Item> inventoryCopy = new ArrayList<Item>();
        inventoryCopy.addAll(inventory);
        for (Item item: inventoryCopy) {
            if (item instanceof Wood && wood_num < 1 && arrows_num < 2) {
                this.inventory.remove(item);
                wood_num ++;
            }
            else if (item instanceof Arrows && arrows_num < 2 && wood_num < 1) {
                arrows_num++;
                if (arrows_num >= 2) {
                    this.inventory.remove(item);
                    this.inventory.remove(arrows);
                }
                else {
                    arrows = (Arrows)item;
                }
            } 
            else if (item instanceof SunStone && sun_stone_num < 1) {
                this.inventory.remove(item);
                sun_stone_num ++;
            }
            else if ((item instanceof Treasure || item instanceof Key || item instanceof SunStone) && key_num < 1 && sun_stone_num < 2) {
                if (!(item instanceof SunStone)) {
                    this.inventory.remove(item);
                    key_num ++;
                }
                else {
                    sun_stone_num++;
                }
                
            }
        }
        String id = DungeonManiaController.getNewId(dungeonMap, Sceptre.class);

        this.inventory.add(new Sceptre(id, null));
    }

    public boolean checkCanBuildMidnightArmour(DungeonMap dungeonMap) {
        int sword_num = 0;
        int sun_stone_num = 0;
        for (Item item: this.inventory) {
            if (item instanceof Sword) {
                sword_num ++;
            }
            else if (item instanceof SunStone) {
                sun_stone_num ++;
            }
        }

        boolean zombiesExist = dungeonMap.checkZombiesExist();

        // I don't think the remove during the loop should affect the loop but possible bug check back tom
        if (sword_num >= 1 && sun_stone_num >= 1 && !zombiesExist) {
            return true;
        }

        
        return false;
    }

    public void buildMidnightArmour(DungeonMap dungeonMap) {
        
        int sword_num = 0;
        int sun_stone_num = 0;
        List<Item> inventoryCopy = new ArrayList<Item>();
        inventoryCopy.addAll(inventory);
        for (Item item: inventoryCopy) {
            if (item instanceof Sword && sword_num < 1) {
                this.inventory.remove(item);
                sword_num ++;
            } 
            else if (item instanceof SunStone && sun_stone_num < 1) {
                this.inventory.remove(item);
                sun_stone_num ++;
            }
        }
        String id = DungeonManiaController.getNewId(dungeonMap, MidnightArmour.class);

        this.inventory.add(new MidnightArmour(id, null)); // For now just adding position to null if in inventory
    }

    public List<String> getBuildables(DungeonMap dungeonMap) {
        List<String> buildables = new ArrayList<String>();
        
        if (checkCanBuildBow()) {
            buildables.add("bow");
        }
        if (checkCanBuildShield()) {
            buildables.add("shield");
        }
        if (checkCanBuildSceptre()) {
            buildables.add("sceptre");
        }
        if (checkCanBuildMidnightArmour(dungeonMap)) {
            buildables.add("midnight_armour");
        }
        return buildables;
    }

    /**
     * @return List<Item> return the inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }
    
}
