package dungeonmania.goals;

import java.util.ArrayList;

import dungeonmania.Dungeon;

/*
 * Written by John Henderson
 */

/*
 * Complex goals stores a list of basic or other complex goals. A complex goal
 * Has a 'combo' which is the type of operation that is performed on the goals,
 * and is either 'AND' or 'OR'.
 */

public class ComplexGoal implements GoalInterface {

    ArrayList<GoalInterface> children = new ArrayList<GoalInterface>();
    String combo;

    public ComplexGoal(String possibleCombo) {
        this.combo = possibleCombo;
    }

    /**
     * Adds a goal to the list of children.
     * @param child the goal to add.
     */
    @Override
	public void add(GoalInterface child) {
		children.add(child);
	}

    /**
     * @return the name of the goal. This function goes over all
     * and created a string of all names, seporated logically.
     */
    @Override
    public String getName() {
        String name = "(";
        int i = 0;
        int end = children.size() - 1;
        for (GoalInterface child : children) {
            if (i == 0) {
                name += child.getName() + " ";
            } else if (i == end) {
                name += combo + " " + child.getName();
            } else {
                name += combo + " " + child.getName() + " ";
            }
            i++;
        }
        return name + ")";
    }

    /**
     * @return the status of the goal. This function goes over all
     * and checks if all children are based on the logical condition
     * of the complex goal.
     */
    @Override
    public boolean isTrue() {

        // And empty list is true by default.
        if (children.size() == 0) {
            return true;
        }

        // get the satus of the first child.
        boolean isTrue = children.get(0).isTrue();
        // loop over all the rest, applying the relevant logical operation.
        for (GoalInterface child : children) {
            if (combo.equals("AND")) {
                isTrue &= child.isTrue();
            } else if (combo.equals("OR")) {
                isTrue |= child.isTrue();
            }
        }
        return isTrue;
    }

    /**
     * @param goal - the goal you would like to set the status of
     * @param status - the status you would like to set the goal to
     * @implNote This should not be called by the user, but rather by the Goal class.
     */
    public void setGoalStatus(String goal, boolean status) {
        for (GoalInterface child : children) {
            child.setGoalStatus(goal, status);
        }
    }

    public void evaluate(Dungeon dungeon) {
        for (GoalInterface child : children) {
            child.evaluate(dungeon);
        }
    }
}