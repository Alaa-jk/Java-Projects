package Uebung_8;

/**
 * TestClient für Uebung 8_1
 * 
 * @author Sven Karol
 */
public class TestClient {

    public static void main(String[] args) {
    	testComplex();
    	System.out.println();
    	testSolverComplex();
    	System.out.println();
    	testSolverReal();
    }
    
    public static void testComplex() {
        Complex x = new Complex(2.2, 3.0);
        Complex y = new Complex(5.0, -4.0);
        System.out.printf("Wert von x = %s%n", x);
        System.out.printf("Wert von y = %s%n", y);
        System.out.printf("y * x = %s%n", x.mult(y));
        System.out.printf("x / y = %s%n", x.div(y));
        System.out.printf("x ^ 4 = %s%n", x.pow(4));
        System.out.printf("x ^ 2 ^ 2 = %s%n", x.pow(2).pow(2));
        System.out.printf("x ^ 1 = %s%n", x.pow(1));
    }
    
    public static void testSolverComplex() {
        double a = 4, b = 3, c = 2; // komplexe Lösung
        testSolver(a, b, c);
    }
    
    public static void testSolverReal() {
        double a = 2, b = -3, c = 1; // reelle Lösung
        testSolver(a, b, c);
    }
    
    public static void testSolver(double a, double b, double c) {
        Complex[] roots = Solver.findRoots(a, b, c);
        
        System.out.println("Lösung 1: " + roots[0]);
        System.out.println("Lösung 2: " + roots[1]);
        
        Complex t1 = new Complex(a,0).mult(roots[0].mult(roots[0]));
        Complex t2 = new Complex(b,0).mult(roots[0]);
        Complex t3 = new Complex(c,0);
        Complex valueTest =  t1.add(t2).add(t3);
        System.out.println("Ergebnis nach Einsetzen Lösung 1 (Erwartungswert: 0): " + valueTest);
    }
}