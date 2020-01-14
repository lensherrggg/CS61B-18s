package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;

public class MapGenerator {
    private static final int MAXROOMWIDTH = 6;
    private static final int MAXROOMHEIGHT = 6;
    private static final String NORTH = "N";
    private static final String SOUTH = "S";
    private static final String EAST = "E";
    private static final String WEST = "W";


    private World world;
    private Random random;
    private TETile[][] map;

    /** Returns MapGenerator Object without a specified seed
     * @param w       width of world generated
     * @param h       height of world generated
     * @param iniPos  coordinate of initial LOCKED_DOOR
     */
    public MapGenerator(int w, int h, Position iniPos) {
        world = new World(w, h, iniPos);
        map = new TETile[w][h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                map[i][j] = Tileset.NOTHING;
//            }
//        }
        initialize();
    }

    /** Returns MapGenerator Object with a specified seed
     * @param w       width of world generated
     * @param h       height of world generated
     * @param iniPos  coordinate of initial LOCKED_DOOR
     * @param seed    seed of random
     */
    public MapGenerator(int w, int h, Position iniPos, long seed) {
        world = new World(w, h, iniPos);
        random = new Random(seed);
        map = new TETile[w][h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                map[i][j] = Tileset.NOTHING;
//            }
//        }
        initialize();
    }

    /** Initialize the world filling everything with NOTHING */
    public void initialize() {
        int width = world.getWidthOfWorld();
        int height = world.getHeightOfWorld();
        map = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
    }

    /** Return the tile of the selected coordinate */
    public TETile getTile(Position pos) {
        if (world.outOfBorder(pos)) {
            throw new IllegalArgumentException("Position out of border");
        }
        return map[pos.getX()][pos.getY()];
    }

    /** Make initial entrance */
    public void makeInitialEntrance() {
        int xPos = world.getInititalPosition().getX();
        int yPos = world.getInititalPosition().getY();
        map[xPos][yPos] = Tileset.LOCKED_DOOR;
    }

    /** Make rectangular room filled with floor surrounded by walls */
    public void makeRoom(Room room) {
        int leftBottomX = room.getLeftBottom().getX();
        int leftBottomY = room.getLeftBottom().getY();
        int rightTopX = room.getRightTop().getX();
        int rightTopY = room.getRightTop().getY();

        for (int i = leftBottomX; i <= rightTopX; i++) {
            for (int j = leftBottomY; j <= rightTopY; j++) {
                if (i == leftBottomX || i == rightTopX || j == leftBottomY || j == rightTopY) {
                    map[i][j] = Tileset.WALL;
                } else {
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    /** Make entrance by changing wall into floor */
    public void makeEntrance(Position entryPoint) {
        map[entryPoint.getX()][entryPoint.getY()] = Tileset.FLOOR;
    }

    /** Make exit by changing wall into floor */
    public void makeExit(Position exitPoint) {
        map[exitPoint.getX()][exitPoint.getY()] = Tileset.FLOOR;
    }

    /** Check availability for enough space for a room */
    public boolean checkRoomAvailability(Room room) {
        int leftBottomX = room.getLeftBottom().getX();
        int leftBottomY = room.getLeftBottom().getY();
        int rightTopX = room.getRightTop().getX();
        int rightTopY = room.getRightTop().getY();
        int width = world.getWidthOfWorld();
        int height = world.getHeightOfWorld();

        if (leftBottomX < 0 || width <= leftBottomX || leftBottomY < 0 || height <= leftBottomY
                || rightTopX < 0 || width <= rightTopX || rightTopY < 0 || height <= rightTopY) {
            return false;
        }

        for (int i = leftBottomX; i <= rightTopX; i += 1) {
            for (int j = leftBottomY; j <= rightTopY; j += 1) {
                TETile currentTile = map[i][j];
                if (currentTile == Tileset.WALL || currentTile == Tileset.FLOOR) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Returns a new room on the north of entryPosition if available,
     * otherwise returns null */
    public Room randomRoomNorth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.getX();
        int entryPositionY = entryPosition.getY();
        int leftBottomX = entryPositionX - random.nextInt(w) - 1;
        int leftBottomY = entryPositionY;
        int rightTopX = leftBottomX + w + 1;
        int rightTopY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        Room newRoom = new Room(leftBottom, rightTop);
        if (!checkRoomAvailability(newRoom)) {
            return null;
        } else {
            return newRoom;
        }
    }

    /** Returns a new room on the east of entryPosition if available,
     * otherwise returns null */
    public Room randomRoomEast(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.getX();
        int entryPositionY = entryPosition.getY();
        int leftBottomX = entryPositionX;
        int leftBottomY = entryPositionY - random.nextInt(h) - 1;
        int rightTopX = leftBottomX + w + 1;
        int rightTopY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        Room newRoom = new Room(leftBottom, rightTop);
        if (!checkRoomAvailability(newRoom)) {
            return null;
        } else {
            return newRoom;
        }
    }

    /** Returns a new room on the south of entryPosition if available,
     * otherwise returns null */
    public Room randomRoomSouth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.getX();
        int entryPositionY = entryPosition.getY();
        int rightTopX = entryPositionX + random.nextInt(w) + 1;
        int rightTopY = entryPositionY;
        int leftBottomX = rightTopX - w - 1;
        int leftBottomY = rightTopY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        Room newRoom = new Room(leftBottom, rightTop);
        if (!checkRoomAvailability(newRoom)) {
            return null;
        } else {
            return newRoom;
        }
    }

    /** Returns a new room on the west of entryPosition if available,
     * otherwise returns null */
    public Room randomRoomWest(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.getX();
        int entryPositionY = entryPosition.getY();
        int rightTopX = entryPositionX;
        int rightTopY = entryPositionY + random.nextInt(w) + 1;
        int leftBottomX = rightTopX - w - 1;
        int leftBottomY = rightTopY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightTop = new Position(rightTopX, rightTopY);
        Room newRoom = new Room(leftBottom, rightTop);
        if (!checkRoomAvailability(newRoom)) {
            return null;
        } else {
            return newRoom;
        }
    }

    public void makeHallway(Hallway hallway) {
        makeVerticalHallway(hallway);
        makeHorizontalHallway(hallway);
        makeHallwayCorner(hallway);
    }

    public void makeVerticalHallway(Hallway hallway) {
        if (hallway.end1.getX() == hallway.end2.getX()) {
            return;
        }
        int x1 = Math.min(hallway.end1.getX(), hallway.end2.getX());
        int x2 = Math.max(hallway.end1.getX(), hallway.end2.getX());
        int y = hallway.corner.getY();
//        for (int i = x1; i < x2 - 1; i++) {
//            map[i][y] = Tileset.FLOOR;
//            if (getTile(new Position(i, y - 1)) == Tileset.NOTHING) {
//                map[i][y - 1] = Tileset.WALL;
//            }
//            if (getTile(new Position(i, y + 1)) == Tileset.NOTHING) {
//                map[i][y + 1] = Tileset.WALL;
//            }
//        }
        // add tile
    }

    public void makeHorizontalHallway(Hallway hallway) {
        if (hallway.end1.getY() == hallway.end2.getY()) {
            return;
        }
        int y1 = Math.min(hallway.end1.getY(), hallway.end2.getY());
        int y2 = Math.max(hallway.end1.getY(), hallway.end2.getY());
        int x = hallway.corner.getX();
        // add tile
    }

    public void makeHallwayCorner(Hallway hallway) {

    }

    /** Get the reverse of a given direction */
    public String directionReverse(String direction) {
        switch(direction) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            default: return EAST;
        }
    }



}
