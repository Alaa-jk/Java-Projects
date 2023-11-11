package Uebung_2;

public class Sum {

	// L�sung 2-2-iii-d
	// Die einzige richtige Implemetierung. Die anderen Varianten
	// rechnen andere bzw. falsche Ergebnisse aus.
    public static void main(String[] args) {
        int n = 1000000;
        double sum = 0.0;
        for (int i = 1; i <= n; i++) sum += 1 / (1.0*i*i);
        System.out.println(sum);
    }

}
