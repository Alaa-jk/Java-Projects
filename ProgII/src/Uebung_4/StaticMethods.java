package Uebung_4;

public class StaticMethods {

	// L�sung Aufgabe 4-1-i
    public static boolean majority(boolean a, boolean b, boolean c) {
        if(a && b) return true;
        if(a && c) return true;
        if(b && c) return true;
        return false;
    }
    
    // L�sung Aufgabe 4-1-ii
    public static boolean areTriangular(double a, double b, double c) {
        return (a < b + c) && (b < a + c) && (c < a + b);
    }
    
    // L�sung Aufgabe 4-1-iii
    public static double sqrt(final double a) {
        double xn = a;
        while((xn - a / xn) > xn * 1E-15) {
            xn = (xn + a / xn) * 0.5;
        }
        return xn;
    }
    
    // L�sung Aufgabe 4-1-iv
    public static double min(double[] a) {
        if(a.length == 0) {
            return Double.NaN;
        }
        double min = a[0];
        for(int i = 1; i < a.length; i++) {
            if(min > a[i]) {
                min = a[i];
            }
        }
        return min;
    }
    
    public static double max(double[] a) {
        if(a.length == 0) {
            return Double.NaN;
        }
        double max = a[0];
        for(int i = 1; i < a.length; i++) {
            if(max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }
    
    // L�sung Aufgabe 4-1-v
    public static void normalize(double[] a) {
        if(a.length == 0) {
            return;
        }
        double min = min(a);
        double max = max(a);
        double diff = max - min;
        for(int i = 0; i < a.length; i++) {
            a[i] = (a[i] - min) / diff;
        }
    }
    
    // L�sung Aufgabe 4-1-vi
    public static int[] histogram(int[] a, int r) {
        int[] result = new int[r];
        for(int i = 0; i < a.length; i++) {
            result[a[i]]++;
        }
        return result;
    }
    
}
