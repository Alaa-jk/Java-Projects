package uebung10_3_ii;

public class IllegalRaceTest {

    public static void main(String[] args) {
       Cockroach theDonald = new Cockroach("Donald", Species.Periplaneta_Americana, 50);
       theDonald.run();
       System.out.println(theDonald);
       theDonald.run();
       System.out.println(theDonald);
       theDonald.backToStart();
       
       Cockroach theBarack = new Cockroach("Barack", Species.Periplaneta_Americana, 51);
       Cockroach theJoe = new Cockroach("Joe", Species.Periplaneta_Americana, 49);
       Cockroach laMerkel = new Cockroach("Angela", Species.Blattella_Germanica, 50);
       
       IllegalRace<Cockroach> race = new IllegalRace<Cockroach>("2020", 20000);
       race.addParticipant(theBarack);
       race.addParticipant(theDonald);
       race.addParticipant(theJoe);
       race.addParticipant(laMerkel);
       
       System.out.println(race);
       race.performRace();
       System.out.println(race);
       
       System.out.println(race.computeWinner());
       race.backToStart();
       
       BettingOffice office = new BettingOffice(race, 1.2);
       office.placeBet("Joe", 1000, "Sven");
       office.placeBet("Donald", 100000, "Ivanka");
       office.placeBet("Angela",500, "Sven");
       
       office.performRace();
       System.out.println(office);
       
       System.out.println(office.computeProfits("Sven"));
       
    }

}
