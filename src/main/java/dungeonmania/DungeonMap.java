package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonMap implements Serializable{
    
    private Map<Position, List<Entity>> map;

    public DungeonMap() {
        this.map = new HashMap<>();
    }

    public void addEntity(Entity e) {
        Position pos = e.getPosition();
        if (map.containsKey(pos)) { // NOTE: assumes pos(x1, y1, z1).equals(pos(x1, y1, z2)) should == true
            map.get(pos).add(e);
        } else {
            ArrayList<Entity> entities = new ArrayList<>();
            entities.add(e);
            map.put(pos, entities);
        }
    }

    public Entity removeEntity(String id) {
        for (List<Entity> entities : map.values()) {
            if (entities.stream().anyMatch(e -> e.getId().equals(id))) {
                return getEntityFromList(id, entities, true);
            }
        }
        return null;
    }

    public Entity removeEntity(String id, Position pos) {
        return getEntityFromList(id, getEntitiesAt(pos), true);
    }

    /**
     * Moves an Entity from one position to another in the Dungeon map.
     * @param id
     * @param oldPos
     * @param newPos
     */
    public void moveEntity(String id, Position oldPos, Position newPos) {
        Entity e = removeEntity(id, oldPos);
        e.setPosition(newPos);
        addEntity(e);
    }

    public Position getUpperBounds() {
        List<Position> allPos = new ArrayList<>();
        map.forEach((k,v) -> {
            allPos.add(k);
        });
        int xLargest = allPos.get(0).getX();
        int yLargest = allPos.get(0).getY();
        for (Position position : allPos) {
            if (position.getX() > xLargest) xLargest = position.getX();
            if (position.getY() > yLargest) yLargest = position.getY();
        }
    
        return new Position(xLargest, yLargest);
    }

    public Position getLowerBounds() {
        List<Position> allPos = new ArrayList<>();
        map.forEach((k,v) -> {
            allPos.add(k);
        });
        int xSmallest = allPos.get(0).getX();
        int ySmallest = allPos.get(0).getY();
        for (Position position : allPos) {
            if (position.getX() < xSmallest) xSmallest = position.getX();
            if (position.getY() < ySmallest) ySmallest = position.getY();
        }
    
        return new Position(xSmallest, ySmallest);
    }

    /**
     * @param pos
     * @return all Entities at the given position as a list.
     */
    public List<Entity> getEntitiesAt(Position pos) {
        return map.get(pos);
    }

    /**
     * @return all Entities in dungeon as a list.
     */
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        map.values().stream().forEach(list -> 
                                      list.stream().forEach(e -> entities.add(e)));
        return entities;
    }

    /**
     * @return all Zombie spawners in dungeon as a list.
     */
    public List<ZombieToastSpawner> getZombieSpawnerList() {
        List<ZombieToastSpawner> zombieSpwanwers = new ArrayList<>();
        
        List<Entity> entities = getEntities();

        for (Entity entity: entities) {
            if (entity instanceof ZombieToastSpawner) {
                zombieSpwanwers.add((ZombieToastSpawner)entity);
            }
        }

        return zombieSpwanwers;
    }

    /**
     * @return check if zombies are in the dungeon
     */
    public boolean checkZombiesExist() {
        List<Entity> entities = getEntities();

        for (Entity entity: entities) {
            if (entity instanceof ZombieToast) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return all portals in dungeon as a list.
     */
    public List<Portal> getPortals() {
        List<Portal> portals = new ArrayList<>();
        
        // map.values().stream().forEach(entity -> {
        //     if (entity instanceof Portal) {
        //         portals.add((Portal) entity);
        //     }

        // });
        
        List<Entity> entities = getEntities();

        for (Entity entity: entities) {
            if (entity instanceof Portal) {
                portals.add((Portal)entity);
            }
        }

        return portals;
    }

    /**
     * Gets the Entity with a given, corresponding id from the Dungeon (O(n^2) lookup). getEntity(Position pos, String id) 
     * should always be used instead if possible (i.e. if the Entity's position is available to you).
     * @param id
     * @return the Entity if it exists; otherwise null
     * 
     */
    public Entity getEntity(String id) {
        for (List<Entity> entities : map.values()) {
            Entity e = getEntityFromList(id, entities, false);
            if (e != null) return e;
        }
        return null;
    }

    /**
     * Gets the Entity with a given, corresponding position and id from the Dungeon. As it is O(n) lookup, it should be 
     * favoured over getEntity(String id).
     * @param pos
     * @param id
     * @return the Entity if it exists; otherwise null
     * 
     */
    public Entity getEntity(String id, Position pos) {
        return getEntityFromList(id, getEntitiesAt(pos), false);
    }

    private Entity getEntityFromList(String id, List<Entity> entities, boolean remove) {
        Optional<Entity> entity = entities.stream().filter(e -> e.getId().equals(id)).findFirst();
        if (entity.isEmpty()) return null; 
        if (remove) entities.remove(entity.get());
        return entity.get();
    }

    // for bomb activating exploding
    public void  removeEntitiesAtXYCoordinates(Position position) {
        // DO call emove entity of all layers on pos except for player

        List<Entity> entitiesAtPos = getEntitiesAt(position);
        
        if (entitiesAtPos != null) {
            List<Entity> entitiesCopy = new ArrayList<Entity>();
            entitiesCopy.addAll(entitiesAtPos);
            for (Entity entity: entitiesCopy) {
                if (!(entity instanceof Player)) {
                    removeEntity(entity.getId(), entity.getPosition());
                }
            }
        }
    }

    public List<Entity> getAdjacentEntities(Position position) {
        List<Entity> entities = new ArrayList<>();

        List<Entity> entitiesAtPos = new ArrayList<>();

        for (Direction d : Direction.values()) {
            entitiesAtPos = getEntitiesAt(position.translateBy(d));
            if (!d.equals(Direction.STAY) && entitiesAtPos != null) {
                for (Entity entity : entitiesAtPos) {
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    // Should not need to use this, please consult me first
    public void setMap(Map<Position, List<Entity>> map) {
        this.map = map;
    }

    public int getTileWeight(Position curPos) {
        List<Entity> entities = getEntitiesAt(curPos);
        // Only swamp tiles have different weights 
        if (MovementHandler.checkSwamp(entities)) {
            SwampTile swamp = MovementHandler.getSwamp(entities);
            return swamp.getMovementFactor();
        } else {
            return 1;
        }
    }

    public List<Entity> getAllOfType(String type) { // would like to remove
        List<Entity> entities = new ArrayList<>();
        map.values().stream().forEach(list -> list.stream().filter(e -> e.getType().equals(type)).forEach(e -> entities.add(e)));
        return entities;
    }

    public <T> List<T> getAllOfType(Class<T> type) {
        List<T> entities = new ArrayList<>();
        map.values().stream().forEach(list -> list.stream().filter(e -> e.getClass().equals(type)).forEach(e -> entities.add((T) e)));
        return entities;
    }

    public List<EntityResponse> getMapEntityResponse() {
        List<EntityResponse> entities = new ArrayList<>();
        map.values().stream()
            .forEach(list -> list.stream()
            .forEach(e -> entities.add(e.getEntityResponse())));
        return entities;
    }

    public Position getPlayerPos() {
        return getPlayer().getPosition();
    }

    public Player getPlayer() {
        Player player = (Player) getAllOfType(Player.class).get(0); 
        return player;
    }

}
