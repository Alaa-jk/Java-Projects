package Klausur;

public class HalloWelt {
    public static void main(String[] args) {
        String result = foo("vhiaelll oewreflotlig!!");
        System.out.println(result);
        result.toUpperCase();

    }
    public static String foo(String a){
        if (a.length() > 2) {
            String b = a.substring(0,2);
            String c = a.substring(2, a.length());
            return foo(b) + foo(c);
        }
        return a.charAt(0)+ "";
    }
}
