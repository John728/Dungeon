package dungeonmania.goals;
import dungeonmania.Dungeon;

/*
 * Written by John Henderson
 */

 /*
  * This is the interface for all basic goals and complex goals. The structure of
  * a goal is tree-like, with basic goals as leaves and complex goals as nodes.
  * The end user does not need to worry about this, the below function is all that
  * needs to be called.
  * 
  * Pattern: Composite 
  */

public interface GoalInterface {
    public void add(GoalInterface component);
    public String getName();
    public boolean isTrue();
    public void setGoalStatus(String goal, boolean status);
    public void evaluate(Dungeon dungeon);
 }