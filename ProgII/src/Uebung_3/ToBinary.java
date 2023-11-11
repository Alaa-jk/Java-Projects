package uebung3_1;

import java.util.Scanner;

//Lösung Aufgabe 3-1-iii
public class ToBinary {

    public static void main(String[] args) {
        int value = new Scanner(System.in).nextInt();
        String result = "";
        while(value > 0) {
            String next = value % 2 == 1 ? "1" : "0";
            result = next + result;
            value = value / 2;
        }
        System.out.println(result);
    }
     // Beispiel:
     //     19 / 2 / 2 / 2 / 2 / 2
     //Wert      9   4   2   1   0
     //Rest      1   1   0   0   1      Ergebnis => 1 0 0 1 1 
     //Stelle   2^0 2^1 2^2 2^3 2^4
}  
