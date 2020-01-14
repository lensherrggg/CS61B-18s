package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class MapVisualTest {
    public static void main(String[] args) {
        MapGenerator mapGen = new MapGenerator(60, 30, new Position(15, 15));
        TERenderer ter = new TERenderer();
        ter.initialize(mapGen.getWorld().getWidthOfWorld(), mapGen.getWorld().getHeightOfWorld());

        // Room drawing test
        Position lB = new Position(0, 0);
        Position rT = new Position(5, 5);
        Room room = new Room(lB, rT);
        mapGen.makeInitialEntrance();
        mapGen.makeRoom(room);

        // Hallway darwing test
        Position e1 = new Position(1, 6);
        Position e2 = new Position(6, 1);
        Position c = new Position(6, 6);
        Hallway hallway1 = new Hallway(e1, e2, c);
        mapGen.makeHallway(hallway1);

        Position e3 = new Position(17, 1);
        Position e4 = new Position(20, 1);
        Hallway hallway2 = new Hallway(e3, e4);
        mapGen.makeHallway(hallway2);


        ter.renderFrame(mapGen.getMap());
    }
}
