package Uebung_2;

// Aufgabe 2-1-iii
public class NumberPrinter {

    public static void main(String[] args) {
        for(int i = 3000; i <= 4000; i++) {
            System.out.printf("%d  ", i);
            if((i - 2999) % 9 == 0) 
                System.out.println();
            
        }

    }

}
