package uebung11_3;

import java.io.IOException;

/**
 * Represents an error in the Betting-Office data format.
 * 
 * @author Sven Karol
 */
public class IllegalDataFormatException extends IOException {

    private static final long serialVersionUID = -5289157438996783017L;
    
    public IllegalDataFormatException() {
        super();
    }
    
    public IllegalDataFormatException(String message) {
        super(message);
    }
    
}
