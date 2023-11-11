package uebung11_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import uebung11_2_i_ii.Bet;
import uebung11_2_i_ii.BettingOffice;
import uebung11_2_i_ii.Cockroach;
import uebung11_2_i_ii.IllegalRace;
import uebung11_2_i_ii.Racer;
import uebung11_2_i_ii.Snail;
import uebung11_2_i_ii.Species;


/**
 * Diese Klasse stellt Methoden zum Laden und speichern von Betting-Office Objekten
 * bereit.
 * 
 * Musterlösung Aufgabe 11-3
 * 
 * @author Sven Karol
 */
public class BettingOfficeIO { 
    
    /**
     * Speichert das übergebene Wettbüro in die angegebene Datei.
     * 
     * @param path Der Pfad zur Datei in der gespeichert werden soll.
     * @param office Das zu speichernde Wettbüro.
     * @throws IOException wenn es einen Schreibfehler gibt.
     */
    public static void writeToFile(File path, BettingOffice<?> office) throws IOException {
        if(!path.exists()) {
            path.createNewFile();
        }
        try(PrintStream out = new PrintStream(path)){
            out.print(office.getRace().getName());
            out.print(",");
            out.print(office.getRace().getDistance());
            out.print(",");
            out.println(office.getQuote());
            
            for(Racer racer : office.getRace().getParticipants()) {
                out.println(toSerialString(racer));
            }
            for(Bet bet : office.getBets()) {
                out.println(toSerialString(bet));
            }
        }
    }

    /**
     * Lädt ein Wettbüro aus der angegebenen Datei.
     * 
     * 
     * @param path Der Pfad zur zu ladenden Datei.
     * @return Das geladene Wettbüro.
     * @throws IOException wenn es ein technisches Problem beim Laden gibt.
     * @throws IllegalDataFormatException wenn es einen Fehler im Format der Datei gibt.
     */
    public static BettingOffice<Racer> loadFromFile(File path) throws IOException, IllegalDataFormatException {
        try(var in = new BufferedReader(new FileReader(path))) {
            String[] firstLine = in.readLine().split(",");
            if(firstLine.length != 3) {
                throw new IllegalDataFormatException("First line must consist of three segments!");
            }
            String name = firstLine[0];
            int distance = -1;
            double quote = 0;
            
            try {
                distance = Integer.parseInt(firstLine[1]);
            }
            catch(NumberFormatException e) {
                throw new IllegalDataFormatException("Second argument of first line must be an integer number.");
            }
            try {
                quote = Double.parseDouble(firstLine[2]); 
            }
            catch(NumberFormatException e) {
                throw new IllegalDataFormatException("Third argument of first line must be a double number.");
            }
            
            BettingOffice<Racer> office = new BettingOffice<Racer>(new IllegalRace<Racer>(name, distance), quote);
            List<Bet> bets = new LinkedList<Bet>();
            for(String line = in.readLine(); line != null; line = in.readLine()) {
                if(line.startsWith("R:")) {
                    office.getRace().addParticipant(racerFromSerialString(line));
                }
                else {
                    bets.add(fromSerialString(line));
                }
            }
            return office;
        }
    }
    
    /**
     * Konvertiert ein übergebenes Racer-Objekt in einen String, der in eine Textdatei geschrieben werden kann.
     * 
     * @param racer Das Object, für den ein String erzeugt werden soll.
     * @return String, der das übergebene Objekt repräsentiert.
     */
    private static String toSerialString(Racer racer) {
        return  "R:"+ racer.getClassName()
                + "," + racer.getSpeciesName() 
                + "," + racer.getMaxVelocity()
                + "," + racer.getName();
    } 
    
    /**
     * Konvertiert einen gegebenen String in ein Racer-Objekt. Vorraussetzung: der String entspricht dem von
     * {@link #toSerialString(Racer)} verwendeten Format.
     * 
     * @param s Der zu konvertierende String.
     * @return Ein dem String entsprechendes Objekt, falls dieses erstellt werden kann.
     * @throws IllegalDataFormatException Wenn das Format nicht korrekt ist. 
     */
    private static Racer racerFromSerialString(String s) throws IllegalDataFormatException{
        if(s.startsWith("R:")) {
            String[] outerSplit = s.split(":");
            if(outerSplit.length != 2) 
                throw new IllegalDataFormatException("Data expected after 'R:'.");
            String[] payload = outerSplit[1].split(",");
            if(payload.length != 4) {
                throw new IllegalDataFormatException("Four values expected after 'R:'.");
            }
            try {
                int maxVelocity = Integer.parseInt(payload[2]);
                if(payload[0].equals("Snail")) {
                    return new Snail(payload[3], payload[1], maxVelocity);
                }
                else if (payload[0].equals("Cockroach")) {
                    return new Cockroach(payload[3], Species.valueOf(payload[1]), maxVelocity);
                }
                else {
                    throw new IllegalDataFormatException("Unknown class '" + payload[0] + "'.");
                }
               
            }
            catch(NumberFormatException e) {
                throw new IllegalDataFormatException("Integer expected as a third value.");
            }

        }
        else {
            throw new IllegalDataFormatException("Racer-data must start with 'R:'.");
        }
    }
    
    /**
     * Konvertiert ein gegebenes Bet-Objekt in einen String, der in eine Textdatei geschrieben werden kann.
     * 
     * @param bet Das Objekt, welches konvertiert werden kann.
     * @return Die passende Zeichenkette.
     */
    private static String toSerialString(Bet bet) {
        return "B:" + bet.getRoachName() 
        		+ "," + bet.getAmount() 
        		+ "," + bet.getPlayerName();
    }
    
    /**
     * Erzeugt aus einem gegeben String ein Bet-Objekt. Vorraussetzung: der String entspricht dem von {@link #toSerialString(Bet)} 
     * verwendeten Format. 
     * 
     * @param s Der zu konvertierende String.
     * @return Das entsprechende Bet-Objekt, wenn das Format stimmt.
     * @throws IllegalDataFormatException Wenn das Format nicht stimmt.
     */
    private static Bet fromSerialString(String s) throws IllegalDataFormatException {
        if(s.startsWith("B:")) {
            String[] outerSplit = s.split(":");
            if(outerSplit.length != 2) 
                throw new IllegalDataFormatException("Data expected after 'B:'.");
            String[] payload = outerSplit[1].split(",");
            if(payload.length != 3) {
                throw new IllegalDataFormatException("Three values expected after 'B:'.");
            }
            try {
                int amount = Integer.parseInt(payload[1]);
                return new Bet(payload[0], amount, payload[2]);
            }
            catch(NumberFormatException e) {
                throw new IllegalDataFormatException("Integer expected as second value.");
            }

        }
        else {
            throw new IllegalDataFormatException("Bet-data must start with 'B:'.");
        }
    }
    
}
