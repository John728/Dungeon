package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertThrows;
import dungeonmania.response.models.DungeonResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewGameExceptTests {
    @Test
    @DisplayName("Test IllegalArgumentException is thrown when dungeonName does not exist.")
    public void testWrongDungeonName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("non-existent", "simple"));

    }

    @Test
    @DisplayName("Test IllegalArgumentException is thrown when configName does not exist.")
    public void testWrongConfigName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("2_doors", "non-existent"));
    }
}
