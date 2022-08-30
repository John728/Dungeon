package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class BombTests {
    @Test
    @DisplayName("Test the player can piuckup a bomb")
    public void testPlaceBomb() throws IllegalArgumentException, InvalidActionException {
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
        // System.out.println(res.getInventory());
        assertEquals(1, getInventory(res, "bow").size());


        // assert after movement
        // assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("tests placing and bomb exploding")
    public void explodeBomb () {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        res = dmc.tick(Direction.RIGHT);

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure

        // res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size()); 
    }

    @Test
    @DisplayName("tests placing bomb then exploding it via switch")
    public void explodeBombAfterPlace() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Pick up Bomb
        res = dmc.tick(Direction.RIGHT);
        // assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        
        //Activate switch
        res = assertDoesNotThrow(() -> dmc.tick(Direction.RIGHT)); 
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size()); 
    }

    @Test
    @DisplayName("tests activating then deactivationg a switch then placing bomb next to switch")
    public void bombWillNotExplodeWithDeactivatedSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_forDeaciveSwitch", "c_bombTest_placeBombRadius2");

        res = dmc.tick(Direction.RIGHT);
        
        // Activate switch
        res = dmc.tick(Direction.RIGHT);

        // Deactivate switch
        res = dmc.tick(Direction.RIGHT);
        // assertEquals(1, getInventory(res, "bomb").size());

        res = dmc.tick(Direction.DOWN);

        // pickup bomb
        res = dmc.tick(Direction.LEFT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size()); 
    }

    @Test
    @DisplayName("tests placing bomb then exploding it via switch with radius 1")
    public void explodeBambRadius1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_movementTest_testMovementDown");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Pick up Bomb
        res = dmc.tick(Direction.RIGHT);
        // assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        
        //Activate switch
        res = assertDoesNotThrow(() -> dmc.tick(Direction.RIGHT)); 
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size()); 
    }
}
