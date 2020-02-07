import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static final String IMGFORMAT = ".png";
    private QuadTree qt;

    /** imgRoot is the name of the image directory*/
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        Node root = new Node(0, MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON,
                MapServer.ROOT_LRLAT, imgRoot, 8);
        qt = new QuadTree(root, 8, imgRoot);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        double queryUllon = params.get("ullon");
        double queryUllat = params.get("ullat");
        double queryLrlon = params.get("lrlon");
        double queryLrlat = params.get("lrlat");
        if (queryLrlon < queryUllon || queryLrlat >queryUllat) {
            throw new IllegalArgumentException("Wrong Longitude And Latitude");
        }
        double width = params.get("w");
        double queryLonDPP = (queryLrlon - queryUllon) / width;
        Map<String, Object> results = new HashMap<>();
        List<List<Node>> tempList = new ArrayList<>();
        qt.getRaster(params, qt.root, queryLonDPP, results, tempList);
        String[][] fileName = converNodeListToArray(tempList);
        results.put("render_grid", fileName);

        return results;
    }

    private String[][] converNodeListToArray(List<List<Node>> list) {
        String[][] result = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            List<Node> currRow = list.get(i);
            result[i] = new String[currRow.size()];
            for (int j = 0; j < currRow.size(); j++) {
                result[i][j] = currRow.get(i).dir + IMGFORMAT;
            }
        }
        return result;
    }

}
