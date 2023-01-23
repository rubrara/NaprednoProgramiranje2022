package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class NamesTest2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names2 names = new Names2();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}


class Names2 {

    Map<String, Name2> names;

    public Names2() {
        this.names = new HashMap<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name, new Name2(name));

        names.computeIfPresent(name, (k, v) -> {
            v.incOcc();
            return v;
        });
    }

    public void printN(int n) {
        this.names.values().stream()
                .filter(name -> name.getOccurrences() >= n)
                .sorted(Comparator.comparing(Name2::getName2))
                .forEach(System.out::println);

    }

    public String findName(int len, int x) {
        List<Name2> names2 = this.names.values().stream()
                .filter(n -> n.getName2().length() < len)
                .sorted(Comparator.comparing(Name2::getName2))
                .collect(Collectors.toList());

        while (x >= names2.size()) {
            x = x - names2.size();
        }

        return names2.get(x).getName2();
    }
}

class Name2 {
    private String name;
    private int occurrences;

    public Name2(String name) {
        this.name = name;
        this.occurrences = 0;
    }

    public void incOcc() {
        this.occurrences++;
    }

    public String getName2() {
        return name;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public int uniqueLetters() {
        Set<Character> uniqueL = new HashSet<>();

        char[] array = name.toLowerCase().toCharArray();

        for (Character c : array) {
            uniqueL.add(c);
        }

        return uniqueL.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, occurrences, uniqueLetters());
    }
}
