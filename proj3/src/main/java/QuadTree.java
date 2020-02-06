import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class QuadTree {
    Node root;
    String imgAddress;

    public QuadTree(Node node, int level, String imgRoot) {
        root = autoAddChild(node, level);
        imgAddress = imgRoot;
    }

    private Node autoAddChild(Node node, int level) {
        if (level == 1) {
            return node;
        } else {
            // upper left child
            int index = node.index * 10 + 1;
            double latInterval = (node.upperLeftLatitude - node.lowerRightLatitude) / 2;
            double lonInterval = (node.lowerRightLongitude - node.upperLeftLongitude) / 2;
            double upperLeftLatitude = node.upperLeftLatitude;
            double upperLeftLongitude = node.upperLeftLongitude;
            double lowerRightLatitude = node.lowerRightLatitude + latInterval;
            double lowerRightLongitude = node.lowerRightLongitude - lonInterval;
            node.addChild(autoAddChild(node, level - 1));

            // upper right child
            index += 1;
            lowerRightLongitude += lonInterval;
            upperLeftLongitude += lonInterval;
            Node child = new Node(index, upperLeftLongitude, upperLeftLatitude, lowerRightLongitude,
                    lowerRightLatitude, imgAddress, level - 1);
            node.addChild(autoAddChild(child, level - 1));

            // lower left child
            index += 1;
            lowerRightLongitude -= lonInterval;
            upperLeftLongitude -= lonInterval;
            lowerRightLatitude -= latInterval;
            upperLeftLatitude -= latInterval;
            child = new Node(index, upperLeftLongitude, upperLeftLatitude, lowerRightLongitude,
                    lowerRightLatitude, imgAddress, level - 1);
            node.addChild(autoAddChild(child, level - 1));

            // lower right child
            index += 1;
            lowerRightLongitude += lonInterval;
            upperLeftLongitude += lonInterval;
            child = new Node(index, upperLeftLongitude, upperLeftLatitude, lowerRightLongitude,
                    lowerRightLatitude, imgAddress, level - 1);
            node.addChild(autoAddChild(child, level - 1));
        }
        return node;
    }

    public void getRaster(Map<String, Double> params, Node n, Double queryLonDPP,
                          Map<String, Object> result, List<List<Node>> nodeList) {
        double query_ulX = params.get("ullon");
        double query_ulY = params.get("ullat");
        double query_lrX = params.get("lrlon");
        double query_lrY = params.get("lrlat");
        // check if this node is valid
        if (n.intersectQueryBox(query_ulX, query_ulY, query_lrX, query_lrY)) {
            // check if this node have the greatest LonDPP
            if (n.level != 1 && n.getLonDPP() > queryLonDPP) {
                for (Node child : n.children) {
                    // find suitable node from children
                    getRaster(params, child, queryLonDPP, result, nodeList);
                }
            } else {
                result.put("query_success", true);
                // set depth
                result.put("depth",root.level - n.level);
                // set ul_lon, ul_lat, lr_lon, lr_lat
                if (!result.containsKey("raster_ul_lon")) {
                    result.put("raster_ul_lon", n.upperLeftLongitude);
                    result.put("raster_ul_lat", n.upperLeftLatitude);
                    result.put("raster_lr_lon", n.lowerRightLongitude);
                    result.put("raster_lr_lat", n.lowerRightLatitude);
                } else {
                    if ((double) result.get("raster_ul_lon") > n.upperLeftLongitude) {
                        result.put("raster_ul_lon", n.upperLeftLongitude);
                    }
                    if ((double) result.get("raster_ul_lat") < n.upperLeftLatitude) {
                        result.put("raster_ul_lat", n.upperLeftLatitude);
                    }
                    if ((double) result.get("raster_lr_lon") < n.lowerRightLongitude) {
                        result.put("raster_ul_lon", n.lowerRightLongitude);
                    }
                    if ((double) result.get("raster_lr_lat") > n.lowerRightLatitude) {
                        result.put("raster_lr_lat", n.lowerRightLatitude);
                    }
                }
                // add node to list
                if (nodeList.size() == 0) {
                    List<Node> row = new ArrayList<>();
                    row.add(n);
                    nodeList.add(row);
                } else {
                    int size = nodeList.size();
                    int index = 0;
                    boolean existRow = false;
                    while (index < size && !existRow) {
                        List<Node> currRow = nodeList.get(index);
                        Node firstNode = currRow.get(0);
                        // find the row to contain this node
                        if (firstNode.upperLeftLatitude == n.upperLeftLatitude) {
                            insertNodeIntoRow(n,currRow);
                            existRow = true;
                        }
                        index++;
                    }
                    // could not find the row to contain this node
                    // add a new row into the list
                    if (!existRow) {
                        List<Node> row = new ArrayList<>();
                        row.add(n);
                        nodeList.add(row);
                    }
                }
            }
        } else {
            // does not include any region of the query box
            return;
        }
    }

    // insert node into the correct position of the row
    private void insertNodeIntoRow(Node n, List<Node> row){
        for (int i = 0; i < row.size(); i++) {
            Node compare = row.get(i);
            if (compare.upperLeftLongitude > n.upperLeftLongitude) {
                // add this node into this position
                row.add(i,n);
                return;
            }
        }
        // add this node into the last position
        row.add(n);
    }

    private class Node implements Comparable<Node> {
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
            childNum = 0;
            children = new Node[4];
            level = depth;
        }

        private void addChild(Node node) {
            if (childNum < 4) {
                children[childNum++] = node;
            }
        }

        public double getLonDPP() {
            return (lowerRightLongitude - upperLeftLongitude) / MapServer.TILE_SIZE;
        }

        public boolean intersectQueryBox(double query_ulX, double query_ulY, double query_lrX, double query_lrY) {
            if (upperLeftLongitude > query_lrX || upperLeftLatitude > query_lrY ||
                    lowerRightLongitude < query_ulX || lowerRightLatitude < query_ulY) {
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
}
