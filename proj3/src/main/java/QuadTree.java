import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class QuadTree {
    Node root;
    String imgAddress;

    public QuadTree(Node node, int level, String imgRoot) {
        imgAddress = imgRoot;
        root = autoAddChild(node, level);
    }

    private Node autoAddChild(Node node, int level) {
        if (level == 1) {
            return node;
        } else {
            // upper left child
            int index = node.getIndex() * 10 + 1;
            double latInterval = (node.getUpperLeftLatitude() - node.getLowerRightLatitude()) / 2;
            double lonInterval = (node.getLowerRightLongitude() - node.getUpperLeftLongitude()) / 2;
            double upperLeftLatitude = node.getUpperLeftLatitude();
            double upperLeftLongitude = node.getUpperLeftLongitude();
            double lowerRightLatitude = node.getLowerRightLatitude() + latInterval;
            double lowerRightLongitude = node.getLowerRightLongitude() - lonInterval;
            Node child = new Node(index, upperLeftLongitude, upperLeftLatitude, lowerRightLongitude,
                    lowerRightLatitude, imgAddress, level - 1);
            node.addChild(autoAddChild(child, level - 1));

            // upper right child
            index += 1;
            lowerRightLongitude += lonInterval;
            upperLeftLongitude += lonInterval;
            child = new Node(index, upperLeftLongitude, upperLeftLatitude, lowerRightLongitude,
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
                    result.put("raster_ul_lon", n.getUpperLeftLongitude());
                    result.put("raster_ul_lat", n.getUpperLeftLatitude());
                    result.put("raster_lr_lon", n.getLowerRightLongitude());
                    result.put("raster_lr_lat", n.getLowerRightLatitude());
                } else {
                    if ((double) result.get("raster_ul_lon") > n.getUpperLeftLongitude()) {
                        result.put("raster_ul_lon", n.getUpperLeftLongitude());
                    }
                    if ((double) result.get("raster_ul_lat") < n.getUpperLeftLatitude()) {
                        result.put("raster_ul_lat", n.getUpperLeftLatitude());
                    }
                    if ((double) result.get("raster_lr_lon") < n.getLowerRightLongitude()) {
                        result.put("raster_ul_lon", n.getLowerRightLongitude());
                    }
                    if ((double) result.get("raster_lr_lat") > n.getLowerRightLatitude()) {
                        result.put("raster_lr_lat", n.getLowerRightLatitude());
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
                        if (firstNode.getUpperLeftLatitude() == n.getUpperLeftLatitude()) {
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
            if (compare.getUpperLeftLongitude() > n.getUpperLeftLongitude()) {
                // add this node into this position
                row.add(i,n);
                return;
            }
        }
        // add this node into the last position
        row.add(n);
    }
}
