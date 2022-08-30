package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.battle.Battle;
import dungeonmania.goals.Goals;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Dungeon extends DungeonManiaController implements Serializable {
    private String id;
    private String name;
    private DungeonMap map;
    private Player player;
    private int enemiesKilled = 0;
    private List<Battle> battles = new ArrayList<>();
    private boolean gameOver;

    protected int spiderClock = 0;
    protected int spiderRate = Config.getSetting(Settings.spider_spawn_rate);

    public Dungeon(String id, String name) {
        this.map = new DungeonMap();
        this.gameOver = false;
        this.id = id;
        this.name = name;
    }

    public DungeonResponse getDungeonResponse() {
        List<EntityResponse> entities = new ArrayList<>();
        entities = map.getMapEntityResponse();

        // Get inventory and convert to item responses
        List<ItemResponse> inventory = new ArrayList<>();
        player.getInventory().stream().forEach(item -> inventory.add(item.getItemResponse()));

        // Get list of buildables as string
        List<String> buildables = player.getInventoryController().getBuildables(map);

        // Following haven't been implemented
        List<BattleResponse> battleResponses = new ArrayList<>();
        battles.forEach(battle -> battleResponses.add(battle.getBattleResponse()));

        List<AnimationQueue> animations = new ArrayList<>();
        
        return new DungeonResponse(id, name, entities, inventory, battleResponses, buildables, Goals.getName(), animations);
    }

    // /**
    //  * Moves Entity e from current position to new x,y coordinates.
    //  * @param e
    //  * @param x
    //  * @param y
    //  */
    // public void moveEntity(Entity e, int x, int y) {
    //     Position oldPos = e.getPosition();
    //     moveEntity(e.getId(), oldPos, new Position(x, y, oldPos.getLayer()));
    // }

    /**
     * @param pos
     * @return all Entities at the given position as a list.
     */
    public void spawnEnemies() {
        List<ZombieToastSpawner> zombieToastSpawners = map.getZombieSpawnerList();

        for (ZombieToastSpawner zombieToastSpawner: zombieToastSpawners) {
            zombieToastSpawner.spawnZombiesTick(getMap());
        }

        spawnSpider(getMap());
        
    }

    public void useAnItem(String itemUsedId) {
        //Item item = (Item)getEntity(itemUsedId);
        Item item = player.getInventoryController().getItemInInventoryFromId(itemUsedId);
        player.getInventoryController().useItem(getMap(), item);
    }
    

    public String getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // public Goal getGoal() {
    //     return goal;
    // }

    public String getName() {
        return name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addBattle(Battle battle) {
        battles.add(battle);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public DungeonMap getMap() {
        return map;
    }

    public void setMap(DungeonMap map) {
        this.map = map;
    }


    // public <T> List<T> getAllOfType(Class<T> type) {
    //     List<T> entities = new ArrayList<>();
    //     map.values().stream().forEach(list -> list.stream().filter(e -> e.getType().equals(type)).forEach(e -> entities.add(e)));
    //     return entities;
    // }


    public Position getPrevPlayerPos() {
        return player.getLastPos();
    }

    public Position getPlayerPos() {
        return player.getPosition();
    }

    public void spawnSpider(DungeonMap dungeonMap) {
        
        spiderClock++;

        if (spiderClock == spiderRate) {
            spiderClock = 0;
            Random rand = new Random();
            Position upperBound = getMap().getUpperBounds();
            Position lowerBound = getMap().getLowerBounds();
            int xUpper = upperBound.getX();
            int yUpper = upperBound.getY();
            int xLower = lowerBound.getX();
            int yLower = lowerBound.getY();

            int x = rand.nextInt(xUpper - xLower);
            int y = rand.nextInt(yUpper - yLower);

            x += xLower;
            y += yLower;

            Position spawnPos = new Position(x,y);
            Spider spider = new Spider(DungeonManiaController.getNewId(dungeonMap, Spider.class), spawnPos);
            map.addEntity(spider);
        }
    }

    /**
     * 
     */
    public void updateMindcontrolledEntities() {
        List<Mercenary> mercenaries = new ArrayList<>();
        
        List<Entity> entities = map.getEntities();

        for (Entity entity: entities) {
            if (entity instanceof Mercenary) {
                mercenaries.add((Mercenary)entity);
            }
        }

        for (Mercenary mercenary: mercenaries) { 
            mercenary.decreaseMindControllTimer();
        }
    }
    
    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void increaseEnemiesKilled() {
        enemiesKilled++;
    }
}
