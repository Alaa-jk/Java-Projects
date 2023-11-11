package Uebung_8;

public class ComplexTest {
    public static void main(String[] args) {


        Komplex a = new Komplex();
        Komplex b = new Komplex();
        Komplex c = new Komplex();
        Komplex d = new Komplex();
        a.setKomplex(4, 2);
        b.setKomplex(3, 4);


        c = a.add(b);
        System.out.println("c = " + c);
        c = c.mult(a);
        System.out.println("c = " + c);


        d = a.sub(b);
        System.out.println("d = " + d);
        d = d.mult(d);

        System.out.println("d = " + d);
    }
}


