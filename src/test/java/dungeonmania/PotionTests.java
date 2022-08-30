package dungeonmania;

import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class PotionTests {
    @Test
    @DisplayName("Potion effects go in queue")
    public void basicTreasureGoal() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_simplePotionTest", "c_potionEffectsLong");
        Player player = dmc.getDungeon().getPlayer();

        // player has no effects
        assertFalse(player.getInvincible());
        assertFalse(player.getInvisible());

        // pick up potion
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assert(getInventory(res, "invincibility_potion").size() == 1);

        // use potion
        res = dmc.tick(getInventory(res, "invincibility_potion").get(0).getId());
        assert(getInventory(res, "invincibility_potion").size() == 0);
        assertTrue(player.getInvincible());

        // the duration of the potion has gone down
        assertEquals(player.getActivePotion().getDuration(), Config.getSetting(Settings.invincibility_potion_duration) - 1);
        
        // pick up the invisability potion
        res = dmc.tick(Direction.RIGHT);
        assert(getInventory(res, "invisibility_potion").size() == 1);

        // use potion
        res = dmc.tick(getInventory(res, "invisibility_potion").get(0).getId());
        assertEquals(player.getActivePotion().getDuration(), Config.getSetting(Settings.invincibility_potion_duration) - 3);
        assert(getInventory(res, "invisibility_potion").size() == 0);
        assertTrue(player.getInvincible());
        assertFalse(player.getInvisible()); // should not be applied yet

        dmc.tick(Direction.STAY);
        assertEquals(player.getActivePotion().getDuration(), Config.getSetting(Settings.invincibility_potion_duration) - 4);
        assertTrue(player.getInvincible());
        assertFalse(player.getInvisible());

        dmc.tick(Direction.STAY);
        assertEquals(player.getActivePotion().getDuration(), Config.getSetting(Settings.invisibility_potion_duration));
        assertFalse(player.getInvincible());
        assertTrue(player.getInvisible());

        dmc.tick(Direction.STAY);
        dmc.tick(Direction.STAY);
        dmc.tick(Direction.STAY);
        dmc.tick(Direction.STAY);
        dmc.tick(Direction.STAY);

        assertFalse(player.getInvincible());
        assertFalse(player.getInvisible());
    }   
}
