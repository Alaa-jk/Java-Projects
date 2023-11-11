package Uebung_4;

public class Divisor {

    public static void main(String[] args) {
        int p = Integer.parseInt(args[0]);
        int q = Integer.parseInt(args[1]);
        System.out.printf("Gtt von %d und %d ist %d.", p, q, ggt(p, q));
      
        /*System.out.printf("Gtt ist %d.", ggt(8, 36, 16, 18));*/
    }
    
    // L�sung Aufgabe 4-2-iii
    public static int ggt(int p, int q) {
        if(q == 0)
            return p;
        else
            return ggt(q, p % q); 
    }

    // L�sung Aufgabe 4-2-iv
    public static int ggt(int p, int q, int r, int s) {
        return ggt(ggt(p, q), ggt(r, s));
        // return ggt(ggt(ggt(p, q), r), s);
    }
    
}
