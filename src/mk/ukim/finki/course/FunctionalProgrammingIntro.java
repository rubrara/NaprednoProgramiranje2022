package mk.ukim.finki.course;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionalProgrammingIntro {

    public static void main(String[] args) {

        //predicate -> враќа boolean;  се користат во filter() операторот од stream();
        Predicate<Integer> evenPredicate = num -> num % 2 == 0;

        System.out.println(evenPredicate.test(6));
        System.out.println(evenPredicate.test(7));

        Predicate<String> stringPredicate = str -> str.startsWith("NP");

        System.out.println(stringPredicate.test("Napredno prog 2022"));
        System.out.println(stringPredicate.test("NP 2022"));


        //function ->
        Function<String, Integer> length = String::length;
        Function<String, Integer> countLowerCaseLetters = str -> {
            int counter = 0;

            for (char c : str.toCharArray()) {
                if (Character.isLowerCase(c)) ++counter;
            }
            return counter;
        };

        System.out.println(countLowerCaseLetters.apply("Napredno Programiranje"));


        //supplier -> не прима нешто, а враќа резултат
        Supplier<String> stringSupplier = () -> "NP123";

        System.out.println(stringSupplier.get());


        //consumer -> прима аргументи ама не враќа ништо, се користи често за печатење.
        Consumer<String> stringConsumer = str -> System.out.printf("Consumed the str: %s", str);

        stringConsumer.accept("NP!@#!@1234\n");

        Consumer<Integer> integerConsumer = number -> System.out.println(number+1);

        int number = 5;

        integerConsumer.accept(number);
        System.out.println(number);
    }


}
