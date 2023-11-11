package Klausur;

public class SrangeWorld {
    private static int result = 0 ;

    public static void main(String[] args) {
        one();
        tow();
        three();
        System.out.println(result);

    }
    public static void one(){
        try {

            tow();
        }
        catch (Exception e){
            result ++;
        }
    }
    public static void tow(){

        if (result == 0) {
            
            throw new IllegalArgumentException("So nicht!");
        }
        try {
            System.out.println("result ist "+result);
            result = result * 22/ result;
        }
        finally {
            result --;
        }

    }
    public static void three(){
        result *=2;
        int result = 17;
    }
}
