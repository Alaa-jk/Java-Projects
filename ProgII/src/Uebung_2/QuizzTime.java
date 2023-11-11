package Uebung_2;

public class QuizzTime {
	// Aufgabe 2-2-d
	// Erkl�rung: j++ hat hier keinen Effekt!
	// - j += j++ entspricht j = j + j++
	// - in der ersten Iteration j = 0
	// - j = 0 + 0, was sich dann auch weiter fortsetzt
    public static void main(String[] args) {
        int i, j;
        for (i = 0, j = 0; i < 10; i++) {j += j++; };
    }

}
