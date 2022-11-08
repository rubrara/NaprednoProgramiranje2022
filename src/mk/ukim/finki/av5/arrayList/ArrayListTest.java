package mk.ukim.finki.av5.arrayList;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>(100);
        integerList.add(3);
        integerList.add(5);
        integerList.add(6);
        integerList.add(9);
        integerList.add(11);

        List<String> stringList = new ArrayList<>();
        stringList.add("A");
        stringList.add("BCD");

        System.out.println(integerList);
        System.out.println(stringList);

        System.out.println(integerList.size());

        System.out.println(stringList.get(1));

        System.out.println(integerList.contains(4));
        System.out.println(integerList.contains(5));

        System.out.println(integerList.indexOf(9));

        System.out.println(integerList.removeIf(i -> i%2==0));

        System.out.println(integerList);

    }
}
