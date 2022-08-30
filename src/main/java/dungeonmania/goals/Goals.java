package dungeonmania.goals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

/*
 * Written by John Henderson
 */

/*
 * This is the file that contains all the methods a user will need to interact with
 * the goals. This file should be used as a face for all goals.
 */

/*
 * Examples:
 * List<BasicGoal> goals = Goals.getGoalList(); // returns a list of all goals
 * Goals.setGoalStatus("treasure", true); // sets the status of the treasure goal to true
 * boolean treasure = Goals.getGoalStatus("treasure"); // returns the status of the treasure goal
 * boolean isGameFinished = Goals.isTrue(); // returns true if all goals are true based on 'AND's and 'OR's
 */

public class Goals {

    private final static List<String> COMPLEX_GOALS = new ArrayList<String>(Arrays.asList("AND", "OR"));
    private final static List<String> BASIC_GOALS = new ArrayList<String>(Arrays.asList("enemies", "boulders", "treasure", "exit"));
    
    private static GoalInterface goal;

    /**
     * @param goals - A JsonObject containing all the goals as specified in the spec file.
     * @return void. This is a recursive function that creates the goal tree, and the user
     * should not interact with the returned object.
     */
    public static void loadGoals(JsonObject goals) {
        goal = loadGoalsHelper(goals);
    }

    /**
     * @param goals - A JsonObject containing all the goals as specified in the spec file.
     * @return GoalInterface - The root of the goal tree.
     */
    private static GoalInterface loadGoalsHelper(JsonObject goals) {
        String goalType = goals.get("goal").getAsString();
        GoalInterface test;
        if (COMPLEX_GOALS.contains(goalType)) {
            test = new ComplexGoal(goalType);
            JsonArray subGoals = goals.get("subgoals").getAsJsonArray();
            for (JsonElement sub: subGoals) {
                test.add(loadGoalsHelper(sub.getAsJsonObject()));
            }
            return test;
        } else if (BASIC_GOALS.contains(goalType)) {
            return new BasicGoal(goalType);
        
        } else {
            throw new IllegalArgumentException("Invalid goal type: " + goalType);
        }
    }

    /**
     * @param goal - the name of the goal you would like to set the status of.
     * @param status - the status you would like to set the goal to.
     */
    public static void setGoalStatus(String goals, boolean status) {
        goal.setGoalStatus(goals, status);
    }

    /**
     * @return true if all goals are true based on 'AND's and 'OR's of complex goals
     */
    public static boolean isTrue() {
        return goal.isTrue();
    }

    /**
     * @return a string representation of the goal tree.
     */
    public static String getName() {
        return isTrue() ? "" : goal.getName();
    }

    /**
     * Goes over all goals, and sets their status based the current game.
     */
    public static void evaluate(Dungeon dungeon) {
        goal.evaluate(dungeon);
    }
}
