package dungeonmania.util;

import java.util.Random;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    STAY(0,0);

    private final Position offset;

    private Direction(Position offset) {
        this.offset = offset;
    }

    private Direction(int x, int y) {
        this.offset = new Position(x, y);
    }

    public Position getOffset() {
        return this.offset;
    }

    public static Direction randomDirection() {
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }
}
