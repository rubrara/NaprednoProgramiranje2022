package mk.ukim.finki.labs.labs1;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Date {
    public int year, month, day;

    public Date(String date) {
        String[] parts = date.split("-");

        this.year = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.day = Integer.parseInt(parts[2]);
    }

    public boolean isNewerComparedTo(Date d) {
        if (year > d.year) return true;
        else if (year < d.year) return false;
        else {
            if (month > d.month) return true;
            else if (month < d.month) return false;
            else {
                return day > d.day;
            }
        }
    }

}

class Faculty {

    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public Student getStudent(long index) {
        return Arrays.stream(students).filter(t -> t.getIndex() == index).findFirst().get();
    }

    public int countStudentsFromCity(String cityName) {
        return (int) Arrays.stream(students).filter(t -> t.getCity().equals(cityName)).count();
    }

    public double getAverageNumberOfContacts() {
        return Arrays.stream(students).mapToDouble(Student::getNumOfContacts).sum() / students.length;
    }

    public Student getStudentWithMostContacts() {
        return Arrays.stream(students).reduce((s1, s2) -> s1.getNumOfContacts() > s2.getNumOfContacts() ? s1 :
                (s1.getNumOfContacts() == s2.getNumOfContacts() ? (s1.getIndex() > s2.getIndex() ? s1 : s2) : s2)).get();
    }

    @Override
    public String toString() {
        return "{" +
                "\"fakultet\":\"" + name + '\"' +
                ", \"studenti\":" + Arrays.toString(students) +
                '}';
    }
}

class Student {

    private ArrayList<Contact> contacts;
    private String firstName, lastName, city;
    private int age;
    private long index;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new ArrayList<>();
    }

    public void addEmailContact(String date, String email) {
        contacts.add(new EmailContact(date, email));
    }

    public double getNumOfContacts() {
        return contacts.size();
    }

    public void addPhoneContact(String date, String phone) {
        contacts.add(new PhoneContact(date, phone));
    }

    public Contact[] getEmailContacts() {
        return contacts.stream().filter(t -> t.getType().equals(ContactType.Email.name())).toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts() {
        return contacts.stream().filter(t -> t.getType().equals(ContactType.Phone.name())).toArray(Contact[]::new);
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        return contacts.stream().reduce((c1, c2) -> c1.isNewerThan(c2) ? c1 : c2).get();
    }

    @Override
    public String toString() {
        return "{" +
                "\"ime\":\"" + firstName + '\"' +
                ", \"prezime\":\"" + lastName + '\"' +
                ", \"vozrast\":" + age +
                ", \"grad\":\"" + city + "\"" +
                ", \"indeks\":" + index +
                ", \"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" + Arrays.toString(getEmailContacts()) +
                '}';
    }
}

enum ContactType {
    Email, Phone
}

enum Operator {
    VIP, ONE, TMOBILE
}

abstract class Contact {

    protected String date;

    Contact(String date) {
        this.date = date;
    }

    public boolean isNewerThan(Contact c) {
        Date d1 = new Date(this.date);
        Date d2 = new Date(c.date);

        return d1.isNewerComparedTo(d2);
    }

    public abstract String getType();

    public abstract String toString();
}

class PhoneContact extends Contact {

    private String phone;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        char op = phone.charAt(2);

        if (op == '0' || op == '1' || op == '2') return Operator.TMOBILE;
        if (op == '5' || op == '6') return  Operator.ONE;
        else return Operator.VIP;
    }

    @Override
    public String getType() {
        return ContactType.Phone.toString();
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", phone);
    }
}

class EmailContact extends Contact {

    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return ContactType.Email.toString();
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", email);
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
