package Letcode;

public class PlainDrome {
    public static void main(String[] args) {
        System.out.print(isPlainDrome(121)+ " True");

    }
    public static boolean isPlainDrome(int num){
        int a, c = 0, d= num;
        while (num > 0){
            a = d % 10;
            d = d / 10;
            c = (c * 10) + a;
        }
        if(d == c) return true;
        else return false;
    }
}
