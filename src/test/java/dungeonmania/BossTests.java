package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntities.Assassin;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class BossTests {

    @Test
    @DisplayName("Testing the assassin refusing the bribe")
    public void testBribedAssassin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribedassassin", "M3_guarunteedFailureAss");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        dmc.tick(Direction.RIGHT);

        Entity assassinEntity = dungeon.getMap().getAllOfType("assassin").get(0);
        Assassin assassin = (Assassin) assassinEntity;
        try {
            dmc.interact(assassin.getId());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidActionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(false, assassin.isBribed());

    }

    @Test
    @DisplayName("Testing the assassin refusing the bribe")
    public void testBribedAssassin2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribedassassin", "M3_guarunteedBribeonAss");
        Dungeon dungeon = dmc.getDungeon();
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        dmc.tick(Direction.RIGHT);

        Entity assassinEntity = dungeon.getMap().getAllOfType("assassin").get(0);
        Assassin assassin = (Assassin) assassinEntity;
        try {
            dmc.interact(assassin.getId());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidActionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(true, assassin.isBribed());

    }

    @Test
    @DisplayName("Testing the hydra moving")
    public void testBasicHydraMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribedassassin", "M3_guarunteedBribeonAss");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        EntityResponse initHydra = getEntities(initDungonRes, "hydra").get(0);

        // Move left and right a lot (there is a slight chance this test fails but highly unlikely)
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);

        for (int i = 0; i < 200; i++) {
            actualDungonRes = dmc.tick(Direction.LEFT);
        }

        EntityResponse hydra = getEntities(dmc.getDungeonResponseModel(), "hydra").get(0);
        assertFalse(hydra.getPosition().equals(initHydra.getPosition()));
    }

    @Test
    @DisplayName("Testing the hydra battles")
    public void testHydraBattles() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_testHydraBattle", "M3_guarunteedBribeonAss");

        assertTrue(res.getEntities().size() == 6);

        res = dmc.tick(Direction.STAY);


        assertTrue(res.getEntities().size() == 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(!res.getEntities().get(i).getType().equals("player"));
        }
    }

}
