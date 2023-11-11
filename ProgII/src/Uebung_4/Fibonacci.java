package Uebung_4;

//L�sung Aufgabe 4-2-i/ii
public class Fibonacci {
    
    private static long[] cache = new long[92];
    
    public static long fib(int n) {
        if(n == 0)
            return 0;
        if(n == 1)
            return 1;
        if(cache[n] == 0)
            cache[n] = fib(n - 1) + fib(n - 2);
        return cache[n];
    }
    
}
