public class Node implements Comparable<Node> {
    private double upperLeftLongitude;
    private double upperLeftLatitude;
    private double lowerRightLongitude;
    private double lowerRightLatitude;

    private int index;
    Node[] children;
    int childNum;
    String dir;
    int level;

    public Node(int n, double ullon, double ullat, double lrlon, double lrlat, String rootDir, int depth) {
        if (n == 0) {
            dir = rootDir + "root";
        } else {
            dir = rootDir + n;
        }
        upperLeftLongitude = ullon;
        upperLeftLatitude = ullat;
        lowerRightLongitude = lrlon;
        lowerRightLatitude = lrlat;
        index = n;
        childNum = 0;
        children = new Node[4];
        level = depth;
    }

    public double getUpperLeftLongitude() {
        return upperLeftLongitude;
    }

    public double getUpperLeftLatitude() {
        return upperLeftLatitude;
    }

    public double getLowerRightLongitude() {
        return lowerRightLongitude;
    }

    public double getLowerRightLatitude() {
        return lowerRightLatitude;
    }

    public int getIndex() {
        return index;
    }

    public void addChild(Node node) {
        if (childNum < 4) {
            children[childNum++] = node;
        }
    }

    public double getLonDPP() {
        return (lowerRightLongitude - upperLeftLongitude) / MapServer.TILE_SIZE;
    }

    public boolean intersectQueryBox(double query_ulX, double query_ulY, double query_lrX, double query_lrY) {
        if (upperLeftLongitude > query_lrX || upperLeftLatitude < query_lrY ||
                lowerRightLongitude < query_ulX || lowerRightLatitude > query_ulY) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Node o) {
        if (this.upperLeftLatitude == o.upperLeftLatitude) {
            if (this.upperLeftLongitude < o.upperLeftLongitude) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (this.upperLeftLatitude > o.upperLeftLatitude) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
