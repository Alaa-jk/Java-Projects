package Klausur;

public interface RAStack<T>{
    public void push(T item);
    public T pop();
    public T top();
    public boolean isEmpty();
    public int size();
    public T elementAt(int index);
    public T removeAt(int index);
}
