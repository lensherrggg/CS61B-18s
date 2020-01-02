import java.sql.Time;

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

        StdDraw.enableDoubleBuffering();

        for(double t = 0; t <= T; t = t + dt){
            double[] xForces = new double[Planets.length];
            double[] yForces = new double[Planets.length];
            // Calculate the net forces of every planet
            for(int i = 0; i < Planets.length; i++){
                xForces[i] = Planets[i].calcNetForceExertedByX(Planets);
                yForces[i] = Planets[i].calcNetForceExertedByY(Planets);
            }

            //Update positions and velocities of each planet
            for(int i = 0; i < Planets.length; i++){
                Planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");

            for(Planet planet : Planets){
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        // Print out the fianl state of the universe when time reaches T
        StdOut.printf("%d\n", Planets.length);
        StdOut.printf("%.2e\n", Radius);
        for(int i = 0; i < Planets.length; i++){
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", 
                Planets[i].xxPos, Planets[i].yyPos, Planets[i].xxVel, 
                Planets[i].yyVel, Planets[i].mass, Planets[i].imgFileName);
        }
    }
}