package uebung11_2_i_ii;

public class Snail extends Racer {
    private String species;
 
    public Snail(String name, String species, int maxVelocity) {
        super(name, maxVelocity);
        if(species == null) {
            throw new IllegalArgumentException("Please provide a value for species!");
        }
        this.species = species;
    }

    @Override
    public String getSpeciesName() {
        return species;
    }

    @Override
    public String getClassName() {
        return "Snail";
    }
}
