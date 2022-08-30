package dungeonmania;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.goals.Goals;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonBuilderTests {
    @Test
    @DisplayName("Test entrance in top left cannot go any further up or left.")
    public void testWallSurroundingEntrance() {
    //     Random rand = new Random();
    //     DungeonManiaController dmc = new DungeonManiaController();

    //     DungeonResponse res = dmc.generateDungeon(-27, 20, -35, 14, "simple");

    //     Position init = getPlayer(res).get().getPosition();

    //     // move player up and left multiple times
    //     for (int i = 0; i < 10; i++) {
    //         res = dmc.tick(Direction.LEFT);
    //         res = dmc.tick(Direction.UP);
    //     }

    //     Position fin = getPlayer(res).get().getPosition();

    //     // should not have moved due to wall boundary
    //     assertEquals(init, fin);
    }

    @Test
    @DisplayName("Test tiny 2x2 generatable")
    public void testMinimumSizedMaze() {
        // DungeonManiaController dmc = new DungeonManiaController();

        // DungeonResponse res2 = dmc.generateDungeon(-50, -49, -50, -49, "simple");

        // Position pos = getPlayer(res2).get().getPosition();
        // Position end = new Position(pos.getX() + 1, pos.getY() + 1);

        // assertEquals(new Position(-50, -50), getPlayer(res2).get().getPosition());

        // // move player down and right until at end
        // for (int i = 0; i < 2; i++) {
        //     res2 = dmc.tick(Direction.DOWN);
        //     pos = getPlayer(res2).get().getPosition();
        //     if (pos.equals(end)) break;

        //     res2 = dmc.tick(Direction.RIGHT);
        //     pos = getPlayer(res2).get().getPosition();
        //     if (pos.equals(end)) break;
        // }

        // assertEquals(new Position(-49, -49), getPlayer(res2).get().getPosition());

        // // goals are completed
        // assertTrue(Goals.isTrue());
        // assertEquals("", res2.getGoals());
    }    

    @Test
    @DisplayName("Test many size generatable")
    public void testAnySizedMaze() {
        // for (int i = 0; i < 5; i++) {
        //     Random rand = new Random();
        //     int xStart = rand.nextInt(60) - 50;
        //     int yStart = rand.nextInt(60) - 50;
        //     int xEnd = xStart + rand.nextInt(50 - xStart);
        //     int yEnd = yStart + rand.nextInt(50 - yStart);
    
        //     DungeonManiaController dmc = new DungeonManiaController();
        //     DungeonResponse res3 = dmc.generateDungeon(xStart, xEnd, yStart, yEnd, "simple");

        //     Position start = new Position(xStart, yStart);

        //     assertEquals(start, getPlayer(res3).get().getPosition());

        //     // path find to exit
        //     // assert exit goal reached
        // }
    }    
    
    @Test
    @DisplayName("Test random randomly generated mazes are unique despite same user inputs")
    public void testUniqueMazes() {
        // generate maze
        // find/record path to exit pFirst
        // assert exit goal reached

        // generate multiple mazes
        // for each maze:
        //      find/record path to exit p
        //      assert exit goal reached
        //      assert p != pFirst
    }    


}

