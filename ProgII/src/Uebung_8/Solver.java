package Uebung_8;

/**
 * Ein Löser für quadratische Gleichungen.
 * 
 * @author Sven Karol
 */
public class Solver {
    
    // Musterlösung 8_1.iii
	/**
     * Berechnet die Nullstellen einer quadartischen Gleichung der Form a*x^2 + b*x + c.
     * 
     * @param a a*x^2
     * @param b b*x
     * @param c c
     * @return die maximal 2 Nullstellen der Gleichung
     */
	public static Complex[] findRoots(double a, double b, double c) {
        double halfP = (b / a) / 2;
        double q = c / a;
        
        double discriminat = halfP * halfP - q;
        Complex[] solutions = new Complex[2];
        
        if(discriminat >= 0.0) {
            solutions[0] = new Complex(-1 * halfP - Math.sqrt(discriminat), 0.0);
            solutions[1] = new Complex(-1 * halfP + Math.sqrt(discriminat), 0.0);
        }
        else {
            solutions[0] = new Complex(-1 * halfP, - Math.sqrt(Math.abs(discriminat)));
            solutions[1] = new Complex(-1 * halfP, Math.sqrt(Math.abs(discriminat)));
        }
        
        return solutions;
    }
    
}
