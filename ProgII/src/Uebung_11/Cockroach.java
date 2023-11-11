package uebung11_2_i_ii;

public class Cockroach extends Racer {
    
    private Species species;
   
    public Cockroach(String name, Species species, int maxVelocity) {
        super(name, maxVelocity);
        if(species == null) {
            throw new IllegalArgumentException("Please provide a value for species!");
        }
        this.species = species;
    }
        
    public Species getSpecies() {
        return species;
    }

    @Override
    public String getSpeciesName() {
        return species.name();
    }

    @Override
    public String getClassName() {
        return "Cockroach";
    }
    
 }
