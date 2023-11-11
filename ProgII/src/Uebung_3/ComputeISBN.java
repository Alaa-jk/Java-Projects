package Uebung_3;

import java.util.Scanner;

//Lösung Aufgabe 3-2-i
public class ComputeISBN {

    public static void main(String[] args) {        
        //String baseString = "074755120";
        String baseString = new Scanner(System.in).next();
        if(baseString.length() != 9) {
            System.out.printf("Die eingegebene Zahl %d Stellen.%n", baseString.length());
        }
        long sum = 0;
        for(int i = 0, j = 10; i < 9; i++, j--) {
            System.out.printf("%c * %d%n", baseString.charAt(i), j);
            sum += Integer.parseInt(baseString.charAt(i) + "") * j;
        }
        System.out.printf("Summe: %d Checksum: %d %n", sum, (11 - sum % 11) % 11);
        long d1 = (11 - sum % 11) % 11;
        
        String ISBN = baseString + (d1 == 10 ? "X" : d1);
        System.out.printf("Die berechnete ISBN-Nummer lautet: %s%n", ISBN);
        
    }

}
