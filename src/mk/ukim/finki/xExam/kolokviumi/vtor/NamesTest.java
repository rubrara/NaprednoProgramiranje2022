package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
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

class Names {

    Map<String, Name> names;

    public Names() {
        this.names = new HashMap<>();
    }

    public void addName(String name) {
        this.names.putIfAbsent(name, new Name(name));
        this.names.computeIfPresent(name, (k, v) -> {
            v.incrementOccurrences();
            return v;
        });
    }

    public void printN(int n) {
        names.values().stream().filter(v -> v.getOccurrences() >= n).sorted(Comparator.comparing(Name::getName)).forEach(System.out::println);
    }

    public String findName(int len, int x) {

        List<Name> namesSelected = names.values().stream()
                .filter(n -> n.getName().length() < len)
                .sorted(Comparator.comparing(Name::getName))
                .collect(Collectors.toList());

        while (x >= namesSelected.size()) {
            x = x - namesSelected.size();
        }
        return namesSelected.get(x).getName();
    }
}

class Name {
    private String name;
    private int occurrences;


    Name(String name) {
        this.name = name;
        this.occurrences = 0;
    }

    public void incrementOccurrences() {
        this.occurrences += 1;
    }

    public String getName() {
        return name;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public int uniqueLetters() {
        Set<Character> letters = new HashSet<>();

        for (Character c : name.toLowerCase().toCharArray()) {
            letters.add(c);
        }

        return letters.size();
    }


    @Override
    public String toString() {
        return String.format("%s (%d) %d", this.getName(), this.getOccurrences(), this.uniqueLetters());
    }
}
