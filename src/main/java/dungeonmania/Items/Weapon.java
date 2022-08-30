package dungeonmania.Items;

/*
 * Written by John Henderson
 */

/*
 * A weapon interface that defines the methods that a weapon must implement.
 * This is used by the battle system to fetch and use the weapons in the players
 * inventory.
 */

public interface Weapon {
    public int getDamage();
    public int getDefence();
    public void decreaseDurability();
    public int getDurability();
    public String getType();
}
