package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Room {
    private Position leftBottom;
    private Position rightTop;

    public Room(Position lB, Position rT) {
        leftBottom = lB;
        rightTop = rT;
    }

    public Position getLeftBottom() {
        return leftBottom;
    }

    public Position getRightTop() {
        return rightTop;
    }

    public boolean insideRoom(Position pos) {
        if (pos.getX() < leftBottom.getX() || pos.getX() > rightTop.getX()) {
            return false;
        }
        if (pos.getY() < leftBottom.getY() || pos.getY() > rightTop.getY()) {
            return false;
        }
        return true;
    }

}
