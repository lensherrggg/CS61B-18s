public class Planet{
    private static double G = 6.67e-11;

    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p){
        double distance = Math.sqrt(Math.pow(this.xxPos - p.xxPos, 2) + Math.pow(this.yyPos - p.yyPos, 2));
        return distance;
    }

    public double calcForceExertedBy(Planet p){
        double force = G * this.mass * p.mass / (Math.pow(this.xxPos - p.xxPos, 2) + Math.pow(this.yyPos - p.yyPos, 2));
        return force;
    }

    public double calcForceExertedByX(Planet p){
        return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
    }

    public double calcForceExertedByY(Planet p){
        return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] allPlanets){
        double forceX = 0;
        for(int i = 0; i < allPlanets.length; i++){
            if(!allPlanets[i].equals(this)){
                forceX = forceX + this.calcForceExertedByX(allPlanets[i]);
            }
        }
        return forceX;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets){
        double forceY = 0;
        for(int i = 0; i < allPlanets.length; i++){
            if(!allPlanets[i].equals(this)){
                forceY = forceY + this.calcForceExertedByY(allPlanets[i]);
            }
        }
        return forceY;
    }

    public void update(double dTime, double xForce, double yForce){
        double xxAcc = xForce / this.mass;
        double yyAcc = yForce / this.mass;
        this.xxVel = this.xxVel + dTime * xxAcc;
        this.yyVel = this.yyVel + dTime * yyAcc;
        this.xxPos = this.xxPos + dTime * xxVel;
        this.yyPos = this.yyPos + dTime * yyVel;
    }

    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
}