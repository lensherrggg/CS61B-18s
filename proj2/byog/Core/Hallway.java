package byog.Core;

public class Hallway {
    Position end1;
    Position end2;
    Position corner;
    String type;

    public Hallway(Position e1, Position e2) {
        end1 = e1;
        end2 = e2;
        if (e1.getX() == e2.getX()) {
            corner = new Position(e1.getX(), (e1.getY() + e2.getY()) / 2);
        }
        if (e1.getY() == e2.getY()) {
            corner = new Position((e1.getX() + e2.getX()) / 2, e1.getY());
        }
    }

    public Hallway(Position e1, Position e2, Position c) {
        end1 = e1;
        end2 = e2;
        corner = c;
        type = getType();
    }

    public String getType() {
        int x1 = Math.min(end1.getX(), end2.getX());
        int x2 = Math.max(end1.getX(), end2.getX());
        int y1 = Math.min(end1.getY(), end2.getY());
        int y2 = Math.max(end1.getY(), end2.getY());

        if (x1 == x2) {
            return "VT";
        }

        if (y1 == y2) {
            return "HZ";
        }

        int xC = corner.getX();
        int yC = corner.getY();

        if (xC == x1) {
            if (yC == y1) {
                return "LB";
            }
            if (yC == y2) {
                return "LT";
            }
        } else if (xC == x2) {
            if (yC == y1) {
                return "RB";
            }
            if (yC == y2) {
                return "RT";
            }
        } else {
            throw new RuntimeException("Corner position error");
        }
        return "";
    }
}
