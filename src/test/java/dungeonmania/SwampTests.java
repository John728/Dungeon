package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovementStrategies.BribedMercenaryMovement;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTests {
    
    @Test
    @DisplayName("Spider movement around swamp")
    public void testSpidersMovementSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderSwampTest", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      1   2   3   4 
        // 1    P   -   -   -
        // 2    -   -   1   2
        // 3    -   -   S   0
        // 4    -   -   B   -
        // 
        // 1 indicates swamp of movement_factor 1, similar applies to other numbers

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);

        // Move onto 1 swamp tile
        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));

        // Stuck 1
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(3, 2));

        // Move onto 2 swamp tile
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));

        // Stuck 1
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));

        // Stuck 2
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 2));

        // Move onto 0 swamp tile
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 3));

        // Move off of 0 swamp tile
        actualDungonRes = dmc.tick(Direction.STAY);
        spider = getEntities(dmc.getDungeonResponseModel(), "spider").get(0);
        assertEquals(spider.getPosition(), new Position(4, 4));
    }

    @Test
    @DisplayName("Mercenary movement around swamps 1")
    public void testMercenarySwampMovement1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercSwampTest1", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      0   1   2   3 
        // 0    -   -   -   -
        // 1    -   1   -   -
        // 2    P   2   M   -
        // 3    -   3   -   -
        // 
        // 1 indicates swamp of movement_factor 1, similar applies to other numbers
        // Mercenary should move through 2 to get to the player

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        assertEquals(merc.getPosition(), new Position(1, 2));

    }

    @Test
    @DisplayName("Mercenary movement around swamps 2")
    public void testMercenarySwampMovement2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercSwampTest2", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      0   1   2   3 
        // 0    -   10  -   -
        // 1    P   10  -   -
        // 2    -   10  M   -
        // 3    -   10  10  10
        // 
        // 1 indicates swamp of movement_factor 1, similar applies to other numbers
        // Mercenary should move up to get to the player

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        assertEquals(merc.getPosition(), new Position(2, 1));
    }

    @Test
    @DisplayName("Mercenary movement around swamps 3")
    public void testMercenarySwampMovement3() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercSwampTest3", "c_spiderTestDontSpawn");

        //  Grid like displayed, checking spider moves in correct path
        //      0   1   2   3 
        //
        // 0    -   4   -   -
        // 1    P   6   M   -
        // 2    -   1   -   -
        // 3    -   1   -   -
        // 
        // 1 indicates swamp of movement_factor 1, similar applies to other numbers
        // Mercenary should move down to get to the player

        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);

        DungeonResponse actualDungonRes = dmc.tick(Direction.STAY);
        merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        assertEquals(merc.getPosition(), new Position(2, 2));
    }
}
