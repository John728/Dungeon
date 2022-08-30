package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


public class IdTests {
    @Test
    @DisplayName("No duplicate spider Ids after killing spider0")
    public void testDuplicateSpider() {
        // 2 spiders: spider0, spider1
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderIdTest", "c_OHKO"); // One Hit KO

        // walk down into spider0 and kill immediately
        res = dmc.tick(Direction.DOWN);

        // move player left until spider2 spawns (should not have the same id as spider1)
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // get ids again after changes
        List<String> spiderIds = new ArrayList<>();
        res.getEntities().stream().filter(entity -> 
                                          entity.getType().equals("spider")).forEach(spider ->
                                                                                              spiderIds.add(spider.getId()));


        // assert no duplicates
        Set<String> set = new HashSet<String>(spiderIds);
        assertEquals(set.size(), spiderIds.size());
    }
}
