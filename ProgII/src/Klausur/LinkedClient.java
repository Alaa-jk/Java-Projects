package Klausur;

public class LinkedClient{
    public static void main(String[] args) {
        LinkedRAStack stack = new LinkedRAStack();
        String name = new String();
        stack.push("Alaa");
        stack.push("Khleif");
        stack.push("Nazeeh");
        stack.push("Discord");
        stack.push("Senior");
        stack.push("Rayaa");

        System.out.println(stack.isEmpty());
        System.out.println(stack.size());
        //stack.display();
        //System.out.println(stack.elementAt(2));
        System.out.println(stack.removeAt(2));
        //stack.display();



        }
    }

