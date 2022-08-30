package dungeonmania;

import static dungeonmania.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovementStrategies.BribedMercenaryMovement;
import dungeonmania.MovingEntities.MovementStrategies.ScaredEnemyMovement;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class MovementTests {
    @Test
    @DisplayName("Test the player can move down")
    public void testMovementDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test the player can move right")
    public void testMovementRight() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test the player can move left")
    public void testMovementLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 1), false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test the player can move up")
    public void testMovementUp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 0), false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test the player can move over floor switch, item and exit but not a walls")
    public void testMovementOverEntities() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementOverEntities", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // Move down over switch
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Move down over arrow
        actualDungonRes = dmc.tick(Direction.DOWN);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Cant move right over wall
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Cant move left over wall with an arrow on it
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Check no new players have been created
        assertEquals(countEntityOfType(initDungonRes, "player"), 1);

        // Can move down over exit
        actualDungonRes = dmc.tick(Direction.DOWN);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 4), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Testing the player moving boulders")
    public void testMovementBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementBoulders", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // This is initial setup
        //  
        //          B
        //  W   B   P   B   B
        //      E
        //

        // Move left to push bolder into wall
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Move up to push boulder
        actualDungonRes = dmc.tick(Direction.UP);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertTrue(getEntitiesStream(actualDungonRes, "boulder").anyMatch(e -> e.getPosition().equals(new Position(2, 0))));
        assertEquals(expectedPlayer, actualPlayer);

        // Move down and right to push boulder against boulder
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 2), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // Move down right and up to push boulder up
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.UP);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertTrue(getEntitiesStream(actualDungonRes, "boulder").anyMatch(e -> e.getPosition().equals(new Position(3, 1))));
        assertEquals(expectedPlayer, actualPlayer);

        // Move right to push boulder right
        actualDungonRes = dmc.tick(Direction.RIGHT);
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2), false);
        actualPlayer = getPlayer(actualDungonRes).get();
        assertTrue(getEntitiesStream(actualDungonRes, "boulder").anyMatch(e -> e.getPosition().equals(new Position(5, 2))));
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Testing the mercenary moving towards the player")
    public void testMercenaryHittingPlayer() {
        // Tests the merc moving into the player
        // Depends on our implementation
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_mercenaryMovement", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // Move left and right until mercinary is next to player
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.LEFT);
        // Player is now to the left of the merc

        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 1), false);
        EntityResponse mercenary = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        EntityResponse expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(2, 1), true);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(expectedMerc, mercenary);
    }

    @Test
    @DisplayName("Testing the mercenary walking into the player")
    public void testEnemyMercenary() {
        // Tests the merc moving into the player
        // Depends on our implementation
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribedMercenaryMovement", "c_movementTest_testMovementDown");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // Set the movement strategy to bribed
        Entity mercenary = dungeon.getMap().getAllOfType("mercenary").get(0);
        
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        EntityResponse expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(2, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);

        actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(countEntityOfType(actualDungonRes, "mercenary"), 0);
    }

    @Test
    @DisplayName("Testing the mercenary following the player when bribed")
    public void testBribedMercenary() {
        // Tests the merc moving into the player
        // Depends on our implementation
        // White box test
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribedMercenaryMovement", "c_movementTest_testMovementDown");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // Set the movement strategy to bribed
        Entity mercenaryEntity = dungeon.getMap().getAllOfType("mercenary").get(0);
        Mercenary mercenary = (Mercenary) mercenaryEntity;
        mercenary.setMoveStrategy(new BribedMercenaryMovement());
        
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        EntityResponse expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(2, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);

        actualDungonRes = dmc.tick(Direction.DOWN);
        mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(2, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);

        actualDungonRes = dmc.tick(Direction.RIGHT);
        mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(1, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);
    }

    @Test
    @DisplayName("Testing the mercenary running away from the player when scared")
    public void testScaredMercenary() {
        // Tests the merc moving into the player
        // Depends on our implementation
        // White box test
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_scaredMercenaryMovement", "c_movementTest_testMovementDown");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // Set the movement strategy to bribed
        Entity mercenaryEntity = dungeon.getMap().getAllOfType("mercenary").get(0);
        Mercenary mercenary = (Mercenary) mercenaryEntity;
        mercenary.setMoveStrategy(new ScaredEnemyMovement());
        
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        EntityResponse expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(4, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);

        actualDungonRes = dmc.tick(Direction.RIGHT);
        mercenaryReponse = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        expectedMerc = new EntityResponse(mercenary.getId(), mercenary.getType(), new Position(4, 1), true);
        assertEquals(mercenaryReponse, expectedMerc);
    }

    @Test
    @DisplayName("Testing the zombie moving")
    public void testBasicZombieMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieMovement", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        EntityResponse initZombie = getEntities(initDungonRes, "zombie_toast").get(0);

        // Move left and right a lot (there is a slight chance this test fails but highly unlikely)
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);

        for (int i = 0; i < 200; i++) {
            actualDungonRes = dmc.tick(Direction.LEFT);
        }

        EntityResponse zombie = getEntities(dmc.getDungeonResponseModel(), "zombie_toast").get(0);
        assertFalse(zombie.getPosition().equals(initZombie.getPosition()));
    }

    @Test
    @DisplayName("Testing the zombie moving into door")
    public void testZombiesWithDoors() {
        // // example layout of the map 
        // //      P
        // //      K   
        // //          W   W   W
        // //          D   Z   W
        // //          W   W   W

        // DungeonManiaController dmc = new DungeonManiaController();
        // DungeonResponse initDungonRes = dmc.newGame("d_zombieMovementWithDoors", "c_movementTest_testMovementDown");
        // EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        // actualDungonRes = dmc.tick(Direction.DOWN);
        // actualDungonRes = dmc.tick(Direction.RIGHT);
        // actualDungonRes = dmc.tick(Direction.RIGHT);
        
        // for (int i = 0; i < 500; i++) {
        //     actualDungonRes = dmc.tick(Direction.LEFT);
        // }

        // EntityResponse zombie = getEntities(dmc.getDungeonResponseModel(), "zombie_toast").get(0);
        // assertFalse(zombie.getPosition().equals(new Position(4,3)));
        // assertFalse(zombie.getPosition().equals(new Position(5,3)));
    }

    @Test
    @DisplayName("Check that the spider moves correctly")
    public void testSpiderMovement() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_testSpiderMovement", "c_goalsTest_basicTreasureGoal");

        // spider inital position
        assertEquals(new Position(5,5), getEntities(res, "spider").get(0).getPosition());

        // spider has moved up
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(5,4), getEntities(res, "spider").get(0).getPosition());

        // moved right
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(6,4), getEntities(res, "spider").get(0).getPosition());

        // moved down
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(6,5), getEntities(res, "spider").get(0).getPosition());

        // moved down
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(6,6), getEntities(res, "spider").get(0).getPosition());

        // moved down
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(5,6), getEntities(res, "spider").get(0).getPosition());

        // moved left
        res = dmc.tick(Direction.STAY);
        assertEquals(new Position(4,6), getEntities(res, "spider").get(0).getPosition());

        // get spider to same position
        res = dmc.tick(Direction.STAY);
        res = dmc.tick(Direction.STAY);
        res = dmc.tick(Direction.STAY);

        assertEquals(new Position(5,4), getEntities(res, "spider").get(0).getPosition());
    }
}