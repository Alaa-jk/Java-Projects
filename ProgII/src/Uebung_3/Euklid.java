package Uebung_3;

//Lösung Aufgabe 3-1-ii
public class Euklid {

    public static void main(String[] args) {
        double[] a = { 1.2, -3.2, 5.8 }; 
        double[] b = { 23, 2.2, 9.8 };
        
        double squareSum = 0;
        
        for(int i = 0; i < a.length; i++) {
            squareSum += (a[i] - b[i]) * (a[i] - b[i]); 
        }
        
        System.out.printf("Distanz: %.2f%n", Math.sqrt(squareSum));

    }

}
