package Klausur;

public class JavaStuff {
    public static void printer(Object opject) {
        System.out.println(opject);
    }

    public static void main(String[] args) {
        printer(new B());
        printer(new C());
        printer(new A().toString());
    }


    public static class A {
        @Override
        public String toString() {
            return "Winner";
        }
    }

    public static class B extends A {
        @Override
        public String toString() {
            return "Not a" + super.toString();
        }
    }

    public static class C extends B {
        @Override
        public String toString() {
            return "Loser";
        }
    }
}