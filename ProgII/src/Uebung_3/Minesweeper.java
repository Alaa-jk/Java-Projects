package uebung3_2;

//Lösung Aufgabe 3-2-iii
public class Minesweeper {

    public static void main(String[] args) {
        int m = 3, n = 5;
        int k = m + 2, l = n + 2; 
        double p = .15;
        boolean[][] playground = new boolean[k][l];
        int[][] scores = new int[m][n];
        for(int i = 1; i < k - 1; i++) {
            for(int j = 1; j < l - 1; j++) {
                playground[i][j] = Math.random() < p ? true : false;
                System.out.print(playground[i][j] ? "* " : ". ");
            }
            System.out.println();
        }
        
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                if(playground[i][j]) {
                    scores[i - 1][j - 1] = -1; // bomb
                }
                else {
                    int count = 0;

                    // explizite Variante der unten stehenden Schleife
                    /*if(playground[i - 1][j - 1]) count++;
                    if(playground[i - 1][j]) count++;
                    if(playground[i - 1][j + 1]) count++;
                    
                    if(playground[i][j - 1]) count++;
                    //if(playground[i][j]) count++;
                    if(playground[i][j + 1]) count++;
                    
                    if(playground[i + 1][j - 1]) count++;
                    if(playground[i + 1][j]) count++;
                    if(playground[i + 1][j + 1]) count++;*/
                    
                    // iterieren über alle nachbarelemente und bomben zählen
                    for(int u = i - 1; u < i + 2; u++) {
                        for(int v = j - 1; v < j + 2; v++) {
                            if(playground[u][v]) {
                                count++;
                            }
                        }
                    }
                    scores[i - 1][j - 1] = count; // neighboring bombs
                }
            }
        }
        
        System.out.println();
        
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                System.out.print(scores[i][j] == -1 ? "* " : scores[i][j] + " ");
            }
            System.out.println();
        }
        
    }

}
