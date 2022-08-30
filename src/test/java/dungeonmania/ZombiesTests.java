package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class ZombiesTests {
    @Test
    @DisplayName("Dungeon spawns Zombies")
    public void testSpawnZombies() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombiesTest", "c_zombiesTest");


        // move player left x8
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(4, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("Can Battle Zombie")
    public void testBattleZombie() throws IllegalArgumentException, InvalidActionException {
    
    }

    @Test
    @DisplayName("Zombies can move through portal")
    public void testZombiePortal() throws IllegalArgumentException, InvalidActionException {
        
    }

    @Test
    @DisplayName("Dungeon spawns Zombies")
    public void testSpawnIsBlocked() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedTest", "c_zombiesTest");


        // move player left x8
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(0, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies")
    public void testSpawnNotBlockedLeft() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotLEFTTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies left has boulder")
    public void testSpawnNotBlockedLeftBoulder() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotLEFTBoulderTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies boulder Right")
    public void testSpawnNotBlockedRightBoulder() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotRIGHTBoulderTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies")
    public void testSpawnNotBlockedRight() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotRIGHTTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies blocked not down")
    public void testSpawnNotBlockedDown() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotDownTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies blocked not down")
    public void testSpawnNotBlockedDownBoulder() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotDownBoulderTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }
    @Test
    @DisplayName("Dungeon spawns Zombies blocked not Up")
    public void testSpawnNotBlockedUp() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotUPTest", "c_zombiesTest");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(1, getEntities(res, "zombie_toast").size());
        

    }

    @Test
    @DisplayName("Dungeon spawns Zombies")
    public void testSpawnRateZero() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieSpawnBlockedNotLEFTTest", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");


        // move player left x2
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
    


        // assert after movement
        assertEquals(0, getEntities(res, "zombie_toast").size());
        

    }
}
