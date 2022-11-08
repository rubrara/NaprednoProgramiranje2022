package mk.ukim.finki.av5.functional;

import java.util.Random;
import java.util.function.*;

public class FunctionalInterfacesTest {

    // Predicate -> filter(), findFirst(), allMatch().....
    // zema argument i vrajca true/false

    Predicate<Integer> LessThanTen = new Predicate<Integer>() {
        @Override
        public boolean test(Integer integer) {
            return integer < 10;
        }
    };

    Predicate<Integer> getLessThanTen = num -> num < 10;



    // Supplier ->
    // daj rezultat za nazad ama ne zemaj nisto

    Supplier<Integer> IntegerSupplier = new Supplier<Integer>() {
        @Override
        public Integer get() {
            return new Random().nextInt(1000);
        }
    };

    Supplier<Integer> getIntegerSupplier = () -> new Random().nextInt(1000);


    // Consumer
    Consumer<Integer> IntegerConsumer = new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) {
            System.out.println(integer +  2);
        }
    };

    Consumer<Integer> getIntegerConsumer = num -> System.out.println(num + 2);


    // Function   y = 5x
    Function<Integer, String> AddFiveToNumberAndFormat = new Function<Integer, String>() {
        @Override
        public String apply(Integer integer) {
            return String.format("%d\n", integer+5);
        }
    };

    Function<Integer, String> getAddFiveToNumberAndFormat = num -> String.format("%d\n", num + 5);


    // BiFunction  y = x + z
    BiFunction<Integer, Integer, String> SumNumbersAndFormat = new BiFunction<Integer, Integer, String>() {
        @Override
        public String apply(Integer integer, Integer integer2) {
            return String.format("%d + %d = %d", integer, integer2, integer + integer2);
        }
    };

    BiFunction<Integer, Integer, String> getSumNumbersAndFormat = (num1, num2) -> String.format("%d + %d = %d", num1, num2, num1 + num2);


}
