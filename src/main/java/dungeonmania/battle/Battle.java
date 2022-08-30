package dungeonmania.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Player;
import dungeonmania.MovingEntities.Enemy;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;

/*
 * Written by John Henderson
 */

/*
 * This class handles battles and everything that entails. The current way that works is that
 * when the player and an enemy are on the same tile, a battle will start. This class will
 * handle item durability dropping, as well as the many factors that affect the battle. It returns
 * a single battle object which represents the battle.
 */

/*
* Example:
* Battle b = new battle(player, someEnemy); // will battle the player and enemy
*/

public class Battle implements Serializable {

    private String enemy;
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private List<Round> rounds = new ArrayList<>();

    /**
     * @param dungeon the current dungeon
     * @param enemy the enemy that is fighting the player
     * @return a singleBttle object which consists of the battle
     */
    public Battle(Dungeon dungeon, Enemy enemy) {
        this.enemy = enemy.getType();
        Player player = dungeon.getPlayer();
        DungeonMap dungeonMap = dungeon.getMap();
        
        // get info for singleBattle
        this.initialPlayerHealth = player.getHealth();
        this.initialEnemyHealth = enemy.getHealth();
        
        // rounds until someone dies
        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            this.rounds.add(new Round(player, enemy));
        }

        if (enemy.getHealth() <= 0) {
            dungeonMap.removeEntity(enemy.getId());
        }

        if (player.getHealth() <= 0) {
            dungeonMap.removeEntity(player.getId());
            dungeon.setGameOver(true);
        }

        dungeon.increaseEnemiesKilled();
    }
    

    public BattleResponse getBattleResponse() {
        List<RoundResponse> roundResponses = new ArrayList<>();
        rounds.stream().forEach(round -> roundResponses.add(round.getRoundResponse()));
        return new BattleResponse(enemy, roundResponses, initialPlayerHealth, initialEnemyHealth);
    }
}
