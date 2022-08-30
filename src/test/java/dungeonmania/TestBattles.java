package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import dungeonmania.Dungeon;
import dungeonmania.Items.Bow;
import dungeonmania.Items.InvincibilityPotion;
import dungeonmania.Items.InvisibilityPotion;
import dungeonmania.Items.Shield;
import dungeonmania.Items.Sword;

public class TestBattles {
    @Test
    @DisplayName("Test player battles spider and player dies")
    public void basicBattle() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basic_battle", "c_basic_battle");

        assertTrue(res.getEntities().size() == 2);

        res = dmc.tick(Direction.STAY);

        assertTrue(res.getEntities().size() == 1);
        assertTrue(res.getEntities().get(0).getType().equals("spider"));
    }

    @Test
    @DisplayName("Test example battle")
    public void testBattleExample() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basic_battle", "c_example_battle");

        // Items for the user
        List<Item> items = new ArrayList<>(Arrays.asList(
            new Sword(null, null), 
            new Sword(null, null), 
            new Shield(null, null),
            new Bow(null, null)
        ));

        items.forEach(item -> {
            dmc.getPlayer().getInventoryController().addItemToInventory(item);
        });

        // start battle
        res = dmc.tick(Direction.STAY);

        List<BattleResponse> battleResponse = res.getBattles();
        assertTrue(battleResponse.size() == 1);
        List<RoundResponse> roundResponses = battleResponse.get(0).getRounds();

        double epsilon = 0.000001d; // error

        assertTrue(roundResponses.size() > 2);
        assertEquals(roundResponses.get(0).getDeltaCharacterHealth(), -0.3, epsilon);
        assertEquals(roundResponses.get(0).getDeltaEnemyHealth(), -2.4, epsilon);
        assertTrue(roundResponses.get(0).getWeaponryUsed().size() == 3);
        assertEquals(roundResponses.get(1).getDeltaCharacterHealth(), -0.3, epsilon);
        assertEquals(roundResponses.get(1).getDeltaEnemyHealth(), -2.4, epsilon);
        assertTrue(roundResponses.get(1).getWeaponryUsed().size() == 3);
    }

    @Test
    @DisplayName("Test Invincible battle")
    public void testBattlePotion() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basic_battle", "c_example_battle");

        // Items for the user
        List<Item> items = new ArrayList<>(Arrays.asList(
            new Sword("1", null), 
            new Sword("2", null), 
            new Shield("3", null),
            new Bow("4", null)
        ));

        items.forEach(item -> {
            dmc.getPlayer().getInventoryController().addItemToInventory(item);
        });

        dmc.getPlayer().getInventoryController().addItemToInventory(new InvincibilityPotion("potion", null));

        // start battle
        res = dmc.tick("potion");

        List<BattleResponse> battleResponse = res.getBattles();
        assertTrue(battleResponse.size() == 1);
        List<RoundResponse> roundResponses = battleResponse.get(0).getRounds();

        double epsilon = 0.000001d; // error

        assertTrue(roundResponses.size() == 1);
        assertEquals(roundResponses.get(0).getDeltaCharacterHealth(), 0, epsilon);
        assertEquals(roundResponses.get(0).getDeltaEnemyHealth(), -10, epsilon);
        assertTrue(roundResponses.get(0).getWeaponryUsed().size() == 4);
    }

    @Test
    @DisplayName("Test invisable battle battle")
    public void testBattlePotion2() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basic_battle", "c_example_battle");

        dmc.getPlayer().getInventoryController().addItemToInventory(new InvisibilityPotion("potion", null));

        // start battle
        res = dmc.tick("potion");

        List<BattleResponse> battleResponse = res.getBattles();
        assertTrue(battleResponse.size() == 0);
    }
}