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

public class SpiderTests {
    @Test
    @DisplayName("Dungeon spawns Spiders")
    public void testSpawnSpiders() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTests", "c_spiderTestSpawn");
        EntityResponse initPlayer = getPlayer(res).get();


        // move player left
        for (int i = 0; i < 11; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        

        // assert after movement
        assertEquals(3, getEntities(res, "spider").size());
    }

    @Test
    @DisplayName("Spider movement around boulders")
    public void testSpidersMovementBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderBoulderTest1", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      1   2   3   4 
        // 1    P   -   -   -
        // 2    -   -   -   -
        // 3    -   -   S   -
        // 4    -   -   -   B

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 3));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));
    }

    @Test
    @DisplayName("Spider movement around boulders anticlockwise")
    public void testSpidersAntiMovementBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderBoulderTest2", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      1   2   3   4 
        // 1    P   -   -   -
        // 2    -   -   -   -
        // 3    -   -   S   -
        // 4    -   B   -   B

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        actualDungonRes = dmc.tick(Direction.STAY);
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 3));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));

        actualDungonRes = dmc.tick(Direction.STAY);
        actualDungonRes = dmc.tick(Direction.STAY);
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(2, 3));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(2, 2));
    }

    @Test
    @DisplayName("Spider movement stuck between boulders")
    public void testSpidersBouldersWithPlayer1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderBoulderTest3", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      0   1   2   3   4 
        // 0    -   -   P   -   -
        // 1    -   -   B   -   -
        // 2    -   -   -   -   B
        // 3    -   -   -   S   -
        // 4    -   -   -   -   -

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));

        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));
    }
}
