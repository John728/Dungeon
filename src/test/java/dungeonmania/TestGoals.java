package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.BasicGoal;
import dungeonmania.goals.ComplexGoal;
import dungeonmania.goals.Goals;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestGoals {
    @Test
    @DisplayName("Check that treasure goal updates right")
    public void basicTreasureGoal() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(Goals.isTrue());
    }

    @Test
    @DisplayName("Check that treasure goal updates right with sunstone")
    public void basicTreasureGoalWithSunstone() throws IllegalArgumentException, InvalidActionException {
        // DungeonManiaController dmc = new DungeonManiaController();
        // dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
        // assert(!Goals.isTrue());
        // dmc.tick(Direction.RIGHT);
        // assert(!Goals.isTrue());
        // dmc.tick(Direction.RIGHT);
        // assert(!Goals.isTrue());
        // dmc.tick(Direction.RIGHT);
        // assert(Goals.isTrue());
    }

    @Test
    @DisplayName("Check that enemy goal updates right")
    public void basicEnemiesGoal() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
    }

    @Test
    @DisplayName("Check that boulder goal updates right")
    public void basicBoulderGoal() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicBoulderGoal", "c_goalsTest_basicTreasureGoal");
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(Goals.isTrue());
    }

    @Test
    @DisplayName("Check that exit goal updates right")
    public void basicExitGoal() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicExitGoal", "c_goalsTest_basicTreasureGoal");
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(!Goals.isTrue());
        dmc.tick(Direction.RIGHT);
        assert(Goals.isTrue());
    }

    @Test
    public void checkStringExit() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse goal = dmc.newGame("d_basicExitGoal", "c_goalsTest_basicTreasureGoal");
        assert(goal.getGoals().contains(":exit"));
    }

    @Test
    public void checkStringBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse goal = dmc.newGame("d_basicBoulderGoal", "c_goalsTest_basicTreasureGoal");
        assert(goal.getGoals().contains(":boulders"));
    }

    @Test
    public void checkStringEnemy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse goal = dmc.newGame("d_goalsTest_basicTreasureGoal", "c_goalsTest_basicTreasureGoal");
        assert(goal.getGoals().contains(":treasure"));
    }

    @Test
    public void checkStringTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse goal = dmc.newGame("d_enemy", "c_goalsTest_basicTreasureGoal");
        assert(goal.getGoals().contains(":enemies"));
    }

    @Test
    public void testComplexGoalEvaluation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoals", "c_goalsTest_basicTreasureGoal");
        String goal = res.getGoals();
        assert(goal.contains(":treasure"));
        assert(goal.contains(":exit"));
        assert(goal.contains(":enemies"));
        assert(goal.contains(":boulders"));

        Goals.setGoalStatus("exit", true);
        assertFalse(Goals.isTrue());

        goal = Goals.getName();
        assert(goal.contains(":treasure"));
        assertFalse(goal.contains(":exit"));
        assert(goal.contains(":enemies"));
        assert(goal.contains(":boulders"));

        Goals.setGoalStatus("enemies", true);
        assertFalse(Goals.isTrue());
        
        goal = Goals.getName();
        assert(goal.contains(":treasure"));
        assertFalse(goal.contains(":exit"));
        assertFalse(goal.contains(":enemies"));
        assert(goal.contains(":boulders"));

        Goals.setGoalStatus("treasure", true);
        assert(Goals.isTrue());

        goal = Goals.getName();
        assert(goal.isEmpty());
    }

    @Test
    public void addingBasicGoal() {
        BasicGoal goal = new BasicGoal("treasure");
        // assert thorws exception if you add to a basic goal
        assertThrows(UnsupportedOperationException.class, () -> {
            goal.add(new BasicGoal("exit"));
        });
    }

    @Test
    public void addingToComplexGoal() {
        assertDoesNotThrow(() -> {
            ComplexGoal goal = new ComplexGoal("AND");
            goal.add(new BasicGoal("treasure"));
            goal.add(new BasicGoal("exit"));
        });
    }
}