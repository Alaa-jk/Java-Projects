package Uebung_3;

import java.util.Arrays;

//Lösung Aufgabe 3-1-iv
public class ArrayCopy {

    public static void main(String[] args) {
        int[][] a = { { 1, 2, 3}, {4 , 5}, {7, 8, 9} };
        int[][] b = new int[a.length][];
        
        for(int i = 0; i < a.length; i++) {
            b[i] = new int[a[i].length];
            for(int j = 0; j < a[i].length; j++) {
                b[i][j] = a[i][j];
            }
        }
        
        System.out.println(Arrays.deepEquals(a, b));
    }

}
