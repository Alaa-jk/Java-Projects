package uebung11_2_i_ii;

public abstract class Racer {
    private String name;
    private int maxVelocity; // auch Fließkommazahl möglich
    private int passedDistance; 
    
    public Racer(String name, int maxVelocity) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please provide a valid name!");
        }
        if(maxVelocity <= 0) {
            throw new IllegalArgumentException("Please provide a non-negative value for maxVelocity!");
        }
        this.name = name;
        this.maxVelocity = maxVelocity;
        passedDistance = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String getSpeciesName();
    
    public abstract String getClassName();
    
    public int getPassedDistance() {
        return passedDistance;
    }
    
    public void run() /* throws RaceException */ {
        //if(Math.random() < 0.05) // ok, das sind keine 5% => wir können das ohne Kontext nicht wissen
        //    throw new RuleViolationException(this);
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
    
    public int getMaxVelocity() {
        return maxVelocity;
    }
}
