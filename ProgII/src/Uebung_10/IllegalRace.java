package uebung10_3_ii;

import java.util.ArrayList;

/**
 * Durch Einsatz einer generischen Klasse können wir Rennen für beliebige Arten von Teilnehmern
 * nutzen.
 * 
 * @author Sven Karol
 *
 * @param <T> der konkrete Racer-Typ
 */
public class IllegalRace<T extends Racer> {
    
    private String name;
    private ArrayList<T> participants;
    private int distance;   
    
    public String getName() {
        return name;
    }
    
    public int getParticipantCount() {
        return participants.size();
    }
    
    public IllegalRace(String name, int distance) {
        this.name = name;
        this.distance = distance;
        participants = new ArrayList<T>();
    }
    
    public void addParticipant(T participant) {
        participants.add(participant);
    }
    
    public T removeParticipant(String name) {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).getName().equals(name)) {
                return participants.remove(i);
            }
        }
        return null;
    }
    
    public String toString() {
        String result = "Race: " + name + ", Distance: " + distance;
        result += System.lineSeparator();
        result += "Participants: " + getParticipantCount();
        for(Racer participant: participants) {
            result += System.lineSeparator();
            result += participant;
        }
        return result;
    }
    
    public T computeWinner() {
        for(T participant:participants) {
            if(participant.getPassedDistance() >= distance) {
                return participant;
            }
        }
        return null;
    }
    
    public void goForward() {
        for(T participant: participants) {
            participant.run();
        }
    }
    
    public void performRace() {
        T winner = computeWinner();
        while(winner == null) {
            goForward();
            winner = computeWinner();
        }
    }
    
    public void backToStart() {
        for(int i = 0; i < participants.size(); i++) {
            participants.get(i).backToStart();
        }
    }
    
}
