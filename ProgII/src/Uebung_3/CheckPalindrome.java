package Uebung_3;

//L?sung Aufgabe 3-1-v
public class CheckPalindrome {

    public static void main(String[] args) {
        String value = args[0];

        for (int i = 0; i < value.length() / 2; i++) {
            if (value.charAt(i) != value.charAt(value.length() - 1 - i)) {
                System.out.printf("'%s' ist kein Palindrom.%n", value);
                return;
            }
        }
        
        System.out.printf("'%s' ist ein Palindrom.%n", value);
    }

}
