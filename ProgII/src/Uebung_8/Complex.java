package Uebung_8;

/**
 * Ein Datentyp für komplexe Zahlen.
 * 
 * @author Sven Karol
 *
 */
public class Complex {
    
	// Real- und Imaginärteil im kartesischen Format
    private double real;
    private double imaginary;
    
    /**
     * Instantiiert eine neue komplexe Zahl.
     * 
     * @param r der Wert des Realteils
     * @param i das Vielfache des Imaginärteils
     */
    public Complex(double r, double i) {
        this.real = r;
        this.imaginary = i;
    }
    
    /**
     * Addiert zu der aktuellen komplexen Zahl die komplex Zahl b.
     * 
     * @param b der zweite Summand für die Addition
     * @return Die Summe als neue, komplexe Zahl
     */
    public Complex add(Complex b) {
        double real = this.real + b.real;
        double imaginary = this.imaginary + b.imaginary;
        return new Complex(real, imaginary);
    }
    
    /**
     * Multipliziert diese komplexe Zahl mit der komplexen Zahl b.
     * 
     * @param b mit der zu multiplizierende Zahl
     * @return Das Produkt als neue, komplexe Zahl
     */
    public Complex mult(Complex b) {
        double real = 
                this.real * b.real - this.imaginary * b.imaginary;
        double imaginary = 
                this.real * b.imaginary +  this.imaginary * b.real;
        return new Complex(real, imaginary);
    }
    
    /**
     * Berechnet den Betrag der komplexen Zahl.
     * 
     * @return Betrag der komplexen als Fließkommazahl
     */
    public double abs() {
        double sum = real * real + imaginary * imaginary;
        return Math.sqrt(sum);
    }
    
    // Teillösung 8.1.iv Subtraktion
    public Complex minus(Complex b) {
        double real = this.real - b.real;
        double imaginary = this.imaginary - b.imaginary;
        return new Complex(real, imaginary);
    }
    
    // Teillösung 8.1.iv konjugiert Komplex
    public Complex conjugate() {
        return new Complex(real, imaginary * -1);
    }
    
    // Teillösung 8.1.iv Division
    public Complex div(Complex b) {
        Complex conB = b.conjugate();
        Complex denominator = conB.mult(b); // (a + bi) * (a - bi) Nenner ==> i eliminiert
        Complex numerator = this.mult(conB); // Zähler
        return new Complex(numerator.real / denominator.real, numerator.imaginary / denominator.real);
    }
    
    // Teillösung 8.1.iv Potenzfunktion
    public Complex pow(int d) {
        int absD = d < 0 ? -1 * d : d;
        Complex result = new Complex(1, 0);
        for(int i = 0; i < absD; i++) {
            result = result.mult(this);
        }
        if(d < 0)
            return new Complex(1,0).div(result);
        else
            return result;
    }
    
    // Teillösung 8.1.iv Berechnung Phasenwinkel
    public double phase() {
        double phase = Math.atan2(real, imaginary);
        return phase;
    }
    
    // Musterlösung 8-1.ii
    public String toString() {
    	
    	// Prüfung, ob eine ganze Zahl im Realteil vorliegt und Erstellung des Strings für den Realteil
    	String realString = real == (long)real 
                                ? String.format("%d",(long)real) 
                                : String.format("%s", real);
    	
    	// Wenn kein Imaginärteil vorliegt, dann nur den real-String zurück geben
        if(imaginary == 0.0 || imaginary == -0.0)
            return realString;
        
        // Berechnung des Betrags des Imaginärteils (nötig, um das Vorzeichen zu behandeln)
        double absImg = Math.abs(imaginary);
        
        // Prüfung ob der Betrag des Imaginärteils eine ganze Zahl ist und Erstellung eines Strings für den Imaginärteil
        String imgString = absImg == (long)absImg 
                ? String.format("%d",(long)absImg) 
                : String.format("%s", absImg);
        
        // String für das Vorzeichen erstellen
        String operand = Math.signum(imaginary) == 1 ? " + " : " - "; 
        
        // Gesamtstring aus Real- und Imaginärteil
        return realString + operand + imgString + "i";
    }
    
    public double im() {
    	return imaginary;
    }
    
    public double re() {
    	return real;
    }
    
}
