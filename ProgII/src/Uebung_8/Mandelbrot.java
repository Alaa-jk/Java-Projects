package Uebung_8;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Musterlösung 8_2.ii
public class Mandelbrot {
	
	/**
	 * Verallgemeinerte Variante von toColor, die bis zur gegebenen Grenze prüft, ob es sich um
	 * eine Mandelbrot-Zahl handelt. 
	 * 
	 * @param c die zu prüfende Zahl
	 * @param max die maximale Anzahl von Iterationen bzw. Folgengliedern zn
	 * @return die Anzahl gelaufener Interation zwischen 0 und max
	 */
    public static int toMandelbrotIndex(Complex c, int max) {      
        Complex zn = new Complex(0.0, 0.0);       
        for (int i = 0; i < max; i++) {
            zn = (zn.mult(zn)).add(c);
            if (zn.abs() > 2.0)
                return i;
        }        
        return max;
    }
    
	/**
	 * Entscheidet, ob eine gegebene komplexe Zahl eine Mandelbrotzahl ist und 
	 * gibt die schwarze Farbe zurück, wenn das der Fall ist und ansonsten die
	 * weiße Farbe.
	 * 
	 * @param c die zu prüfende komplexe Zahl
	 * @return Einen Farbwert: Schwarz oder Weiß in Abhängigkeit ob c eine Mandelbrotzahl ist oder nicht.
	 */
    public static Color toColorBlackWhite(Complex c) {
        int index = toMandelbrotIndex(c, 255);
        if(index >= 255)
        	return Color.BLACK;
        else
        	return Color.WHITE;
    }

    /**
     * Mandelbrot in Graustufen.
     */
    public static Color toColorGrayscale(Complex c) {
        int index = toMandelbrotIndex(c, 255);
        return grayscaleFromIndex(index);
    }
    
    public static Color grayscaleFromIndex(int index) {
        return new Color(255 - index, 255 - index, 255 - index);
    }
    
    /**
     * Mandelbrot aus unserer Datei mit 256 Farbwerten.
     */
    public static Color toColorMap(Complex c) throws IOException {
    	if(colorMap == null) {
    		colorMap = readColorMap(".\\files\\colors.txt");
    	}
    	int index = toMandelbrotIndex(c, colorMap.length - 1);
        return colorMap[index];
    } 
    
    private static Color[] colorMap = null;
   
    /**
     * Funktion liest die Farbpalette aus der gegebenen Datei.
     */
    private static Color[] readColorMap(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        String[] contents = Files.readString(path).split("\r\n|\n");
        Color[] colorMap = new Color[contents.length];

        for(int i = 0; i < contents.length; i++) {
            String[] codes = contents[i].split(" ");
            int r = Integer.parseInt(codes[0]);
            int g = Integer.parseInt(codes[1]);
            int b = Integer.parseInt(codes[2]);
            colorMap[i] = new Color(r, g, b);
        }

        return colorMap;
    }
	
}
