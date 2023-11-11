package uebung11_1;

public class Uebung_11_1_v {

    public static void main(String[] args) {
       foo();
       System.out.println("Ende.");
    }
    
    public static void foo() {
        bar();
    }
    
    public static void bar() {
        fooBar();
    }
    
    public static void fooBar() {
        throw new UnsupportedOperationException();
    }

}
