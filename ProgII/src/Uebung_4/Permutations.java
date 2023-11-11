package Uebung_4;

public class Permutations {

    public static void main(String[] args) {
        permute(2);
    } 

    public static void permute(int n) {
        permute("", n);
    }
    
    // L�sung Aufgabe 4-3-i
    private static void permute(String prefix, int letters) {
        System.out.printf("permute(\"%s\", %d)%n", prefix, letters);
    	int left = letters - prefix.length();
        
        if (left == 0) {
            //System.out.println(prefix);
            return;
        }
        
        for (int i = 0; i < letters; i++) {
            char nextChar = (char)('a' + i);
            if(!prefix.contains(nextChar + ""))
                permute(prefix + nextChar, letters);
        }
    }
    
    // L�sung Aufgabe 4-3-ii
    public static void permute(String prefix, int letters, int digits) {
        
        if (prefix.length() == digits) {
            System.out.println(prefix);
            return;
        }
        
        for (int i = 0; i < letters; i++) {
            char nextChar = (char)('a' + i);
            if(!prefix.contains(nextChar + ""))
                permute(prefix + nextChar, letters, digits);
        }
    }


}
