// package dungeonmania;

/*
 * Written by John Henderson
 */

/*
 * This class was meant to provide a way to time travel in the dungeon.
 * When the player time travels, this class would handle the movement 
 * and ticking, as well as overriding some of the methods of dungeon.
 */


// import java.util.ArrayList;
// import java.util.List;

// import dungeonmania.goals.Goals;
// import dungeonmania.response.models.AnimationQueue;
// import dungeonmania.response.models.BattleResponse;
// import dungeonmania.response.models.DungeonResponse;
// import dungeonmania.response.models.EntityResponse;
// import dungeonmania.response.models.ItemResponse;
// import dungeonmania.util.Direction;

// public class TimeTravelingDungeon extends Dungeon {

//     List<Dungeon> dungeons = new ArrayList<>();
//     List<Entity> entitiesKilled = new ArrayList<>();
//     Player oldPlayer;
//     Player currentPlayer;
//     private int currentDungeonIndex = 0;

//     public TimeTravelingDungeon(List<Dungeon> dungeons, Player player) {
//         super(dungeons.get(0).getId(), dungeons.get(0).getName());
//         this.dungeons = dungeons;
//         this.currentPlayer = new Player(player.getId(), player.getPosition());
//         updateDungeon(0);
//     }

//     public DungeonResponse tick(Direction direction) {
//         currentPlayer.movePlayer(this, direction);
//         updateDungeon(currentDungeonIndex);
//         currentDungeonIndex++;

//         // remove all killed entities from the map
//         for (Entity e : entitiesKilled) {
//             this.removeEntity(e.getId());
//         }

//         // preform battles

//         return null;
//     }    

//     public void updateDungeon(int index) {
//         this.map = dungeons.get(index).map;
//         this.oldPlayer = dungeons.get(index).getPlayer();
//         this.player = dungeons.get(index).getPlayer();
//         this.player.setType("old_player");
//         this.enemiesKilled = dungeons.get(index).enemiesKilled;
//         this.battles = dungeons.get(index).battles;
//         this.spiderClock = dungeons.get(index).spiderClock;
//         this.spiderRate = dungeons.get(index).spiderRate;
//     }

//     @Override
//     public DungeonResponse getDungeonResponse() {
//         List<EntityResponse> entities = new ArrayList<>();
//         map.values().stream().forEach(list -> 
//                                       list.stream().forEach(e -> entities.add(e.getEntityResponse())));
//         entities.add(currentPlayer.getEntityResponse());
//         List<ItemResponse> inventory = new ArrayList<>();
//         currentPlayer.getInventory().stream().forEach(item -> inventory.add(item.getItemResponse()));

//         // Get list of buildables as string
//         List<String> buildables = player.getInventoryController().getBuildables();

//         //  Get list of buildables as Item
//         // List<ItemResponse> buildables = new ArrayList<>();
//         // player.getInventory().stream().forEach(item -> build.add(item.getItemResponse()));

//         List<BattleResponse> battleResponses = new ArrayList<>();
//         battles.forEach(battle -> battleResponses.add(battle.getBattleResponse()));

//         List<AnimationQueue> animations = new ArrayList<>();
        
//         return new DungeonResponse(id, name, entities, inventory, battleResponses, buildables, Goals.getName(), animations);
//     }
// }