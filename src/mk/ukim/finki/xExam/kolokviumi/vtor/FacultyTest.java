package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student {
    private String id;
    private int yearOfStudies;
    private Map<Integer, Map<String, Integer>> gradesByCourseAndTerm;
    List<Integer> gradesList;
    TreeSet<String> courseNames;
    private int coursesPassed;

    public Student(String id, int yearOfStudies) {
        this.id = id;
        this.yearOfStudies = yearOfStudies;
        gradesByCourseAndTerm = new TreeMap<>();
        IntStream.range(1, yearOfStudies * 2 + 1).forEach(i -> gradesByCourseAndTerm.put(i, new HashMap<>()));
        this.coursesPassed = 0;
        this.gradesList = new ArrayList<>();
        this.courseNames = new TreeSet<>();
    }

    public String getId() {
        return id;
    }

    public int getYearOfStudies() {
        return yearOfStudies;
    }

    public int getCoursesPassed() {
        return coursesPassed;
    }

    public Map<Integer, Map<String, Integer>> getGradesByCourseAndTerm() {
        return gradesByCourseAndTerm;
    }

    public boolean isGraduated() {
        return coursesPassed == yearOfStudies * 2 * 3;
    }

    public double getAverageGrade() {
        return gradesList.stream().mapToInt(i -> i).average().orElse(5.0);
    }

    public String getGraduationLog() {
        return String.format("Student with ID %s graduated with average grade %.2f in %d years.", id, getAverageGrade(), yearOfStudies);
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        if (!gradesByCourseAndTerm.containsKey(term))
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, this.id));
        if (gradesByCourseAndTerm.get(term).size() == 3)
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", this.id, term));
        else {
            gradesByCourseAndTerm.get(term).put(courseName, grade);
            ++coursesPassed;
            gradesList.add(grade);
            courseNames.add(courseName);
        }
    }

    public String getDetailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s\n", id));
        gradesByCourseAndTerm.forEach((key, value) -> {
            int term = key;
            int numOfCourses = value.size();
            double avgGradeForTerm = value.values().stream().mapToInt(i -> i).average().orElse(5.0);
            sb.append(String.format("Term %d\n", term));
            sb.append(String.format("Courses: %d\n", numOfCourses));
            sb.append(String.format("Average grade for term: %.2f\n", avgGradeForTerm));
        });

        sb.append(String.format("Average grade: %.2f\n", getAverageGrade()));
        sb.append(String.format("Courses attended: %s", String.join(",", courseNames)));

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f", id, coursesPassed, getAverageGrade());
    }
}

class Course {
    String courseName;
    IntSummaryStatistics statistics;

    public Course(String courseName) {
        this.courseName = courseName;
        this.statistics = new IntSummaryStatistics();
    }

    public void addGrade(int grade) {
        this.statistics.accept(grade);
    }

    public String getCourseName() {
        return courseName;
    }

    public int getNumOfStudents() {
        return (int) statistics.getCount();
    }

    public double getAverageCourseGrade() {
        return statistics.getAverage();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", getCourseName(), getNumOfStudents(), getAverageCourseGrade());
    }
}


class Faculty {

    Map<String, Student> studentsById;
    List<String> logs;
    Map<String, Course> coursesByName;


    public Faculty() {
        this.studentsById = new HashMap<>();
        this.logs = new ArrayList<>();
        this.coursesByName = new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        Student student = new Student(id, yearsOfStudies);
        studentsById.putIfAbsent(id, student);
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student = studentsById.get(studentId);
        student.addGrade(term, courseName, grade);
        coursesByName.putIfAbsent(courseName, new Course(courseName));
        coursesByName.get(courseName).addGrade(grade);

        if (student.isGraduated()) {
            logs.add(student.getGraduationLog());
            studentsById.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return studentsById.get(id).getDetailedReport();
    }

    void printFirstNStudents(int n) {
        List<Student> studentList = new ArrayList<>(studentsById.values());

        studentList.stream().sorted(Comparator.comparing(Student::getCoursesPassed).thenComparing(Student::getAverageGrade).thenComparing(Student::getId).reversed()).limit(n).forEach(System.out::println);
    }

    void printCourses() {
        //[course_name] [count_of_students] [average_grade]
        Comparator<Course> comparator = Comparator.comparing(Course::getNumOfStudents).thenComparing(Course::getAverageCourseGrade).thenComparing(Course::getCourseName);

        Set<Course> sortedCourses = new TreeSet<>(comparator);

        sortedCourses.addAll(coursesByName.values());
        sortedCourses.forEach(System.out::println);

    }
}


public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
