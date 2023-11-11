package uebung11_2_i_ii;

/**
 * Geprüfte Ausnahme für Rennunfälleim Rennverlauf.
 * 
 * Musterlösung Aufgabe 11-2-ii
 * 
 * @author Sven Karol
 *
 */
public class AccidentException extends RaceException {

    private static final long serialVersionUID = 1328923836280860464L;
    
    public AccidentException(Racer ...causes) { super(causes); }
    
    public AccidentException(String message, Racer ...causes) { super(message, causes); }
    
    public AccidentException(Throwable cause, Racer ...causes) { super(cause, causes); }
    
    public AccidentException(String message, Throwable cause, Racer ...causes) { super(message, cause, causes); }  

}
