package uebung10_3_ii;

public class Bet {
    
    private String participantName;
    private String playerName;
    private int amount;
    
    public Bet(String roachName, String playerName, int amount) {
        this.amount = amount;
        this.participantName = roachName;
        this.playerName = playerName;
    }
    
    public String getParticipantName() {
        return participantName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAmount() {
        return amount;
    }
    
    public String toString() {
        return playerName + " bets " + amount + " on " + participantName;
    }
}
