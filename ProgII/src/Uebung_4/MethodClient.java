package Uebung_4;

public class MethodClient {

    public static void main(String[] args) {
        System.out.printf("Math: %f Static Lib: %f%n", Math.sqrt(2), StaticMethods.sqrt(2));
        
        double[] a1 = new double[10];
        for(int i = 0; i < a1.length; i++) {
            a1[i] = Math.random() * 30;
        }
        
        StaticMethods.normalize(a1);
        
        printArray(a1);
        
        int[] a2 = new int[100];
        for(int i = 0; i < a2.length; i++) {
            a2[i] = (int)Math.round(Math.random() * 99);
        }
        
        printArray(StaticMethods.histogram(a2, 100));
        
    }
    
    public static void printArray(int[] a) {
        for(int i = 0; i < a.length; i++) {
            System.out.print(i + " : ");
            System.out.println(a[i]);
        }
    }
    
    public static void printArray(double[] a) {
        for(int i = 0; i < a.length; i++) {
            System.out.print(i + " : ");
            System.out.println(a[i]);
        }
    }

}
