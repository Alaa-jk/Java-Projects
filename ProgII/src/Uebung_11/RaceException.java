package uebung11_2_i_ii;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Geprüfte Ausnahme für Probleme im Rennverlauf.
 * 
 * Musterlösung Aufgabe 11-2-ii
 * 
 * @author Sven Karol
 *
 */
public abstract class RaceException extends Exception {

    private static final long serialVersionUID = 5968251754904339685L;
    
    private List<Racer> causalities = new LinkedList<Racer>();
    
    public List<Racer> getCausalities() {
       return Collections.unmodifiableList(causalities); 
    }
    
    public RaceException(Racer ...causes) {
        super();
        causalities.addAll(Arrays.asList(causes));
    }
    
    public RaceException(String message, Racer ...causes) {
        super(message);
        causalities.addAll(Arrays.asList(causes));
    }
    
    public RaceException(Throwable cause, Racer ...causes) {
        super(cause);
        causalities.addAll(Arrays.asList(causes));
    }
    
    public RaceException(String message, Throwable cause, Racer ...causes) {
        super(message, cause);
        causalities.addAll(Arrays.asList(causes));
    }  
}
