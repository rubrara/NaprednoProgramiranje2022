package mk.ukim.finki.course;

import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {

        Optional<String> optional = Optional.empty();

        optional.ifPresent(System.out::println);

    }
}
