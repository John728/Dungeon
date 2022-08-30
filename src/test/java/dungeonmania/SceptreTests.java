package dungeonmania;



import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getValueFromConfigFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovementStrategies.BribedMercenaryMovement;
import dungeonmania.MovingEntities.MovementStrategies.DefaultMercenaryMovement;
import dungeonmania.exceptions.InvalidActionException;

public class SceptreTests {
    
    @Test
    @DisplayName("Test the player use the sceptre on any mercenary")
    public void testSceptreControlMercenary() throws IllegalArgumentException, InvalidActionException {
         // Tests the merc moving into the player
        // Depends on our implementation
        // White box test
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreMercenary", "M3_config");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse mercs = getEntities(res, "mercenary").get(0);

        EntityResponse merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        
        // EntityResponse initPlayer = getPlayer(initDungonRes).get();
        
        Mercenary mer = (Mercenary)dungeon.getMap().getEntity(merc.getId());
        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");

        assertEquals(1, getInventory(res, "sceptre").size());

        res = dmc.interact(merc.getId());
        
        res = dmc.tick(Direction.LEFT);
        
        assertTrue((mer.getMoveStrategy() instanceof BribedMercenaryMovement));

        assertTrue((mer.getPrevMoveStrategy() instanceof DefaultMercenaryMovement));

    }

    @Test
    @DisplayName("Test the player use the sceptre on any mercenary")
    public void testSceptreControlAssassin() throws IllegalArgumentException, InvalidActionException {

    }

    @Test
    @DisplayName("Test the player can make use sceptre and will wear off after certain number of ticks")
    public void testSceptreControlWearsOffMercenary() throws IllegalArgumentException, InvalidActionException {
                 // Tests the merc moving into the player
        // Depends on our implementation
        // White box test
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreMercenary", "M3_config");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse mercs = getEntities(res, "mercenary").get(0);

        EntityResponse merc = getEntities(dmc.getDungeonResponseModel(), "mercenary").get(0);
        
        // EntityResponse initPlayer = getPlayer(initDungonRes).get();
        
        Mercenary mer = (Mercenary)dungeon.getMap().getEntity(merc.getId());
        // move player right
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");

        assertEquals(1, getInventory(res, "sceptre").size());

        res = dmc.interact(merc.getId());
        
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        dungeon.getMap().getAllOfType(Player.class);
        res = dmc.tick(Direction.LEFT);
        
        assertTrue((mer.getMoveStrategy() instanceof DefaultMercenaryMovement));

        assertTrue((mer.getPrevMoveStrategy() == null));
    }

    @Test
    @DisplayName("Test the player can make use sceptre and will wear off after certain number of ticks")
    public void testSceptreControlWearsOffAssassin() throws IllegalArgumentException, InvalidActionException {

    }

}
