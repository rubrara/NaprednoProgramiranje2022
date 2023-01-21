package mk.ukim.finki.xExam.kolokviumi.vtor.lab;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

class SuperString {

    private LinkedList<String> list;
    private LinkedList<Integer> undo_stack;

    public SuperString () {
        list = new LinkedList<String>();
        undo_stack = new LinkedList<Integer>();
    }

    public void append(String s) {
        list.addLast(s);
        undo_stack.addFirst(1);
    }

    public void insert(String s) {
        list.addFirst(s);
        undo_stack.addFirst(-1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( String s : list )	sb.append(s);
        return sb.toString();
    }

    public boolean contains(String s){
        return toString().contains(s);
    }

    public void reverse() {
        this.list = this.list.stream().map(SuperString::reverseString).collect(Collectors.toCollection(LinkedList::new));
        Collections.reverse(list);
        Collections.reverse(undo_stack);
    }

    public void removeLast(int k) {
        for (int i = 0; i < k; i++) {
            if (undo_stack.getLast() == 1) {
                this.list.removeFirst();
                undo_stack.removeLast();
            } else if (undo_stack.getLast() == -1) {
                this.list.removeLast();
                undo_stack.removeLast();
            }
        }
    }

    private static String reverseString(String line) {
        StringBuilder result = new StringBuilder();
        for (char c : line.toCharArray()) {
            result.insert(0, c);
        }
        return result.toString();
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {
                    s.append(jin.next());
                }
                if ( command == 1 ) {
                    s.insert(jin.next());
                }
                if ( command == 2 ) {
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {
                    s.reverse();
                }
                if ( command == 4 ) {
                    System.out.println(s);
                }
                if ( command == 5 ) {
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {
                    break;
                }
            }
        }
    }
}

