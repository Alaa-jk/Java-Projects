package Uebung_3;

//L�sung Aufgabe 3-2-ii
public class IntegerSequence {

    public static void main(String[] args) {
        int[] sequence = new int[] { 1, 2, 2, 3, 3, 3, 2, 1, 1, 1, 1, 3, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 5, 6, 6,
                1, 2, 2, 2, 2, 3 };

        int plateauStart = -1;
        int plateauLength = 0;

        for (int i = 1; i < sequence.length; i++) {
            if (sequence[i - 1] < sequence[i]) {
                int currentStart = i;
                for (int j = i + 1; j < sequence.length; j++) {
                    if (sequence[j] != sequence[currentStart]) {
                        int currentLength = j - currentStart;
                        if (sequence[j] > sequence[currentStart] && currentLength > plateauLength) {
                            plateauStart = currentStart;
                            plateauLength = currentLength;
                        }
                        i = j - 1; // Das erste ungleiche Zeichen bricht die Sequenz ab, k�nnte aber schon Teil der
                             // n�chsten Sequenz sein.
                        break;
                    }

                }
            }
        }
        System.out.printf("Plateau beginnt am Index %d und hat die L�nge %d.%n", plateauStart, plateauLength);
        for (int i = plateauStart - 1; i <= plateauStart + plateauLength; i++) {
            System.out.print(sequence[i] + " ");
        }
        System.out.println();
    }

}
