package Uebung_2;

// Aufgabe 2-1-i
public class TestIfInteger {

    public static void main(String[] args) {
       // Hinweis: die Pr�fung der Eingabe �bernimmt die
       // Funktion parseInt, die eine Exception erzeugt,
       // wenn sie kein Integer erzeugen kann
       int a = Integer.parseInt(args[0]);
       int b = Integer.parseInt(args[1]);
       int c = Integer.parseInt(args[2]);

       if(a == b && b == c) {
           System.out.println("equal");
       }
       else {
           System.out.println("not equal");
       }
    }

}
