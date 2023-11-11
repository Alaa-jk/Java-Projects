package Uebung_9;




/**
 * Musterlösung Aufgabe 9-1. 
 * 
 * @author Sven Karol
 *
 */
public class Cockroach {
    
    /**
     * Aufgabe 9-1.ii 
     */
    private String name;
    /**
     * Aufgabe 9-1.ii 
     */
    private Species species;
    /**
     * Aufgabe 9-1.ii 
     */
    private int maxVelocity; // auch Fließkommazahl möglich
    /**
     * Aufgabe 9-1.ii 
     */
    private int passedDistance; 
    
    /**
     * Aufgabe 9-1.iii
     */
    public Cockroach(String name, Species species, int maxVelocity) {
        this.name = name;
        this.species = species;
        this.maxVelocity = maxVelocity;
        passedDistance = 0;
    }
    
    /**
     * Aufgabe 9-1.ii 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Aufgabe 9-1.ii 
     */
    public Species getSpecies() {
        return species;
    }
    
    /**
     * Aufgabe 9-1.ii 
     */
    public int getPassedDistance() {
        return passedDistance;
    }
    
    /**
     * Aufgabe 9-1.iv
     */
    public void run() {
        passedDistance = passedDistance + (int)(Math.random() * maxVelocity);
    }
    
    /**
     * Aufgabe 9-1.v
     */
    public String toString() {
        return "Name: " + name 
                + ", Spezies: " + species 
                + ", Position: " + passedDistance
                + ", Velocity: " + maxVelocity;
    }
    
    public void backToStart() {
        passedDistance = 0;
    }
}
