package Uebung_9;

import java.util.ArrayList;
import java.util.List;

/**
 * Musterlösung Aufgabe 9-3.
 * 
 * @author Sven Karol
 */
public class BettingOffice {
    
    /**
     * Aufgabe 9-3.i
     */
    private RoachRace race;
    
    /**
     * Aufgabe 9-3.i
     */
    private List<Bet> placedBets;
    
    /**
     * Aufgabe 9-3.i
     */
    private double quote; // Erweiterungsmöglichkeit: Quote pro Teilnehmer, Ableitung aus Geschwindigkeit
    
    /**
     * Aufgabe 9-3.i
     */
    public BettingOffice(RoachRace race, double quote) {
        placedBets = new ArrayList<Bet>();
        this.quote = quote;
        this.race = race;
    }

    /**
     * Aufgabe 9-3.ii
     */
    public void placeBet(String roachName, int amount, String playerName) {
        placedBets.add(new Bet(roachName, playerName, amount));
    }
    
    /**
     * Aufgabe 9-3.iii
     */
    public void performRace() {
        race.performRace();
    }
    
    /**
     * Aufgabe 9-3.iv
     */
    public int computeProfits(String playerName) {
        Cockroach winner = race.computeWinner();
        int profits = 0;
        if(winner != null) {
            for(Bet bet : placedBets) {
                if(bet.getPlayerName().equals(playerName) 
                        && bet.getRoachName().equals(winner.getName())) {
                    profits += (int) (bet.getAmount() * quote);
                }
            }
        }
        return profits;    
    }
    
    /**
     * Aufgabe 9-3.v
     */
    public String toString() {
        String result = race.toString();
        for(Bet bet : placedBets) {
            result += System.lineSeparator();
            result += bet;
        }
        return result;
    }
}
