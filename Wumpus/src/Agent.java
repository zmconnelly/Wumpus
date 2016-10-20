
import java.util.Random;

public abstract class Agent {

    protected Location location;
    protected Direction direction;
    protected final byte BREEZE = 0b00000001, STENCH = 0b0000010, BUMP = 0b00000100, GLITTER = 0b00001000, DEATH = 0b00010000, DEATH_WUMPUS = 0b00100000, SCREAM = 0b01000000;
    protected static final int GRAB = 1, MOVE = 2, TURN_LEFT = 3, TURN_RIGHT = 4, SHOOT = 5, QUIT = 6;
    protected int percepts, arrowCount;
    protected World world;
    protected Random random = new Random();

    public Agent(World world) {
        this.world = world;
        this.arrowCount = world.arrowCount;
        this.location = new Location(world.x, world.y);
     //   this.direction = NORTH;
        this.percepts = world.getPercepts();
    }

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        private final Direction[] terms = values();

        public Direction left() {
            return terms[(this.ordinal() + 1) % terms.length];
        }

        public Direction right() {
            return terms[(this.ordinal() - 1) % terms.length];
        }
    }

    public enum State {
        SAFE,
        UNSAFE,
        EXPLORED;
    }

    public void updateLocation() {
        switch (direction) {
            case NORTH:
                if (location.y < World.size - 1) {
                    location.y += 1;
                }
                break;
            case EAST:
                if (location.x > 0) {
                    location.x -= 1;
                }
                break;
            case SOUTH:
                if (location.y > 0) {
                    location.y--;
                }
                break;
            case WEST:
                if (location.x < World.size - 1) {
                    location.x++;
                }
        }
    }

    protected class Location {

        int x;
        int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
