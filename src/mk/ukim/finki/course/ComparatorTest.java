package mk.ukim.finki.course;

import java.util.Comparator;

class Student implements Comparable<Student> {
    String name;
    int index;
    int year;

    public Student(String name, int index, int year) {
        this.name = name;
        this.index = index;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.year, o.year);
    }
}

public class ComparatorTest {
    public static void main(String[] args) {

        Comparator<Student> byIndexNumber = (l, r) -> Integer.compare(l.index, r.index);
        Comparator<Student> buNameComparator = (l, r) -> l.name.compareTo(r.name);

        Comparator<Student> byAgeAndIndexNumber = (l, r) -> {
            int res = Integer.compare(l.year, r.year);
            if (res == 0) {
                return Integer.compare(l.index, r.index);
            } else {
                return res;
            }
        };

        Comparator<Student> byAgeAndIndexNumber2 = Comparator.comparing(Student::getYear).thenComparing(Student::getIndex);

    }
}
