package Uebung_9;

public class Bet {
    
    private String roachName;
    private String playerName;
    private int amount;
    
    public Bet(String roachName, String playerName, int amount) {
        this.amount = amount;
        this.roachName = roachName;
        this.playerName = playerName;
    }
    
    public String getRoachName() {
        return roachName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAmount() {
        return amount;
    }
    
    public String toString() {
        return playerName + " bets " + amount + " on " + roachName;
    }
}
