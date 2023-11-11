package uebung11_1;

public class Uebung_11_1_vii {
    
    private static String value = "";
    
    public static void main(String[] args) {
        try {
            value += "1";
            throw new Exception("Foo");
        }
        catch(Exception e) {
            value += "2";
        }
        finally {
            value += 3;
            foo();
            value += 4;
        }
    }

    private static String foo() {
        throw new UnsupportedOperationException();
    }

}
