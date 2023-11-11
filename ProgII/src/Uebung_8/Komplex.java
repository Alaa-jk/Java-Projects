package Uebung_8;

public class Komplex {
    private double re;

    private double im;


    public boolean setKomplex(double real, double imaginaer) {
        re = real;
        im = imaginaer;

        return true;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public String toString() {
        return "" + re + " + " + im + " i";
    }


    public Komplex add(Komplex a) {
        double x, y;
        x = re + a.getRe();
        y = im + a.getIm();

        Komplex b = new Komplex();
        b.setKomplex(x, y);

        return b;
    }

    public Komplex sub(Komplex a) {
        double x, y;
        x = re - a.getRe();
        y = im - a.getIm();

        Komplex b = new Komplex();
        b.setKomplex(x, y);

        return b;
    }

    public Komplex mult(Komplex a) {
        double x, y;
        x = re * a.getRe() - im * a.getIm();
        y = re * a.getIm() + im * a.getRe();

        Komplex b = new Komplex();
        b.setKomplex(x, y);

        return b;
    }


    public Komplex div(Komplex a) {
        double x, y;
        x = (re * a.getRe() + im * a.getIm())
                / (a.getRe() * a.getRe() + a.getIm() * a.getIm());
        y = (im * a.getRe() - re * a.getIm())
                / (a.getRe() * a.getRe() + a.getIm() * a.getIm());

        Komplex b = new Komplex();
        b.setKomplex(x, y);

        return b;
    }
}

