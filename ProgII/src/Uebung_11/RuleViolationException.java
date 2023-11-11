package uebung11_2_i_ii;

/**
 * Geprüfte Ausnahme für Regelverstöße im Rennverlauf.
 * 
 * Musterlösung Aufgabe 11-2-ii
 * 
 * @author Sven Karol
 *
 */
public class RuleViolationException extends RaceException {

    private static final long serialVersionUID = 3373430889744903758L;

    public RuleViolationException(Racer ...causes) { super(causes); }
    
    public RuleViolationException(String message, Racer ...causes) { super(message, causes); }
    
    public RuleViolationException(Throwable cause, Racer ...causes) { super(cause, causes); }
    
    public RuleViolationException(String message, Throwable cause, Racer ...causes) { super(message, cause, causes); }  
    
}
