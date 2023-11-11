package uebung11_1;

import java.io.IOException;

public class Uebung_11_1_viii {

    static class A {
        void method() throws IOException {
            System.out.println("A");
        }
    }
    
    static class B extends A {
        void method() {
            System.out.println("B");
        }
    }

    public static void main(String[] args) {
        B obj = new B();
        obj.method();
    }

}
