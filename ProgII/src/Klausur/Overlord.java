package Klausur;

public class Overlord {
    private int monsters;

    public Overlord(int monsters) {
        this.monsters = monsters;
    }

    public static void main(String[] args) throws Exception {
        Overlord overlord = new Overlord(0);
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();
        overlord.carryOneMore();

        System.out.println(overlord.toString());

    }


    public void carryOneMore() throws Exception {
        if(monsters <= 8){
            monsters++;
        }
        else {
            throw new Exception("Cannot carry more ");
        }
    }


    public String toString() {
        return "I am the Overlord, carrying " + monsters + " monsters.";
    }
}
