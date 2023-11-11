package uebung11_2_i_ii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

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
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please provide a valid name!");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Please provide a distance larger than 0!");
        }
        this.name = name;
        this.distance = distance;
        participants = new ArrayList<T>();
    }

    public void addParticipant(T participant) {
        participants.add(participant);
    }

    public T removeParticipant(String name) {
        if (name == null) {
            throw new NullPointerException("Name must not be null");
        }
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).getName().equals(name)) {
                return participants.remove(i);
            }
        }
        throw new NoSuchElementException("Name " + name + " not found!");
    }
    
    public String toSerialString() {
        String result = "Race:" + name + "," + distance;
        result += System.lineSeparator();
        for (Racer participant : participants) {
            result += System.lineSeparator();
            result += participant;
        }
        return result;
    }
    
    public String toString() {
        String result = "Race: " + name + ", Distance: " + distance;
        result += System.lineSeparator();
        result += "Participants: " + getParticipantCount();
        for (Racer participant : participants) {
            result += System.lineSeparator();
            result += participant;
        }
        return result;
    }

    public T computeWinner() {
        for (T participant : participants) {
            if (participant.getPassedDistance() >= distance) {
                return participant;
            }
        }
        return null;
    }

    public void goForward() {
        for (T participant : participants) {
            participant.run();
        }
    }

    private Random rand = new Random(23);

    public void performRace() throws RaceException {

        double p = rand.nextDouble();
        // In some few cases we have an accident with n participants
        if (p < 0.02) {
            int count = (int) (rand.nextDouble() * participants.size() + 1);
            List<Racer> causes = new LinkedList<Racer>();
            List<Racer> left = new LinkedList<Racer>(participants);
            while (causes.size() < count) {
                Racer selected = left.get((int) (rand.nextDouble() * left.size()));
                causes.add(selected);
                left.remove(selected);
            }
            throw new AccidentException(causes.toArray(new Racer[0]));
        }

        // In some few cases we have a rule violation
        if (p < 0.05) {
            throw new RuleViolationException(participants.get((int) (rand.nextDouble() * participants.size())));
        }

        T winner = computeWinner();
        while (winner == null) {
            goForward();
            winner = computeWinner();
        }
    }

    public void backToStart() {
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).backToStart();
        }
    }
    
    public List<Racer> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
    
    public int getDistance() {
        return distance;
    }
}
