package dungeonmania.goals;

import dungeonmania.Dungeon;
import dungeonmania.goals.goalEvaluation.Boulders;
import dungeonmania.goals.goalEvaluation.Enemies;
import dungeonmania.goals.goalEvaluation.EvaluationInterface;
import dungeonmania.goals.goalEvaluation.Exit;
import dungeonmania.goals.goalEvaluation.Treasure;

/*
 * Written by John Henderson
 */

/* 
 * Basic goals stores any basic goal. This goal has a name, and a status that
 * can be set, and retrieved when needed. The user should not interact with this class,
 * but instead the Goal class, which will act as a face for all goals. Goals are set
 * to true by default.
 */

/*
 * Possible goals:
    * boalders
    * treasure
    * exit
    * enemies
 */

public class BasicGoal implements GoalInterface {

    // private String goal;
    private EvaluationInterface goal;
    private boolean isComplete = false;

    public BasicGoal(String goal) {
        switch (goal) {
            case "boulders":
                this.goal = new Boulders();
                break;
            case "treasure":
                this.goal = new Treasure();
                break;
            case "exit":
                this.goal = new Exit();
                break;
            case "enemies":
                this.goal = new Enemies(); // enemies goal needs to know how many enemies there are at the start
                break;
            default:
                throw new IllegalArgumentException("Invalid goal type: " + goal);
        }
    }

    /**
     * This is not defined for basic goals, and should never be called
     * unless there is an issue with the goal config.
     */
    @Override
    public void add(GoalInterface component) {
        // could change leaf to node, and then add, but this should not 
        // really happen
        throw new UnsupportedOperationException("Cannot add to leaf");
    }

    /**
     * @return the name of the goal.
     */
    @Override
    public String getName() {
        return isTrue() ? goal.getName() : ":" + goal.getName();
        // return goal.getName();
    }

    /**
     * @return the status of the goal.
     */
    @Override
    public boolean isTrue() {
        return isComplete;
    }

    /**
     * @param status - the status you would like to set the goal to
     */
    public void setStatus(boolean status) {
        this.isComplete = status;
    }

    /**
     * @param goal - the goal you would like to set the status of
     * @param status - the status you would like to set the goal to
     * @implNote This should not be called by the user, but rather by the Goal class.
     */
    @Override
    public void setGoalStatus(String goal, boolean status) {
        if (this.goal.getName().equals(goal)) {
            this.isComplete = status;
        }
    }

    public void evaluate(Dungeon dungeon) {
        this.setStatus(goal.evaluate(dungeon));
    }
}
