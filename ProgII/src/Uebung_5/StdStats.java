package Uebung_5;

import java.util.Arrays;

// L�sung Aufgabe 5-1
public class StdStats {
    
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
    
    public static double avg(double[] a) {
        if(a.length == 0) {
            return Double.NaN;
        }
        double sum = 0.0;
        for(int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum / a.length;
    }
    
    public static double variance(double[] a) {
        if(a.length <= 1) {
            return Double.NaN;
        }
        double avg = avg(a);
        double sum = 0.0;
        
        for(int i = 0; i < a.length; i++) {
            double diff = a[i] - avg; 
            sum += diff * diff;
        }
        
        return sum / (a.length - 1);
    }
    
    public static double stddev(double[] a) {
        return Math.sqrt(variance(a));
    }
    
    public static double median(double[] a) {
        if(a.length <= 1) {
            return Double.NaN;
        }
        double[] b = new double[a.length];
        System.arraycopy(a, 0, b, 0, a.length);
        Arrays.sort(b);
        if(a.length % 2 != 0) {
            return a[a.length / 2];
        }
        else {
            return (a[a.length / 2] + a[a.length / 2 + 1]) / 2;
        }
    }
    
    public static void plotPoints(double[] a) {
        // von 0 bis length und jeweils eins mehr
        StdDraw.setXscale(-1, a.length);
        StdDraw.setYscale(min(a), max(a));
        // Radius in Abh�ngigkeit von Array-L�nge
        StdDraw.setPenRadius(1.0 / (3.0 * a.length));
        
        for (int i = 0; i < a.length; i++) {
            StdDraw.point(i, a[i]);
        }
    }
    
    public static void plotLines(double[] a) {
        // von 0 bis length und jeweils eins mehr
        StdDraw.setXscale(-1, a.length);
        StdDraw.setYscale(min(a), max(a));
        // Standardradius
        StdDraw.setPenRadius();
        
        for (int i = 1; i < a.length; i++) {
            StdDraw.line(i - 1, a[i-1], i, a[i]);
        }
    }
    
    public static void plotBars(double[] a) {
        // von 0 bis length und jeweils eins mehr
        StdDraw.setXscale(-1, a.length);
        StdDraw.setYscale(min(a), max(a));
        
        for (int i = 0; i < a.length; i++) {
            // Rechteck wird vom Zentrum gezeichnet, daher: a[i]/2
            StdDraw.filledRectangle(i, a[i]/2, 0.25, a[i]/2);
        }
    }
}
