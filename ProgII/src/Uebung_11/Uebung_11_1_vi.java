package uebung11_1;

public class Uebung_11_1_vi {
    public static void main(String[] args) {
        System.out.println("Return value: " + foo());
    }

    static String foo() {
        try {
            int i = 42 / 0;
            return "try";
        } catch (ArithmeticException e) {
            return "catch";
        } finally {
            return "me";
        }
    }
}