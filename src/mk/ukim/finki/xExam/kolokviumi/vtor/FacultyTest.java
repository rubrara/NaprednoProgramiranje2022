package mk.ukim.finki.xExam.kolokviumi.vtor;

import mk.ukim.finki.av5.grades.Student;

import java.util.*;
import java.util.stream.Collectors;

class StudentOnCourse {
    private String studentId;
    private int totalPoints;
    private String courseId;

    public StudentOnCourse(String courseId, String studentId, int totalPoints) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.totalPoints = totalPoints;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    int getGrade() {
        int grade = totalPoints / 10 + 1;

        if (grade < 5) {
            grade = 5;
        }

        if (grade > 10) {
            grade = 10;
        }
        return grade;
    }

    @Override
    public String toString() {
        return String.format("%s %d (%d)", getStudentId(), getTotalPoints(), getGrade());
    }

    public String reportWithCourse() {
        return String.format("%s %d (%d)\n", getCourseId(), getTotalPoints(), getGrade());
    }
}

class Faculty {
    Map<String, List<StudentOnCourse>> studentsByCourse = new HashMap<>();
    Map<String, List<StudentOnCourse>> coursesByStudent = new HashMap<>();

    public Faculty() {
    }

    void addInfo(String courseId, String studentId, int totalPoints) {
        StudentOnCourse student = new StudentOnCourse(courseId, studentId, totalPoints);

        studentsByCourse.putIfAbsent(courseId, new ArrayList<>());
        coursesByStudent.putIfAbsent(studentId, new ArrayList<>());

        studentsByCourse.get(courseId).add(student);
        coursesByStudent.get(studentId).add(student);
    }

    void printCourseReport(String courseId, String comparatorType, Boolean descending) {
        Comparator<StudentOnCourse> comparator = getComparator(comparatorType, descending);

        studentsByCourse.get(courseId).stream().sorted(comparator).forEach(System.out::println);
    }

    void printStudentReport(String studentId) {
        coursesByStudent.get(studentId)
                .stream()
                .sorted(Comparator.comparing(StudentOnCourse::getCourseId))
                .forEach(StudentOnCourse::reportWithCourse);
    }

    Map<Integer, Integer> gradeDistribution(String courseId) {

        return studentsByCourse.get(courseId).stream()
                .map(StudentOnCourse::getGrade)
                .collect(Collectors.groupingBy(grade -> grade, TreeMap::new, Collectors.summingInt(grade -> 1)));

    }


    private Comparator<StudentOnCourse> getComparator(String type, Boolean descending) {
        Comparator<StudentOnCourse> comp;

        if (type.equalsIgnoreCase("byId")) comp = Comparator.comparing(StudentOnCourse::getStudentId);
        else comp = Comparator.comparing(StudentOnCourse::getGrade)
                .thenComparing(StudentOnCourse::getTotalPoints)
                .thenComparing(StudentOnCourse::getStudentId);

        if (descending) return comp.reversed();

        return comp;

    }

}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.equals(".")) break;
            String[] parts = line.split("\\s+");
            if (parts[0].equals("addInfo")) {
                String courseId = parts[1];
                String studentId = parts[2];
                int totalPoints = Integer.parseInt(parts[3]);
                faculty.addInfo(courseId, studentId, totalPoints);
            } else if (parts[0].equals("printCourseReport")) {
                String courseId = parts[1];
                String comparator = parts[2];
                boolean descending = Boolean.parseBoolean(parts[3]);
                faculty.printCourseReport(courseId, comparator, descending);
            } else if (parts[0].equals("printStudentReport")) {
                String studentId = parts[1];
                faculty.printStudentReport(studentId);
            } else {
                String courseId = parts[1];
                Map<Integer, Integer> grades = faculty.gradeDistribution(courseId);
                grades.forEach((key, value) -> System.out.println(String.format("%2d -> %3d", key, value)));
            }
        }
    }

}

