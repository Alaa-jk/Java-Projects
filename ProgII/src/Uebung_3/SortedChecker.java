package uebung3_1;

//Lösung Aufgabe 3-1-i
public class SortedChecker {

    public static void main(String[] args) {
        int[] a = new int[args.length];
        for(int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        
        for(int i = 0; i < a.length - 1; i++) {
            if( a[i] > a[i + 1]) {
                System.out.println("nicht sortiert");
                return;
            }
        }
        
        System.out.println("sortiert");
    }

}
