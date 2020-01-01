public class NBody{
    public static double readRadius(String fileName){
        In in = new In(fileName);
        in.readInt();
        double Radius = in.readDouble();
        return Radius;
    }

    public static Planet[] readPlanets(String fileName){
        In in = new In(fileName);
        int PlanetNum = in.readInt();
        in.readDouble();
        Planet[] Planets = new Planet[PlanetNum];
        for(int i = 0; i < PlanetNum; i++){
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            Planets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return Planets;
    }

    public static void main(String args[]){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String fileName = args[2];
        double Radius = readRadius(fileName);
        Planet[] Planets = readPlanets(fileName);

        StdDraw.setScale(-Radius, Radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");

        for(Planet planet : Planets){
            planet.draw();
        }
    }
}