package dungeonmania;

import static dungeonmania.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTests {

    @Test
    @DisplayName("Test the player can move right through a portal")
    public void testPortalRight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = null;

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        
        
        // assert after movement
        expectedPosition = new Position(6, 1);
        assertEquals(expectedPosition, actualPlayer.getPosition());
    }

    @Test
    @DisplayName("Test the player can move down through a portal")
    public void testPortalDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = null;

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        
        
        // assert after movement
        expectedPosition = new Position(1, 6);
        assertEquals(expectedPosition, actualPlayer.getPosition());
    }

    // @Test
    // @DisplayName("Test the player can move down through a portal with multiple portals ")
    // public void testMultiPortals() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse initDungonRes = dmc.newGame("d_portalTest", "c_movementTest_testMovementDown");
    //     EntityResponse initPlayer = getPlayer(initDungonRes).get();
    //     Position expectedPosition = null;

    //     DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
    //     EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        
        
    //     // assert after movement
    //     expectedPosition = new Position(1, 6);
    //     assertEquals(expectedPosition, actualPlayer.getPosition());
    // }

    @Test
    @DisplayName("Test the player can move down through a portal and come back up ")
    public void testGoBackThroughPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = null;

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        
        
        // assert after movement
        expectedPosition = new Position(1, 6);
        assertEquals(expectedPosition, actualPlayer.getPosition());
        
        actualDungonRes = dmc.tick(Direction.UP);

        actualPlayer = getPlayer(actualDungonRes).get();

        expectedPosition = new Position(1, 1);
        assertEquals(expectedPosition, actualPlayer.getPosition());
    }

    @Test
    @DisplayName("Test fully blocked portal")
    public void testFullyBlockedPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_advanced_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = new Position(1,1);

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPosition, actualPlayer.getPosition());
    
    }

    @Test
    @DisplayName("Test partially blocked portal")
    public void testPartiallyBlockedPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_advanced_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = new Position(3,4);

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPosition, actualPlayer.getPosition());
    }

    
    @Test
    @DisplayName("Test portal chain")
    public void testPortalChain() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_advanced_portalTest", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        Position expectedPosition = new Position(5,3);

        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.UP);
        actualDungonRes = dmc.tick(Direction.UP);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPosition, actualPlayer.getPosition());
    }
}
