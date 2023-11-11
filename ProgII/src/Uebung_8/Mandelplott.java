package Uebung_8;

import stdlib.Picture;

import java.awt.*;
import java.io.IOException;

// Musterlösung 8_2.ii
public class Mandelplott {

    public static void main(String[] args) throws IOException {
        // Mittelpunkt des betrachteten Definitionsbereichs
    	final double xCenter = Double.parseDouble(args[0]);
        final double yCenter = Double.parseDouble(args[1]);
        
        // Größe Definitionsbereich Mandelbrot: (size x size)  
        final double size = Double.parseDouble(args[2]);
        final int res = Integer.parseInt(args[3]);
        
        // Anlegen eines Picture-Objekts mit (res x res) Pixeln
        Picture pic1 = new Picture(res, res);
        
        plotMandelbrot(pic1, xCenter, yCenter, size, 1);
        
        pic1.show();
        
        // Anlegen eines Picture-Objekts mit (res x res) Pixeln
        Picture pic2 = new Picture(res, res);
        
        plotMandelbrot(pic2, xCenter, yCenter, size, 2);
        
        pic2.show();
    }
    
    public static void plotMandelbrot(Picture pic, double xCenter, double yCenter, double size, int variant) throws IOException {
    	final int res = pic.width(); 
    	
        // Untere Grenze y-Achse Definitionsbereich Mandelbrot
        final double yLow  = yCenter - size / 2;
        // Linke Grenze x-Achse Definitionsbereich Mandelbrot
        final double xLeft = xCenter - size / 2;
        
        // Schrittgröße im Definitionsbereich
        final double stepSize = size / res;
        
        // Von (0,0) bis (799,799) "abrastern"
        for (int i = 0; i < res; i++) {
        	// ein Schritt in y-Richtung
            double y = yLow +  i * stepSize; // 0 .. size
            
            for (int j = 0; j < res; j++) {
                
            	// ein Schritt in x-Richtung
            	double x = xLeft + j * stepSize; // 0 .. size
                
            	// komplexe Testzahl im Mandelbrot-Definitionsbereich anlegen
                Complex toTest = new Complex(x, y);
                
                // Farbe anhand der Mandelbrotfolge bestimmen, und zusätzlich Auswahl zwischen verschieden Farbvarianten
                Color color;
                if(variant == 1) color = Mandelbrot.toColorGrayscale(toTest);
                else if(variant == 2) color = Mandelbrot.toColorMap(toTest);
                else color = Mandelbrot.toColorBlackWhite(toTest);
                
                // erhaltene Farbe im Bildbereich setzen
                int col = j;
                int row = res - 1 - i;
                pic.set(col, row, color); // 0,0 -> links oben
            }
        }
    }

}
