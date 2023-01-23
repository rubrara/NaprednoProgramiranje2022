package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Audition {

    Map<String, Set<Participant>> participantsByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
    }

    void addParticpant(String city, String code, String name, int age) {

        participantsByCity.putIfAbsent(city, new HashSet<>());
        participantsByCity.computeIfPresent(city, (k, v) -> {
            v.add(new Participant(city, code, name, age));
            return v;
        });
    }

    void listByCity(String city) {

        participantsByCity.get(city).stream()
                .sorted(Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode))
                .forEach(System.out::println);
    }
}

class Participant {
    private String city;
    private String code;
    private String name;
    private int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format(code + " " + name + " " + age);
    }
}