package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static void addHexagon(TETile[][] world, Position pos, int size, TETile t) {
        if (size < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }
        for (int i = 0; i < 2 * size; i++) {
            addRow(world, pos, i, size, t);
        }
    }

    private static int offsetOfLine(int lineNum, int size) {
        if (lineNum < size) {
            return size - 1 - lineNum;
        } else {
            return lineNum - size;
        }
    }

    private static int widthOfLine(int lineNum, int size) {
        if (lineNum < size) {
            return size + 2 * lineNum;
        } else {
            return 5 * size - 2 - 2 * lineNum;
        }
    }

    private static void addRow(TETile[][] world, Position pos, int lineNum, int size, TETile t) {
        int offset = offsetOfLine(lineNum, size);
        for (int j = 0; j < widthOfLine(lineNum, size); j++) {
            world[pos.x + offset + j + 1][pos.y + lineNum] = t;
        }
    }

    public static void main (String[] args) {
        int WIDTH = 60;
        int HEIGHT = 30;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        Position pos = new Position(3, 0);
        addHexagon(world, pos, 5, Tileset.FLOWER);
        ter.renderFrame(world);

    }
}
