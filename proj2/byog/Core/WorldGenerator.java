package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;

public class WorldGenerator {
    private static final int MAXROOMWIDTH = 6;
    private static final int MAXROOMHEIGHT = 6;
    private static final String NORTH = "N";
    private static final String SOUTH = "S";
    private static final String EAST = "E";
    private static final String WEST = "W";

    private int width;
    private int height;

    /** Nested class for easy access to coordinates */
    private class Position {
        int x;
        int y;

        private Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }

    private Position inititalPosition;
    private Random random;
    private TETile[][] world;

    /** Returns WorldGenerator Object without specified seed
     * @param w     width of world generated
     * @param h     height of world generated
     * @param iniX  x coordinate of initial LOCKED_DOOR
     * @param iniY  y coordinate of initial LOCKED_DOOR
     */
    public WorldGenerator(int w, int h, int iniX, int iniY) {
        width = w;
        height = h;
        inititalPosition = new Position(iniX, iniY);
        random = new Random();
    }

    /** Returns WorldGenerator Object with specified seed
     * @param w     width of world generated
     * @param h     height of world generated
     * @param iniX  x coordinate of initial LOCKED_DOOR
     * @param iniY  y coordinate of initial LOCKED_DOOR
     * @param seed  random seed used to generate world
     */
    public WorldGenerator(int w, int h, int iniX, int iniY, long seed) {
        width = w;
        height = h;
        inititalPosition = new Position(iniX, iniY);
        random = new Random(seed);
    }

    /** Initialize the world filling everything with NOTHING */
    public void initialize() {
        world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /** Make rectangular room filled with floor surrounded by walls */
    private void makeRoom(Position leftBottom, Position rightTop) {
        int leftBottomX = leftBottom.x;
        int leftBottomY = leftBottom.y;
        int rightTopX = rightTop.x;
        int rightTopY = rightTop.y;

        for (int i = leftBottomX; i <= rightTopX; i++) {
            for (int j = leftBottomY; j <= rightTopY; j++) {
                if (i == leftBottomX || i == rightTopX || j == leftBottomY || j == rightTopY) {
                    world[i][j] = Tileset.WALL;
                } else {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    /** Make initial entrance */
    private void makeInitialEntrance() {
        world[inititalPosition.x][inititalPosition.y] = Tileset.LOCKED_DOOR;
    }

    /** Make entrance by changing wall into floor */
    private void makeEntrance(Position entryPoint) {
        world[entryPoint.x][entryPoint.y] = Tileset.FLOOR;
    }

    /** Make exit by changing wall into floor */
    private void makeExit(Position exitPoint) {
        world[exitPoint.x][exitPoint.y] = Tileset.FLOOR;
    }

    /** Check availability for enough space for a room */
    private boolean checkAvailability(Position leftBottom, Position rightTop) {
        int leftBottomX = leftBottom.x;
        int leftBottomY = leftBottom.y;
        int rightTopX = rightTop.x;
        int rightTopY = rightTop.y;

        if (leftBottomX < 0 || width <= leftBottomX || leftBottomY < 0 || height <= leftBottomY
                || rightTopX < 0 || width <= rightTopX || rightTopY < 0 || height <= rightTopY) {
            return false;
        }

        for (int i = leftBottomX; i <= rightTopX; i += 1) {
            for (int j = leftBottomY; j <= rightTopY; j += 1) {
                TETile currentTile = world[i][j];
                if (currentTile == Tileset.WALL || currentTile == Tileset.FLOOR) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Returns random positions for a new room on the north of entryPosition if available,
     * otherwise returns null */
    private Position[] randomPositionNorth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int leftBottomX = entryPositionX - random.nextInt(w) - 1;
        int leftBottomY = entryPositionY;
        int rightTopX = leftBottomX + w + 1;
        int rightTopY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        if (!checkAvailability(leftBottom, rightTop)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightTop};
        }
    }

    /** Returns random positions for a new room on the east of entryPosition if available,
     * otherwise returns null */
    private Position[] randomPositionEast(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int leftBottomX = entryPositionX;
        int leftBottomY = entryPositionY - random.nextInt(h) - 1;
        int rightTopX = leftBottomX + w + 1;
        int rightTopY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        if (!checkAvailability(leftBottom, rightTop)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightTop};
        }
    }

    /** Returns random positions for a new room on the south of entryPosition if available,
     * otherwise returns null */
    private Position[] randomPositionSouth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int rightTopX = entryPositionX + random.nextInt(w) + 1;
        int rightTopY = entryPositionY;
        int leftBottomX = rightTopX - w - 1;
        int leftBottomY = rightTopY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        if (!checkAvailability(leftBottom, rightTop)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightTop};
        }
    }

    /** Returns random positions for a new room on the west of entryPosition if available,
     * otherwise returns null */
    private Position[] randomPositionWest(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int rightTopX = entryPositionX;
        int rightTopY = entryPositionY + random.nextInt(w) + 1;
        int leftBottomX = rightTopX - w - 1;
        int leftBottomY = rightTopY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        if (!checkAvailability(leftBottom, rightTop)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightTop};
        }
    }

    /** Get the reverse of a given direction */
    private String directionReverse(String direction) {
        switch(direction) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            default: return EAST;
        }
    }

    


}
