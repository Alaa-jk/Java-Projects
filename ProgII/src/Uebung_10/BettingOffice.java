package uebung10_3_ii;

import java.util.ArrayList;
import java.util.List;

public class BettingOffice {
    
    private IllegalRace<?> race;
    private List<Bet> placedBets;
    private double quote; // Erweiterungsmöglichkeit: Quote pro Teilnehmer, Ableitung aus Geschwindigkeit
    
    public BettingOffice(IllegalRace<?> race, double quote) {
        placedBets = new ArrayList<Bet>();
        this.quote = quote;
        this.race = race;
    }

    public void placeBet(String roachName, int amount, String playerName) {
        placedBets.add(new Bet(roachName, playerName, amount));
    }
    
    public void performRace() {
        race.performRace();
    }
    
    public int computeProfits(String playerName) {
        Racer winner = race.computeWinner();
        int profits = 0;
        if(winner != null) {
            for(Bet bet : placedBets) {
                if(bet.getPlayerName().equals(playerName) 
                        && bet.getParticipantName().equals(winner.getName())) {
                    profits += (int) (bet.getAmount() * quote);
                }
            }
        }
        return profits;    
    }
    
    public String toString() {
        String result = race.toString();
        for(Bet bet : placedBets) {
            result += System.lineSeparator();
            result += bet;
        }
        return result;
    }
}
