package uebung10_3_ii;

/**
 * Eine abstrakte Elterklasse f�r Rennteilnehmer.
 * 
 * @author Sven Karol
 *
 */
public abstract class Racer {
    private String name;
    private int maxVelocity; // auch Flie�kommazahl m�glich
    private int passedDistance; 
    
    public Racer(String name, int maxVelocity) {
        this.name = name;
        this.maxVelocity = maxVelocity;
        passedDistance = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String getSpeciesName();
    
    public int getPassedDistance() {
        return passedDistance;
    }
    
    public void run() {
        passedDistance = passedDistance + (int)(Math.random() * maxVelocity);
    }
    
    public String toString() {
        return "Name: " + name 
                + ", Spezies: " + getSpeciesName() 
                + ", Position: " + passedDistance
                + ", Velocity: " + maxVelocity;
    }
    
    public void backToStart() {
        passedDistance = 0;
    }
}
