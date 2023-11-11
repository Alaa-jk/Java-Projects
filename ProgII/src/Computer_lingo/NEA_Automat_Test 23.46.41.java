package Computer_lingo;
/*
 * Computer lingustic
 * Non-deterministic finite-state Automat
 * Erstellt durch:
 * Alaa Khleif ---- Mat-Nr: 26930
 * Anas Lasri ---- Mat-Nr: 26572
 * Abdelghafour Kaouch ---- Mat-Nr: 25504
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NEA_Automat_Test {
    public static void main(String[] args) throws IOException {


        NEA_Automat automat = new NEA_Automat();
        System.out.println("Geben Sie einen String von {0, 1} ein!!");
        BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
        String s = eingabe.readLine();
        while (s != null) {

            automat.restart(); // Begin von zustand {E} = 1<<0 in bitwise
            automat.transition(s);
            if (automat.akzeptiert()) System.out.println("Akzeptiert: " + s + "\n");
            else if (automat.verworffen()) System.out.println("Verworffen: " + s + "\n");
            System.out.println("Geben Sie erneuet einen String von {0, 1} ein!!");
            s = eingabe.readLine();
        }
    }
}


class NEA_Automat {

    /*
     * Tupel Q = {A,B,C,D,E,F}, start Zustand s = A, End Zustand F = {E},
     */

    private int Q;

    public void restart() {
        Q = 1 << 0; // Start state, s = A
    }

    static private final int[][] delta = {{1 << 0 | 1 << 1, 1 << 0}, {1 << 2, 0}, {1 << 4 | 1 << 3, 1 << 3}, {1 << 2, 1 << 2}, {1 << 5, 0}, {1 << 5, 1 << 5}};
 /*
 *                                       {{00000001| 00000010, 00000001}
                                        , {00000100, 00000000}
                                        , {00010000 | 00001000, 00001000}
                                        , {00001000, 00000100}
                                        , {00100000, 00000000}
                                        , {001000000, 00100000}};*/
    // delta[A,0] = {A,B}
    // delta[A,1] = {A}.
    // delta[B,0] = {C}
    // delta[B,1] = {} - 1 nicht gelesen in zustand B.
    // delta[F,0] = {C}
    // delta[F,1] = {C}
    // delta[D,0] = {E}
    // delta[D,1] = {} - 1 nicht gelesen in zustand D.
    // delta[C,0] = {D,F}
    // delta[C,1] = {F}
    // delta[E,0] = {E}
    // delta[E,1] = {E}

    public void transition(String in) {
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            int nextSS = 0; // nächste zustand, initialiesier mit 0(Start State)
            for (int j = 0; j <= 5; j++) {
                if ((Q & (1 << j)) != 0) {
                    try {
                        nextSS |= delta[j][c - '0'];
                        System.out.printf("C ist %s Q ist %d delta[%d] nextSS %d \n",c, Q, delta[j][c-'0'], nextSS);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // in effect, nextSS |= 0 - no more transitions can be made...
                    }
                }
            }
            Q = nextSS; // new state after c
        }

    }

    public boolean akzeptiert() {
        System.out.println("Q ist " +Q);
        return (Q & (1 << 5)) != 0; // Wahr wenn das wort in Endzustand ist {E}
    }

    public boolean verworffen() {
        return (Q & (1 << 5)) == 0; // Falsch wenn das wort nicht in Endzustand ist{E}
    }
}
/*
Eingabebeispiel:
100001
10001
111110000
000011110000
 */
