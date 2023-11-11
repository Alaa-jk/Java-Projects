package Klausur;

public class LinkedRAStack <T> implements RAStack<T> {
    private Node first = null;
    private int size = 0;
    private class Node {
        T item;
        Node next;
    }

    /**
     * @param item
     */
    @Override
    public void push(T item) {
        Node oldfirst = first;
        first = new Node();
        first.item= item;
        first.next = oldfirst;
        size++;

    }

    /**
     * @return
     */
    @Override
    public T pop() {
        if (first == null) {
            throw new IllegalStateException("stack is empty");

        }
        T item = first.item;
        first = first.next;
        size--;
        return item;
    }

    /**
     * @return
     */
    @Override
    public T top() {
        if (first == null) {
            throw new IllegalStateException("stack is empty");
        }

        return first.item;

    }


    @Override
    public boolean isEmpty() {
        if (first == null) {
            return true;
        }
        return false;
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public T elementAt(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index is unsuitable");
        }
        Node current = first;
        for (int i = 0; i <= size(); i++) {
            if (i == index){
                return current.item;
            }
            else {
                System.out.println("Suche läuft...");
            }
        }

        return null;
    }


    @Override
    public T removeAt(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index is unsuitable");
        }
        Node previous = new Node();
        Node current = first;
        int i = -1;
        do {
            i++;
            if (i == index) {
                T value = current.item;
                if (value != null) {
                     previous.next= current.next;
                }
                else {

                }
            }
        }while(i <= size);

        return null;
    }
    public void display() {

        Node current = first;
        if(first == null) {
            System.out.println("List is empty");
            return;
        }
        while(current != null) {
            System.out.println(current.item + " ");
            current = current.next;
        }

    }

}

