package mk.ukim.finki.av0;

import java.util.Objects;

class Person {
    String name;
    int age;

    public Person() {
        this.name = "Kostadin";
        this.age = 25;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

public class PersonTest {

    public static void main(String[] args) {
        Person p = new Person("David", 20);
        System.out.println(p);

        Person p1 = new Person("David", 22);

        Person p2 = new Person("David", 22);

//        p2.setAge(254);

        System.out.println(p1);

        System.out.println(p == p2);
        System.out.println(p.name.equals(p2.name));
        System.out.println(p == p2);

    }

}

