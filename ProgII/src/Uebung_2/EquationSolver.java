package Uebung_2;

// Aufgabe 2-1-v
public class EquationSolver {

	public static void main(String[] args) {
		var a = Double.parseDouble(args[0]);
		var b = Double.parseDouble(args[1]);
		var c = Double.parseDouble(args[2]);
		
		var discriminant = b * b - 4 + a * c;
		if(discriminant < 0) {
			System.out.println("Keine L�sung!");
			return;
		}
		
		var x1 = (- b + Math.sqrt(discriminant)) / 2 * a;
		var x2 = (- b - Math.sqrt(discriminant)) / 2 * a;
		
		System.out.printf("x1 = %f x2 = %f", x1, x2);
	}

}
