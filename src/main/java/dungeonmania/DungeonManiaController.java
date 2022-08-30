package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goals;
import dungeonmania.response.models.*;
import dungeonmania.util.*;
import dungeonmania.Items.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.MovingEntities.MovementStrategies.BribedMercenaryMovement;
import dungeonmania.StaticEntities.*;
import dungeonmania.battle.Battle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.Items.Arrows;
import dungeonmania.Items.Bomb;
import dungeonmania.Items.InvincibilityPotion;
import dungeonmania.Items.InvisibilityPotion;
import dungeonmania.Items.Key;
import dungeonmania.Items.SunStone;
import dungeonmania.Items.Sword;
import dungeonmania.Items.Treasure;
import dungeonmania.Items.Weapon;
import dungeonmania.Items.Wood;
import dungeonmania.MovingEntities.Assassin;
import dungeonmania.MovingEntities.Enemy;
import dungeonmania.MovingEntities.Hydra;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.MovingEntities.MovementStrategies.BribedMercenaryMovement;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.battle.Battle;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goals;
import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;


public class DungeonManiaController {
    private static final boolean WALL = false;
    private static final boolean EMPTY = true;

    private Dungeon dungeon;
    private Persistance persistance = new Persistance();

    public Dungeon getDungeon() {
        return dungeon;
    }

    private void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public Player getPlayer() {
        return dungeon.getPlayer();
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        System.setOut(System.out); // for debugging

        // will load the settings into the config object (handles exceptions separately)
        Config.loadConfig(configName);

        // Get json from files
        JsonObject json = null;
        try {
            json = JsonParser.parseString(FileLoader.loadResourceFile("dungeons/" + dungeonName + ".json")).getAsJsonObject();
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("This dungeon's json file is either malformed or non-existent.");
        }

        // Create Dungeon
        Dungeon dungeon = new Dungeon(dungeonName + "_id", dungeonName);
        DungeonMap dungeonMap = new DungeonMap();

        // load entities
		JsonArray jsonEntities = json.getAsJsonArray("entities");
		for (int i = 0; i < jsonEntities.size(); i++) {
			loadEntity(dungeon, dungeonMap, jsonEntities.get(i).getAsJsonObject());
		}

		// load goals and set dungeon goals
        JsonElement goalConditions = json.get("goal-condition");
        Goals.loadGoals(goalConditions.getAsJsonObject());

        dungeon.setMap(dungeonMap);
        setDungeon(dungeon);


        return dungeon.getDungeonResponse();
    }

    /**
     * /game/new/generate
     * @throws Exception
     * 
     * @pre-conditions Assumes configName is valid and that xStart < xEnd and yStart < yEnd
     */
    public DungeonResponse generateDungeon(int xStart, int xEnd, int yStart, int yEnd, String configName) {

        boolean[][] maze = randomPrimsMaze(Math.abs(xEnd - xStart) + 1, Math.abs(yEnd - yStart) + 1);
    
        return createDungeonAsJson(xStart, yStart, maze, configName);
    }

    private boolean[][] randomPrimsMaze(int x, int y) {
        // ** Randomised Prim's Algorithm **
        // Start must be in top-left corner, end in bottom-right

        // let maze be a 2D array of booleans (of size width and height) default false
        // false representing a wall and true representing empty space
        boolean[][] maze = new boolean[x][y];

        // maze[start] = empty
        maze[0][0] = EMPTY;

        // let options be a list of positions
        List<Position> options = new ArrayList<>();

        // let explored be list of explored options
        List<Position> explored = new ArrayList<>();

        // add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
        if (maze[0].length > 2) options.add(new Position(0, 2));
        if (maze.length > 2) options.add(new Position(2, 0));

        // while options is not empty:
        while (options.size() > 0) {
            // let next = remove random from options
            Random rand = new Random(System.currentTimeMillis());
            Position next = options.remove(rand.nextInt(options.size()));

            // if not yet explored
            if (!explored.stream().anyMatch(p -> p.equals(next))) {
                // let emptyNeighbours = each neighbour of distance 2 from next not on boundary that are empty
                List<Position> emptyNeighbours = new ArrayList<>();
                List<Position> wallNeighbours = new ArrayList<>();
                addToCorrespondingNeighbourList(wallNeighbours, emptyNeighbours, maze, new Position(next.getX() + 2, next.getY()));
                addToCorrespondingNeighbourList(wallNeighbours, emptyNeighbours, maze, new Position(next.getX() - 2, next.getY()));
                addToCorrespondingNeighbourList(wallNeighbours, emptyNeighbours, maze, new Position(next.getX(), next.getY() + 2));
                addToCorrespondingNeighbourList(wallNeighbours, emptyNeighbours, maze, new Position(next.getX(), next.getY() - 2));


                // if neighbours is not empty:
                if (emptyNeighbours.size() > 0) {
                    // let neighbour = random from neighbours
                    Position neighbour = emptyNeighbours.get(rand.nextInt(emptyNeighbours.size()));

                    // maze[ next ] = empty (i.e. true)
                    maze[next.getX()][next.getY()] = EMPTY;

                    // maze[ position inbetween next and neighbour ] = empty (i.e. true)
                    maze[(next.getX() + neighbour.getX())/2][(next.getY() + neighbour.getY())/2] = EMPTY;

                    // maze[ neighbour ] = empty (i.e. true)
                    maze[neighbour.getX()][neighbour.getY()] = EMPTY;
                }
                // add to options all neighbours of 'next' not on boundary that are of distance 2 away and are walls
                wallNeighbours.stream().forEach(n -> options.add(n));

                // mark next option as explored
                explored.add(next);
            }
        }


        // if maze[end] is a wall:
        if (maze[x - 1][y - 1] == WALL) {
            // maze[end] = empty
            maze[x - 1][y - 1] = EMPTY;
        }

        // let neighbours = neighbours not on boundary of distance 1 from maze[end]
        List<Position> neighbours = new ArrayList<>();
        neighbours.add(new Position(x - 2, y - 1));
        neighbours.add(new Position(x - 1, y - 2));

        // if there are no cells in neighbours that are empty:
        if (neighbours.stream().noneMatch(n -> maze[n.getX()][n.getY()] == EMPTY)) {
            // let's connect it to the grid
            // let neighbour = random from neighbours
            Random rand = new Random();
            Position neighbour = neighbours.get(rand.nextInt(neighbours.size()));

            // maze[neighbour] = empty
            maze[neighbour.getX()][neighbour.getY()] = EMPTY;
        }
        return maze;
    }

    private DungeonResponse createDungeonAsJson(int xStart, int yStart, boolean[][] maze, String configName) {
        DungeonResponse res = null;
        try {
            String dungeonName = "dungeonBuilder" + String.valueOf(System.currentTimeMillis());
            File f = new File( "src/main/resources/dungeons/" + dungeonName + ".json");
            f.createNewFile();
            FileWriter w = new FileWriter(f);

            // Entities
            w.write("{\n\t\"entities\": [\n");

            // player at start
            w.append(formattedJson(xStart, yStart, "player", true));

            // add maze walls
            for (int y = 0; y < maze[0].length; y++) {
                for (int x = 0; x < maze.length; x++) {
                    if (maze[x][y] == WALL) {
                        w.append(formattedJson(xStart + x, yStart + y, "wall", true));
                    }
                }
            }

            // add surrounding walls
            for (int x = 0; x < maze.length; x++) {
                w.append(formattedJson(xStart + x, yStart - 1, "wall", true));
                w.append(formattedJson(xStart + x, yStart + maze[0].length, "wall", true));
            }
            for (int y = -1; y < maze[0].length + 1; y++) {
                w.append(formattedJson(xStart - 1, yStart + y, "wall", true));
                w.append(formattedJson(xStart + maze.length, yStart + y, "wall", true));
            }

            // exit at end
            w.append(formattedJson(xStart + maze.length - 1, yStart + maze[0].length - 1, "exit", false));

            // exit goal
            w.append("\t],\n\t\"goal-condition\": {\n\t\t\"goal\": \"exit\"\n\t}\n}");

            w.flush();
            w.close();

            res = newGame(dungeonName, configName);

            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void addToCorrespondingNeighbourList(List<Position> walls, List<Position> paths, boolean[][] maze, Position pos) {
        try {
            if (maze[pos.getX()][pos.getY()] == EMPTY) {
                paths.add(pos);
            } else if (maze[pos.getX()][pos.getY()] == WALL) { // else
                walls.add(pos);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // on/outside boundary -> do nothing
        }
    }

    private String formattedJson(int x, int y, String type, Boolean trailingComma) {
        String ret = "\t\t{\n" +
                     "\t\t\t\"x\": " + String.valueOf(x) + 
                     ",\n\t\t\t\"y\": " + String.valueOf(y) + 
                     ",\n\t\t\t\"type\": " + "\"" + type + "\"" +
                     "\n\t\t}";
        if (trailingComma) ret += ",";
        ret += "\n";
        return ret;
    }



    private void loadEntity(Dungeon dungeon, DungeonMap dungeonMap, JsonObject json) {
		String type = json.get("type").getAsString();
		int x = json.get("x").getAsInt();
		int y = json.get("y").getAsInt();
        Position pos = new Position(x, y);

        Entity e = null;
		switch (type) {
            case "player":
                Player player = new Player(getNewId(dungeonMap, Player.class), pos); // load relevant info to backend
                dungeon.setPlayer(player);
                // Player needs to be added to dungeon so we can move it around the map/grid
                dungeonMap.addEntity(player); 
                return;
            case "wall":
                e = new Wall(getNewId(dungeonMap, Wall.class), pos); break;
            case "exit":
                e = new Exit(getNewId(dungeonMap, Exit.class), pos); break;
            case "boulder":
                e = new Boulder(getNewId(dungeonMap, Boulder.class), pos); break;
            case "switch":
                e = new FloorSwitch(getNewId(dungeonMap, FloorSwitch.class), pos); break;
            case "door":
                int doorKey = json.get("key").getAsInt();
                e = new Door(getNewId(dungeonMap, Door.class), pos, doorKey); break;
            case "portal":
                String portalColour = json.get("colour").getAsString();
                e = new Portal(getNewId(dungeonMap, Portal.class), pos, portalColour); break;
            case "swamp_tile":
                int movement_factor = json.get("movement_factor").getAsInt();
                e = new SwampTile(getNewId(dungeonMap, SwampTile.class), pos, movement_factor); break;
            case "zombie_toast_spawner":
                e = new ZombieToastSpawner(getNewId(dungeonMap, ZombieToastSpawner.class), pos); break;
            case "spider":
                e = new Spider(getNewId(dungeonMap, Spider.class), pos); break;
            case "zombie_toast":
                e = new ZombieToast(getNewId(dungeonMap, ZombieToast.class), pos); break;
            case "mercenary":
                e = new Mercenary(getNewId(dungeonMap, Mercenary.class), pos, Config.getSetting(Settings.bribe_amount)); break;
            case "treasure":
                e = new Treasure(getNewId(dungeonMap, Treasure.class), pos); break;
            case "key":
                int key = json.get("key").getAsInt();
                e = new Key(getNewId(dungeonMap, Key.class), pos, key); break;
            case "invincibility_potion":
                e = new InvincibilityPotion(getNewId(dungeonMap, InvincibilityPotion.class), pos); break;
            case "invisibility_potion":
                e = new InvisibilityPotion(getNewId(dungeonMap, InvisibilityPotion.class), pos); break;
            case "wood":
                e = new Wood(getNewId(dungeonMap, Wood.class), pos); break;
            case "arrow":
                e = new Arrows(getNewId(dungeonMap, Arrows.class), pos); break;
            case "bomb":
                e = new Bomb(getNewId(dungeonMap, Bomb.class), pos); break;
            case "sword":
                e = new Sword(getNewId(dungeonMap, Sword.class), pos); break;
            // case "time_travelling_portal":
            //     e = new TimeTravelingPortal(getNewId(dungeonMap, TimeTravelingPortal.class), pos); break;
            case "sun_stone":
                e = new SunStone(getNewId(dungeonMap, SunStone.class), pos); break;
            // case "sceptre":
            //     e = new SunStone(getNewId(dungeonMap, Sceptre.class), pos); break;
            // case "midnight_armour":
            //     e = new SunStone(getNewId(dungeon, MidnightArmour.class), pos); break;
            case "hydra":
                e = new Hydra(getNewId(dungeonMap, Hydra.class), pos); break;
            case "assassin":
            e = new Assassin(getNewId(dungeonMap, Assassin.class), pos); break;
        }
        if (e != null) {
            dungeonMap.addEntity(e);
        }
    }

    /**
     * Example usage: getNewId(dungeon, Spider.class).
     * @param <T> Any entity
     * @param dungeon
     * @param clas
     * @return String in form {@code <clas><number>}. e.g. "Spider3"
     * @throws InterruptedException
     */
    public static <T> String getNewId(DungeonMap dungeonMap, Class<T> clas) {
        int count = 0;
        for (Entity e : dungeonMap.getEntities()) {
            if (clas.isInstance(e)) count++;
        }
        String name = clas.getName();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e1) {
            // do nothing
        }

        return name.substring(name.lastIndexOf(".") + 1) + String.valueOf(count) + 
               "_" + String.valueOf(System.currentTimeMillis());
    }


    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return dungeon.getDungeonResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        
        if (dungeon.getPlayer().getInventoryController().getItemInInventoryFromId(itemUsedId) != null) {
            dungeon.useAnItem(itemUsedId);
            return tick(Direction.STAY);
        }
        else {
            return dungeon.getDungeonResponse();
        }
        
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        
        Player player = dungeon.getPlayer();
        DungeonMap dungeonMap = dungeon.getMap();
        player.movePlayer(dungeonMap, movementDirection);

        // If the player is currently time traveling, we need to do some extra stuff
        // if (timeTraveling) {
        //     timeTravelingTick();
        //     return dungeon.getDungeonResponse();
        // }

        // Move all other entities
        MovementHandler.moveMovingEntities(dungeon);

        /* get position of the player and positions needed to check movement */
        Position newPos = new Position(player.getPosition());
        
        // get items at current player position
        player.getInventoryController().addItemstoInventory(dungeonMap.getEntitiesAt(newPos), dungeonMap);
        
        // preform battles
        if (!player.getInvisible()) {
            Iterator<Entity> iter = dungeonMap.getEntitiesAt(newPos).stream().filter(entity -> entity instanceof Enemy).iterator();
            while (iter.hasNext()) {
                dungeon.addBattle(new Battle(dungeon, (Enemy) iter.next()));
            }
        }

        // checks goals conditions and updates accordingly if they are met
        Goals.evaluate(dungeon);

        // Check and update the active potion
        player.checkActivePotions(dungeonMap);

        //spawn zombies if spawner exists
        dungeon.spawnEnemies();

        // update mindcontrolled mercenaries' timers
        dungeon.updateMindcontrolledEntities();

        // do we need to time travel
        // if (dungeonMap.getEntitiesAt(player.getPosition()).stream().anyMatch(entity -> entity instanceof TimeTravelingPortal)) {
        //     return rewind(30);
        // }

        return dungeon.getDungeonResponse();
    }


    private void timeTravelingTick() {
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        
        Player player = dungeon.getPlayer();
        DungeonMap dungeonMap = dungeon.getMap();

        InventoryController inventoryController = dungeon.getPlayer().getInventoryController();


        
            if (buildable.equals("bow")) {
                if (inventoryController.checkCanBuildBow()) {
                    // System.out.println("ooga booga baby");
                    inventoryController.buildBow(dungeonMap);
                }
                else {
                    throw new InvalidActionException(buildable);
                }
            }
            else if (buildable.equals("shield")) {
                if (inventoryController.checkCanBuildShield()) {
                    inventoryController.buildShield(dungeonMap);
                }
                else {
                    throw new InvalidActionException(buildable);
                }
            }    
            else if (buildable.equals("sceptre")) {
                if (inventoryController.checkCanBuildSceptre()) {
                    inventoryController.buildSceptre(dungeonMap);
                }
                else {
                    throw new InvalidActionException(buildable);
                }
            }
            else if (buildable.equals("midnight_armour")) {
                if (inventoryController.checkCanBuildMidnightArmour(dungeon.getMap())) {
                    inventoryController.buildMidnightArmour(dungeonMap);
                }
                else {
                    throw new InvalidActionException(buildable);
                }
            }
            else {
                throw new IllegalArgumentException();
            }

        return dungeon.getDungeonResponse();
        
    }

    /**
     * /game/interact
     * @param entityId The id of the entity to interact with
     * @return DungeonResponse with the result of the interaction
     * @throws IllegalArgumentException if the entityId is not valid
     * @author John Henderson
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        
        Entity entity = dungeon.getMap().getEntity(entityId);
        if (entity == null) {throw new IllegalArgumentException("Entity of id " + entityId + " does not exist");}

        // choose correct method
        if (entity.getType() == "mercenary" || entity.getType() == "assassin") {
            return interactWithGenericMercenary((Mercenary) entity);
        }
        if (entity.getType() == "zombie_toast_spawner") {
            return interactWithZombieSpawner((ZombieToastSpawner) entity);
        }

        throw new InvalidActionException("Entity of id " + entityId + " is not a mercenary or zombie_toast_spawner");

    }

    /**
     * @param entity The mercenary to interact with
     * @return A DungeonResponse object containing the result of the interaction
     * @throws InvalidActionException
     * @pre Entity is a mercenary, and is valid
     * @author John Henderson
     */
    private DungeonResponse interactWithGenericMercenary(Mercenary entity) throws InvalidActionException {
        
        InventoryController inventoryController = dungeon.getPlayer().getInventoryController();

        if (inventoryController.getNumOfItem("sceptre") > 0) {
            return useSceptre(inventoryController, entity);
        } 
        
        // get Position and distance
        double distance = MovementHandler.getEuclidianDistance(dungeon.getPlayerPos(), entity.getPosition());
        int intDistance = (int) Math.round(distance);
        // The player is too far away to interact with the mercenary
        if (intDistance > Config.getSetting(Settings.bribe_radius)) {
            throw new InvalidActionException("Mercenary or Assassin is too far away");
        }

        // The plater does not have enough gold to bribe the mercenary
        if (inventoryController.getNumOfTreasure() < entity.getBribeAmount()) {
            throw new InvalidActionException("Not enough treasure to bribe");
        }

        // mercenary can be bribed
        if (entity instanceof Assassin) {
            Random rand = new Random();
            double chance = Config.getSettingAsDouble(Settings.assassin_bribe_fail_rate);
            if (rand.nextDouble() <= chance) {
                return getDungeonResponseModel();
            }
            entity.setBribed(true);
            entity.setMoveStrategy(new BribedMercenaryMovement());
            dungeon.getPlayer().addAlly(entity);
        } else {
            entity.setBribed(true);
            entity.setMoveStrategy(new BribedMercenaryMovement());
            dungeon.getPlayer().addAlly(entity);
        }

        if (entity.isBribed()) {
            for (int i = 0; i < entity.getBribeAmount(); i++) {
                inventoryController.removeItemFromInventory(inventoryController.getItemInInventory("treasure"));
            }
        }

        // return a dungeon response 
        return getDungeonResponseModel();
    }

    private DungeonResponse useSceptre(InventoryController inventoryController, Mercenary entity) {
        inventoryController.removeItemFromInventory(inventoryController.getItemInInventory("sceptre"));
        entity.setBribed(true);
        entity.setPrevMoveStrategy(entity.getMoveStrategy());
        entity.setMoveStrategy(new BribedMercenaryMovement());
        entity.setMindControlTimer(Config.getSetting(Settings.mind_control_duration));
        dungeon.getPlayer().addAlly(entity);

        return getDungeonResponseModel();
    }

    /**
     * @param entity The zombie_toast_spawner to interact with
     * @return A DungeonResponse object containing the result of the interaction
     * @throws InvalidActionException
     * @pre Entity is a zombie_toast_spawner, and is valid
     * @author John Henderson
     */
    private DungeonResponse interactWithZombieSpawner(ZombieToastSpawner entity) throws InvalidActionException {
        // get Position and distance
        Position posBetween = Position.calculatePositionBetween(dungeon.getPlayer().getPosition(), entity.getPosition());
        double distance = Math.sqrt(Math.pow(posBetween.getX(), 2) + Math.pow(posBetween.getY(), 2));
        InventoryController inventoryController = dungeon.getPlayer().getInventoryController();

        // Check that the player is close enough to the spawner 
        if (distance != 1.0) {
            throw new InvalidActionException("ZombieToastSpawner is too far away");
        }

        // Check that the player has a weapon
        if (inventoryController.getInventory().stream().noneMatch(item -> item instanceof Weapon)) {
            throw new InvalidActionException("Player does not have a weapon");
        }

        // Player has met all conditions and can now deploy the zombie toast spawner and remove weapon

        dungeon.getMap().removeEntity(entity.getId());
        Item weapon = inventoryController.getInventory().stream().filter(item -> item instanceof Weapon).findFirst().orElse(null); // should never be null
        inventoryController.removeItemFromInventory(weapon);

        // return a dungeon response
        return getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        persistance.saveGame(dungeon, name);
        return getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        dungeon = persistance.loadGame(name);
        return getDungeonResponseModel();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return persistance.getSavedGames();
    }

    // public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
    //     dungeon = new TimeTravelingDungeon(persistance.getDungeonRange(0, ticks), dungeon.getPlayer());
    //     timeTraveling = true; :(
    //     return dungeon.getDungeonResponse();
    // }
}
