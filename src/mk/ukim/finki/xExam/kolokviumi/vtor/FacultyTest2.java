package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class FacultyTest2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty2 faculty = new Faculty2();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.equals("end")) break;
            String[] parts = line.split("\\s++");
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
            } else if (parts[0].equals("printStudentReport")) { //printStudentReport
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

class Faculty2 {
    Map<String, List<StudentOnCourse>> studentsByCourse;
    Map<String, List<StudentOnCourse>> coursesByStudent;

    public Faculty2() {
        this.studentsByCourse = new HashMap<>();
        this.coursesByStudent = new HashMap<>();
    }

    public void addInfo(String courseId, String studentId, int totalPoints) {
        StudentOnCourse student = new StudentOnCourse(studentId, courseId, totalPoints);

        studentsByCourse.putIfAbsent(courseId, new ArrayList<>());
        studentsByCourse.get(courseId).add(student);

        coursesByStudent.putIfAbsent(studentId, new ArrayList<>());
        coursesByStudent.get(studentId).add(student);
    }

    private Comparator<StudentOnCourse> getComparator(String comparator, boolean descending) {
        Comparator<StudentOnCourse> comparator1;
        if (comparator.equalsIgnoreCase("byid")) comparator1 = Comparator.comparing(StudentOnCourse::getStudentId);
        else comparator1 = Comparator.comparing(StudentOnCourse::getGrade).thenComparing(StudentOnCourse::getPoints).thenComparing(StudentOnCourse::getStudentId);

        if (descending) return comparator1.reversed();

        return comparator1;

    }

    public void printCourseReport(String courseId, String comparator, boolean descending) {

        Comparator<StudentOnCourse> comp = getComparator(comparator, descending);

        studentsByCourse.get(courseId).stream()
                .sorted(comp)
                .forEach(s -> System.out.println(s.getStudentReport()));

    }

    public void printStudentReport(String studentId) {
        coursesByStudent.get(studentId).stream()
                .sorted(Comparator.comparing(StudentOnCourse::getCourseId))
                .forEach(s -> System.out.println(s.getCourseReport()));
    }

    public Map<Integer, Integer> gradeDistribution(String courseId) {
        return studentsByCourse.get(courseId).stream()
                .collect(Collectors.groupingBy(StudentOnCourse::getGrade, HashMap::new, Collectors.summingInt(t -> 1)));
    }
}

class StudentOnCourse {
    private String studentId;
    private String courseId;
    private int points;

    public StudentOnCourse(String studentId, String courseId, int points) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.points = points;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getPoints() {
        return points;
    }

    public int getGrade() {
        if (points / 10 < 5) return 5;

        return points/10 + 1;
    }

    public String getStudentReport() {
        return String.format("%s %d (%d)", studentId, getPoints(), getGrade());
    }

    public String getCourseReport() {
        return String.format("%s %d (%d)", courseId, points, getGrade());
    }
}
