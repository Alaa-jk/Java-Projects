package Uebung_9;

import java.util.ArrayList;
import java.util.List;

/**
 * Musterlösung Aufgabe 9-2.
 * 
 * @author Sven Karol
 *
 */
public class RoachRace {
    
    /**
     * Aufgabe 9-2.ii
     */
    private String name;
    
    /**
     * Aufgabe 9-2.ii
     */
    private List<Cockroach> participants;
    
    /**
     * Aufgabe 9-2.ii
     */
    private int distance;
    
    /**
     * Aufgabe 9-2.iii
     */
    public RoachRace(String name, int distance) {
        this.name = name;
        this.distance = distance;
        participants = new ArrayList<Cockroach>();
    }
    
    /**
     * Aufgabe 9-2.ii
     */
    public String getName() {
        return name;
    }
    
    /**
     * Aufgabe 9-2.ii
     */
    public int getParticipantCount() {
        return participants.size();
    }
    
    /**
     * Aufgabe 9-2.iv
     */
    public void addCockroach(Cockroach participant) {
        participants.add(participant);
    }
    
    /**
     * Aufgabe 9-2.v
     */
    public Cockroach removeCockroach(String name) {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).getName().equals(name)) {
                return participants.remove(i);
            }
        }
        return null;
    }
    
    /**
     * Aufgabe 9-2.vi
     */
    public String toString() {
        String result = "Race: " + name + ", Distance: " + distance;
        result += System.lineSeparator();
        result += "Participants: " + getParticipantCount();
        for(Cockroach participant: participants) {
            result += System.lineSeparator();
            result += participant;
        }
        return result;
    }
    
    /**
     * Aufgabe 9-2.vii
     */
    public Cockroach computeWinner() {
        for(Cockroach participant:participants) {
            if(participant.getPassedDistance() >= distance) {
                return participant;
            }
        }
        return null;
    }
    
    /**
     * Aufgabe 9-2.viii
     */
    public void goForward() {
        for(Cockroach participant: participants) {
            participant.run();
        }
    }
    
    /**
     * Aufgabe 9-2.ix
     */
    public void performRace() {
        Cockroach winner = computeWinner();
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
