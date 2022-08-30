package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;



public class TestInteract {
    @Test
    @DisplayName("")
    public void testMovementDown() throws IllegalArgumentException, InvalidActionException {
        // DungeonManiaController dmc = new DungeonManiaController();
        // dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
        // dmc.interact("");
    }

    @Test
    @DisplayName("Interact with Mecenary too far away")
    public void testInteractWithMercenaryTooFarAway() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary", "c_battleTests_basicMercenaryMercenaryDies");
        dmc.getDungeon();
        
        dmc.tick(Direction.RIGHT);

        EntityResponse mercenary = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        
        String mercId = mercenary.getId();

        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(mercId);
        });

    }

    @Test
    @DisplayName("Interact with Mecenary too far away")
    public void testInteractWithMercenary() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary", "c_battleTests_basicMercenaryBribeRadius3");
        dmc.getDungeon();
        
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        EntityResponse mercenary = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        
        String mercId = mercenary.getId();

        assertDoesNotThrow(() -> {
            dmc.interact(mercId);
        });

    }

    @Test
    @DisplayName("Interact with zombie spawner too far away")
    public void testInteractWithZombieSpawnTooFar() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dun = dmc.newGame("d_zombiesTest copy", "c_zombiesTest copy");
        Dungeon dung = dmc.getDungeon();
        
        dun = dmc.tick(Direction.RIGHT);
        // dun = dmc.tick(Direction.RIGHT);

        EntityResponse zombieSpawn = getEntities(dmc.getDungeonResponseModel(), "zombie_toast_spawner").get(0);
        
        String spawnId = zombieSpawn.getId();

        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(spawnId);
        });

    }

    @Test
    @DisplayName("Interact with zombie spawner")
    public void testInteractWithZombieSpawn() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dun = dmc.newGame("d_zombiesTest copy", "c_zombiesTest copy");
        Dungeon dung = dmc.getDungeon();
        
        dun = dmc.tick(Direction.DOWN);
        dun = dmc.tick(Direction.RIGHT);

        EntityResponse zombieSpawn = getEntities(dmc.getDungeonResponseModel(), "zombie_toast_spawner").get(0);
        
        String spawnId = zombieSpawn.getId();

        assertDoesNotThrow(() -> {
            dmc.interact(spawnId);
        });

    }
}
