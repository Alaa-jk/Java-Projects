package uebung10_3_ii;

public class Cockroach extends Racer {
    
    private Species species;

    public Cockroach(String name, Species species, int maxVelocity) {
        super(name, maxVelocity);
        this.species = species;
    }
        
    public Species getSpecies() {
        return species;
    }

    @Override
    public String getSpeciesName() {
        return species.name();
    }
    
 }
