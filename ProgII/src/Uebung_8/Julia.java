package Uebung_8;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Musterlösung 8-3.ii
public class Julia {
	
    public static int toJuliaIndex(Complex zn, Complex c, int max) {          
        for (int i = 0; i < max; i++) {
            zn = (zn.mult(zn)).add(c);
            if (zn.abs() > 2.0)
                return i;
        }        
        return max;
    }
    
    public static Color toColorGrayscale(Complex z0, Complex c) {
        int index = toJuliaIndex(z0, c, 255);
        return new Color(255 - index, 255 - index, 255 - index);
    }
    
    public static Color toColorMap(Complex z0, Complex c, Color[] colorMap) {
        int index = toJuliaIndex(z0, c, colorMap.length - 1);
        return colorMap[index];
    }
    
    public static Color grayscaleFromIndex(int index) {
        return new Color(255 - index, 255 - index, 255 - index);
    }
    
    public static Color toColorBlackWhite(Complex z0, Complex c) {
        int index = toJuliaIndex(z0, c, 255);
        if(index >= 255)
        	return Color.BLACK;
        else
        	return Color.WHITE;
    }

    
    /**
     * Julia aus unserer Datei mit 256 Farbwerten.
     */
    public static Color toColorMap(Complex z0, Complex c) throws IOException {
    	if(colorMap == null) {
    		colorMap = readColorMap(".\\files\\colors.txt");
    	}
    	int index = toJuliaIndex(z0, c, colorMap.length - 1);
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
