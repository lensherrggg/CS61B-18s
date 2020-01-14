package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class World {
    private int width;
    private int height;
    private Position inititalPosition;

    public World(int w, int h, Position iniPos) {
        width = w;
        height = h;
        inititalPosition = iniPos;
    }

    public World getWorld() {
        return this;
    }

    public int getWidthOfWorld() {
        return width;
    }

    public int getHeightOfWorld() {
        return height;
    }

    public Position getInititalPosition() {
        return inititalPosition;
    }

    public boolean outOfBorder(Position pos) {
        if (pos.getX() < 0 || pos.getX() > width) {
            return true;
        } else if(pos.getY() < 0 || pos.getY() > height) {
            return true;
        } else {
            return false;
        }
    }

}
