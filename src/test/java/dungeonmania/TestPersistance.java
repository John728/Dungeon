package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;



public class TestPersistance {
    @Test
    @DisplayName("")
    public void testMovementDown() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
        
        DungeonResponse res1 = dmc.saveGame("test");
        
        DungeonManiaController dmc1 = new DungeonManiaController();
        DungeonResponse res2 = dmc1.loadGame("test");

        assertEquals(res1.getDungeonId(), res2.getDungeonId());
        assertEquals(res1.getDungeonName(), res2.getDungeonName());
        assertEquals(res1.getEntities(), res2.getEntities());

        Persistance.clearDungeons();
    }

    @Test
    @DisplayName("Test all entities are saved")
    public void testOneOfAllEntities() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_one_of_all", "c_goalsTest_basicTreasureGoal");
        
        DungeonResponse res1 = dmc.saveGame("test");
        
        DungeonManiaController dmc1 = new DungeonManiaController();
        DungeonResponse res2 = dmc1.loadGame("test");

        assertEquals(res1.getDungeonId(), res2.getDungeonId());
        assertEquals(res1.getDungeonName(), res2.getDungeonName());
        assertEquals(res1.getEntities(), res2.getEntities());

        res1 = dmc1.tick(Direction.DOWN);

        dmc1.saveGame("test2");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse res3 = dmc2.loadGame("test2");
        
        assertEquals(res1.getDungeonId(), res3.getDungeonId());
        assertEquals(res1.getDungeonName(), res3.getDungeonName());
        
        for (EntityResponse er: res1.getEntities()) {
            assertTrue(res3.getEntities().contains(er));
        }


        Persistance.clearDungeons();
    }

    @Test
    @DisplayName("Test we get all the saved games")
    public void testGetSavedGames() {
        // clear any saved games that could mess this up
        Persistance.clearDungeons();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_one_of_all", "c_goalsTest_basicTreasureGoal");

        dmc.saveGame("test");
        dmc.saveGame("test2");
        dmc.saveGame("test3");
        dmc.saveGame("test4");
        
        List<String> expected = new ArrayList<String>(Arrays.asList("test", "test2", "test3", "test4"));

        for (int i = 0; i < expected.size(); i++) {
            assert(dmc.allGames().contains(expected.get(i)));
        }

        Persistance.clearDungeons();
    }
}