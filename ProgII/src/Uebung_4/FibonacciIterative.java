package Uebung_4;

//L�sung Aufgabe 4-2-ii als Schleife
public class FibonacciIterative {
    
    public static long fib(int n) {
        long[] cache = new long[n + 1];
        cache[0] = 0;
        cache[1] = 1;
        for(int i = 2; i <= n; i++) {
            cache[i] = cache[i - 1] + cache[i - 2];
        }
        return cache[n];
    }
    
}
