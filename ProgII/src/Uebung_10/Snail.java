package uebung10_3_ii;

/**
 * Zusätzliche Racer: Schnecken.
 * 
 * @author Sven Karol
 *
 */
public class Snail extends Racer {
    private String species;
 
    public Snail(String name, String species, int maxVelocity) {
        super(name, maxVelocity);
        this.species = species;
    }

    @Override
    public String getSpeciesName() {
        return species;
    }
}
