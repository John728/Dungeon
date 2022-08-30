package dungeonmania;


import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class BuildTests {
    
    @Test
    @DisplayName("Test the player can't make a bow without enough items")
    public void testMakeSwordFails() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildBow", "c_movementTest_testMovementDown");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // res = dmc.build("sword");
        assertThrows(IllegalArgumentException.class, () -> dmc.build("sword"));
        assertEquals(0, getInventory(res, "sword").size());

    }

    @Test
    @DisplayName("Test the player can make a bow")
    public void testMakeBow() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildBow", "c_movementTest_testMovementDown");

        // create the expected result
        // EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player downward
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.tick(Direction.RIGHT);
        // EntityResponse player = getPlayer(actualDungonRes).get();

        res = dmc.build("bow");
        
        assertEquals(1, getInventory(res, "bow").size());

    }

    @Test
    @DisplayName("Test the player can't make a bow without enough items")
    public void testCannotMakeBow() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildBow", "c_movementTest_testMovementDown");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // res = dmc.build("bow");
        
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));
        assertEquals(0, getInventory(res, "bow").size());

    }

    @Test
    @DisplayName("Test the player make a Key")
    public void testMakeShieldWithKey() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildShield", "c_movementTest_testMovementDown");

        // create the expected result
        // EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player downward
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.build("shield");
        assertEquals(1, getInventory(res, "shield").size());
    }
    @Test
    @DisplayName("Test the player make a treasure")
    public void testMakeShieldWithTreasure() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildShield2", "c_movementTest_testMovementDown");

        // create the expected result
        // EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        // move player downward
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.build("shield");
        assertEquals(1, getInventory(res, "shield").size());

    }

    @Test
    @DisplayName("Test the player can't make shield without enough items")
    public void testCannotMakeShield() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildShield2", "c_movementTest_testMovementDown");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // res = assertThrows(InvalidActionException.class, dmc.build("shield"));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
        assertEquals(0, getInventory(res, "shield").size());

    }


    @Test
    @DisplayName("Test the player can make a sceptre with wood and key")
    public void testMakeSceptreWithWood() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildSceptre2", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        
        assertEquals(1, getInventory(res, "sceptre").size());

    }

    @Test
    @DisplayName("Test the player can make a sceptre with arrows")
    public void testMakeSceptreWithArrows() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildSceptreArrows", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        
        assertEquals(1, getInventory(res, "sceptre").size());

    }


    @Test
    @DisplayName("Test the player can make a sceptre with Treasure")
    public void testMakeSceptreWithTreasure() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildSceptreTreasure", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        
        assertEquals(1, getInventory(res, "sceptre").size());

    }

    @Test
    @DisplayName("Test the player can make a sceptre with 2 sunstones")
    public void testMakeSceptreWith2Sunstones() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildSceptreSunStone", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        
        assertEquals(1, getInventory(res, "sceptre").size());
    }

    @Test
    @DisplayName("Test the player can't make a sceptre")
    public void testCannotMakeSceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildSceptre2", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        // dmc.build("sceptre");
        
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
        // assertEquals(0, getInventory(res, "sceptre").size());

    }

    @Test
    @DisplayName("Test the player can make a midnight armour") 
    public void testMidnightArmour() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildMidnightArmour", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("midnight_armour");
        res = dmc.tick(Direction.RIGHT);
        // assertEquals(1, getInventory(res, "sun_stone").size());

        assertEquals(1, getInventory(res, "midnight_armour").size());
    }


    @Test
    @DisplayName("Test the player can't make midnight aromur without required materials")
    public void testCannotMakeMidnightArmour() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildMidnightArmour", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        // dmc.build("midnight_armour");
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, getInventory(res, "midnight_armour").size());
    }

    @Test
    @DisplayName("Test the player can't make midnight aromur due to zombies being in dungeon")
    public void testCannotMakeMidnightArmourWithZombies() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_buildMidnightArmourZombie", "M3_config");

        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // dmc.build("midnight_armour");
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, getInventory(res, "midnight_armour").size());
    }
}
