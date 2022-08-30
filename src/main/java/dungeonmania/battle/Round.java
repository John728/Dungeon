package dungeonmania.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Config;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Settings;
import dungeonmania.Items.InvincibilityPotion;
import dungeonmania.Items.Weapon;
import dungeonmania.MovingEntities.Enemy;
import dungeonmania.MovingEntities.Hydra;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

/*
 * Written by John Henderson
 */

/*
 * A class to preform a single round of battle
 */

public class Round implements Serializable {

    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<Item> itemsUsed = new ArrayList<>();

    public Round(Player player, Enemy enemy) {
        
        double initialPlayerHealth = player.getHealth();
        double initialEnemyHealth = enemy.getHealth();

        // list of weapons that the player has
        this.itemsUsed = getPlayerWeapons(player);

        // make list of weapons and potions
        if (player.getActivePotion() != null) itemsUsed.add(player.getActivePotion());

        // change players health
        player.setHealth(getPlayersNewHealth(player, enemy));
        enemy.setHealth(getEnemiesNewHealth(player, enemy));

        // calculate difference in health
        this.deltaPlayerHealth = player.getHealth() - initialPlayerHealth;
        this.deltaEnemyHealth = enemy.getHealth() - initialEnemyHealth;
    }

    public List<Item> getPlayerWeapons(Player player) {
        // get all weapons
        List<Item> itemsUsed = player.getInventoryController().getInventory().stream()
                                     .filter(item -> item instanceof Weapon)
                                     .collect(Collectors.toList()); 
                                     
        // decrease durability of weapons
        itemsUsed.forEach(item -> decreaseDurability(player, (Weapon)item));

        // remove all duplicate types of weapons
        Iterator<Item> it = itemsUsed.iterator();
        while (it.hasNext()) {
            Item item = (Item) it.next();
            if (itemsUsed.stream().filter(i -> i.getType().equals(item.getType())).count() > 1) {
                it.remove();
            }
        }

        return itemsUsed;
    }

    /**
     * @param player the player that is fighting the enemy
     * @param enemy the enemy that is being fought
     * @return the new health of the player
     */
    private double getPlayersNewHealth(Player player, Enemy enemy) {
        if (player.getActivePotion() instanceof InvincibilityPotion) return player.getHealth(); 
        return player.getHealth() - getEnemiesAdjustedDamage(player, enemy)/10.0;
    }

    /**
     * @param player the player that is fighting the enemy
     * @param enemy the enemy that is being fought
     * @return what the health of the enemy should be
     */
    private double getEnemiesNewHealth(Player player, Enemy enemy) {
        if (enemy instanceof Hydra) {
            Random rand = new Random(1);
            double chance = Config.getSettingAsDouble(Settings.hydra_health_increase_rate);
            if (rand.nextDouble() <= chance) {
                return enemy.getHealth() + Config.getSetting(Settings.hydra_health_increase_amount);
            }
        }
        if (player.getActivePotion() instanceof InvincibilityPotion) return 0;
        return enemy.getHealth() - getPlayersAdjustedDamage(player, enemy)/5.0;
    }

    /**
     * @param player the player that is fighting the enemy
     * @param enemy the enemy that is being fought
     * @return the adjusted damage of the player. Damage is adjusted whether the
     * player has weapons, potions, ect.
     */
    private int getPlayersAdjustedDamage(Player player, Enemy enemy) {
        int swordDamage = getWeaponDamage(player, "sword", true);
        int bowDamage = getWeaponDamage(player, "bow", false);
        int midnightArmourDamage = getWeaponDamage(player, "midnight_armour", true);

        // get the total damage
        return bowDamage * (player.getDamage() + swordDamage + midnightArmourDamage) + allyBonusAttack(player);
    }

    /**
     * @param player
     * @param type
     * @param isAdditive
     * @pre type is a weapon type
     * @return
     */
    private int getWeaponDamage(Player player, String type, boolean isAdditive) {
        
        Weapon item = (Weapon) itemsUsed.stream().filter(i -> i.getType().equals(type)).findFirst().orElse(null);
        
        if (item == null) {
            return isAdditive ? 0 : 1;
        }

        return item.getDamage();
    }

    private int getWeaponDefence(Player player, String type, boolean isAdditive) {
        
        Weapon item = (Weapon) itemsUsed.stream().filter(i -> i.getType().equals(type)).findFirst().orElse(null);
        
        if (item == null) {
            return isAdditive ? 0 : 1;
        }

        return item.getDefence();
    }

    private void decreaseDurability(Player player, Weapon item) {
        // the item is now fully used and is removed from the inventory
        item.decreaseDurability();
        if (item.getDurability() <= 0) {
            player.getInventoryController().removeItemFromInventory((Item) item);
        }
    }

    /**
     * @param player
     * @return the bonus received by the user from having an ally
     */
    private int allyBonusAttack(Player player) {
        return (int)player.getAllis().stream().count() * Config.getSetting(Settings.ally_attack);
    }

    /**
     * @param player
     * @param enemy
     * @return the adjusted damage of the enemy
     */
    private int getEnemiesAdjustedDamage(Player player, Enemy enemy) {
        int shieldDefence = getWeaponDefence(player, "shield", true);
        int midnightArmourDefence = getWeaponDefence(player, "midnight_armour", true);
        return enemy.getDamage() - shieldDefence - midnightArmourDefence - allyBonusDefense(player);
    }

    /**
     * @param player
     * @return the bonus received by the user from having an ally
     */
    private int allyBonusDefense(Player player) {
        return (int)player.getAllis().stream().count() * Config.getSetting(Settings.ally_defence);
    }

    public RoundResponse getRoundResponse() {
        List<ItemResponse> itemResponses = new ArrayList<>();
        itemsUsed.stream().forEach(item -> itemResponses.add(item.getItemResponse()));
        return new RoundResponse(deltaPlayerHealth, deltaEnemyHealth, itemResponses);
    }
}
