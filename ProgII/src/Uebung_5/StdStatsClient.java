package Uebung_5;

// L�sung Aufgabe 5_2
public class StdStatsClient {

    public static void main(String[] args) {
        int flipsPerTrail = 100;
        int trials = 10000000;
        int[] flips = flipHeadCount(flipsPerTrail, trials);
        double[] normalized = new double[flipsPerTrail + 1]; 
        
        for(int i = 0; i <= flipsPerTrail; i++) {
            normalized[i] = (double)flips[i] / (double)trials;
        }
        
        StdStats.plotLines(normalized);
        
    }
    
    public static int[] flipHeadCount(int flips, int trials) {
        int[] result = new int[flips + 1]; 
        for(int i = 0; i < trials; i++) {
            int count = 0;
            for(int j = 0; j < flips; j++) {
                if(Math.random() > 0.5) {
                    count++;
                }
            }
            result[count]++;
        }
        return result;
    }

}
