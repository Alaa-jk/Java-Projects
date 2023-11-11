package Uebung_8;

import stdlib.Picture;

import java.awt.*;
import java.util.Random;

public class MandelbrotZoomer {
	
	private static final Random rand = new Random(3288);
    
    public static void main(String[] args) throws InterruptedException {
        double size = 0.001; // der Bereich in dem wir uns bewegen
        int res = 400;       // Auflösung des Zielbildes
        
        Picture picGrayscale = new Picture(res, res);
        final int maxGray = 255;
        double mandelFraction = 0;
        
        Complex start = interestingMandelbrot();
        
        // Wir zoomen solange in das Bild, bis wir einen Ausschnitt mit Belegung > 0.4 gefunden
        // haben. Wenn so ein Bereich nicht gefunden wird, dann ist die Annahme, dass die Belegung
        // irgendwann gegen 0 läuft, so dass wir spätestens dann abbrechen.
        do {
            // Untere Grenze y-Achse Definitionsbereich Mandelbrot
            final double yLow  = start.im() - size / 2;
            // Linke Grenze x-Achse Definitionsbereich Mandelbrot
            final double xLeft = start.re() - size / 2;
        	int mandelCount = plotMandelbrot(picGrayscale, xLeft, yLow, size, maxGray);
           
            // Anteil der schwarzen Pixel am Gesamtbild
            mandelFraction = (double)mandelCount / (res * res);
            size = size - size * 0.01; // Bereich verkleinern für mögliche nächste Iteration
            picGrayscale.show();
            Thread.sleep(10);
        }
        while( mandelFraction > 0 && mandelFraction < 0.4);
    }
    
  
    public static Complex interestingMandelbrot() {
        int mandelIndex = -1;
        double yCenter = 0;  // Mittelpunkt: x
        double xCenter = 0;  // Mittelpunkt: y
        
        Complex result = null;
        
        // Suchen einer interessanten Mandelbrot Zahl im Übergang zwischen Mandelbrot und nicht Mandelbrot
        while(mandelIndex < 100 || mandelIndex > 150) {
            xCenter = -1.0 + rand.nextDouble() * 2.0;
            yCenter = -1.0 + rand.nextDouble() * 2.0;
            result = new Complex(xCenter, yCenter);
            mandelIndex = Mandelbrot.toMandelbrotIndex(result, 155);
        }
        
        return result;
    }
    
    public static int plotMandelbrot(Picture pic, double xLeft, double yLow, double size, final int maxGray) {
    	final int res = pic.width(); 
        
        // Schrittgröße im Definitionsbereich
        final double stepSize = size / res;
        
        // Anzahl bestimmter Pixel mit Mandelbrotzahlen
        int mandelCount = 0;
        
        // Von (0,0) bis (799,799) "abrastern"
        for (int i = 0; i < res; i++) {
        	// ein Schritt in y-Richtung
            double y = yLow +  i * stepSize; // 0 .. size
            
            for (int j = 0; j < res; j++) {
                
            	// ein Schritt in x-Richtung
            	double x = xLeft + j * stepSize; // 0 .. size
                
            	// komplexe Testzahl im Mandelbrot-Definitionsbereich anlegen
                Complex toTest = new Complex(x, y);
                
                // anhand der Mandelbrotfolge bestimmen
                int index =  Mandelbrot.toMandelbrotIndex(toTest, maxGray);
                Color color  = Mandelbrot.grayscaleFromIndex(index);
                
                // Zählen der Pixel die die Schleife komplett durchlaufen haben
                if(maxGray == index) 
                    mandelCount++; 

                // erhaltene Farbe im Bildbereich setzen
                int col = j;
                int row = res - 1 - i;
                pic.set(col, row, color); // 0,0 -> links oben
            }
        }
        
        return mandelCount;
    }

}
