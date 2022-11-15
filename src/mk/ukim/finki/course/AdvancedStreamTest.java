package mk.ukim.finki.course;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancedStreamTest {
    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();

        students.add(new Student("Stefan", 151233, 7));
        students.add(new Student("Stefan", 211000, 2));
        students.add(new Student("Stefan", 201030, 3));

        List<Integer> evenIndexesStudents = students.stream()
                .map(Student::getIndex)
                .filter(i -> i %2 == 0)
                .collect(Collectors.toList());

        System.out.println(evenIndexesStudents);

    }
}
