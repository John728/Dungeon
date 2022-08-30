package dungeonmania.goals.goalEvaluation;

import dungeonmania.Dungeon;

/*
 * Written by John Henderson
 */

 /*
  * This interface is intended to allow the user to more easily create new Goals.
  * To create a new goal, implement this interface.
  */

public interface EvaluationInterface {
    public boolean evaluate(Dungeon dungeon);
    public String getName();
}
