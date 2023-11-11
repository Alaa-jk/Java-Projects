package uebung11_2_i_ii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BettingOffice<T extends Racer> {
    
    private IllegalRace<T> race;
    private List<Bet> placedBets;
    private double quote; // Erweiterungsmöglichkeit: Quote pro Teilnehmer, Ableitung aus Geschwindigkeit
    
    public BettingOffice(IllegalRace<T> race, double quote) {
        placedBets = new ArrayList<Bet>();
        this.quote = quote;
        this.race = race;
    }

    public void placeBet(String roachName, int amount, String playerName) {
        placedBets.add(new Bet(roachName, amount, playerName));
    }
    
    public void performRace() {
        try {
            race.performRace();
        } 
        catch(RuleViolationException e) {
            System.err.println("Rules violated!");
            System.err.println("Disqualifying " + e.getCausalities().get(0));
            race.removeParticipant(e.getCausalities().get(0).getName());
            race.backToStart();
            performRace();
        }
        catch(AccidentException e) {
            System.err.println("Accident happened!");
            System.err.println("Back to start!");
            race.backToStart();
            performRace();
        }
        catch(RaceException e) {
            // may not happen since we have another exception class 
            // that inherits from RaceException
        }
        
    }
    
    public int computeProfits(String playerName) {
        Racer winner = race.computeWinner();
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
    
    public String toString() {
        String result = race.toString();
        for(Bet bet : placedBets) {
            result += System.lineSeparator();
            result += bet;
        }
        return result;
    }
    
    public IllegalRace<T> getRace() {
        return race;
    }
    
    public List<Bet> getBets() {
        return Collections.unmodifiableList(placedBets);
    }
    
    public double getQuote() {
        return quote;
    }
}
