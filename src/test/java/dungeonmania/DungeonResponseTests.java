package dungeonmania;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class DungeonResponseTests {
    /*
     * d_responseTest_basicDungeon looks something like this 
     *          wall (0,1)
     *          player (1,1)                            exit (5, 1)
     *                  zombie_toast_spawner (2,2)
     */
    @Test
    @DisplayName("Test entity responses from new dungeon")
    public void testBasicEntitiesTest() {
        // Simple test with a player, wall and exit
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_responseTest_basicDungeon", "c_spiderTest_basicMovement");

        // Check the wall entity response
        assertEquals(countEntityOfType(initDungonRes, "wall"), 1);
        EntityResponse actualWall = getEntities(initDungonRes, "wall").get(0);
        EntityResponse expectedWall = new EntityResponse(actualWall.getId(), "wall", new Position(1, 0), false);
        assertEquals(actualWall, expectedWall);

        // Check the exit entity reponse
        assertEquals(countEntityOfType(initDungonRes, "exit"), 1);
        EntityResponse actualExit = getEntities(initDungonRes, "exit").get(0);
        EntityResponse expectedExit = new EntityResponse(actualExit.getId(), "exit", new Position(5, 1), false);
        assertEquals(actualExit, expectedExit);

        // Check player reponse
        assertEquals(countEntityOfType(initDungonRes, "player"), 1);
        EntityResponse actualPlayer = getPlayer(initDungonRes).get();
        EntityResponse expectedPlayer = new EntityResponse(actualPlayer.getId(), "player", new Position(1, 1), false);
        assertEquals(actualPlayer, expectedPlayer);

        // Check zombie toast spawner
        assertEquals(countEntityOfType(initDungonRes, "zombie_toast_spawner"), 1);
        EntityResponse actualZTS = getEntities(initDungonRes, "zombie_toast_spawner").get(0);
        EntityResponse expectedZTS = new EntityResponse(actualZTS.getId(), "zombie_toast_spawner", new Position(2, 2), true);
        assertEquals(actualZTS, expectedZTS);

        // Check number of entities
        assertEquals(initDungonRes.getEntities().size(), 4);
    }

}
