package mk.ukim.finki.xExam.kolokviumi.drugi;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

interface NumberProcessor<T extends Number, R> {
    R compute(ArrayList<T> numbers);
}

class Numbers<T extends Number> {

    ArrayList<T> numberList;

    public Numbers(ArrayList<T> numberList) {
        this.numberList = numberList;
    }

    void process(NumberProcessor<T, ?> processor) {
        System.out.println(processor.compute(numberList));
    }
}

public class NumberProcessorTest {

    public static void main(String[] args) {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        ArrayList<Double> doubleArrayList = new ArrayList<>();

        int countOfIntegers;
        Scanner sc = new Scanner(System.in);

        countOfIntegers = sc.nextInt();
        while (countOfIntegers > 0) {
            integerArrayList.add(sc.nextInt());
            --countOfIntegers;
        }


        int countOfDoubles;
        countOfDoubles = sc.nextInt();
        while (countOfDoubles > 0) {
            doubleArrayList.add(sc.nextDouble());
            --countOfDoubles;
        }

        Numbers<Integer> integerNumbers = new Numbers<>(integerArrayList);

        NumberProcessor<Integer, Long> firstProcessor = numbers -> numbers.stream().filter(num -> num < 0).count();
        System.out.println("RESULTS FROM THE FIRST NUMBER PROCESSOR");
        integerNumbers.process(firstProcessor);

        NumberProcessor<Integer, String> secondProcessor = new NumberProcessor<Integer, String>() {
            @Override
            public String compute(ArrayList<Integer> numbers) {
                IntSummaryStatistics sumStats = numbers.stream().mapToInt(i -> i).summaryStatistics();

                return String.format("Count: %d Min: %.2d Average: %.2f Max: %.2d", sumStats.getCount(), sumStats.getMin(), sumStats.getAverage(), sumStats.getMax());
            }
        };
        System.out.println("RESULTS FROM THE SECOND NUMBER PROCESSOR");
        integerNumbers.process(secondProcessor);

        Numbers<Double> doubleNumbers = new Numbers<>(doubleArrayList);

        NumberProcessor<Double, List<Double>>thirdProcessor = numbers -> numbers.stream().sorted().collect(Collectors.toList());

        NumberProcessor<Double, List<Double>> thirdWithAnonymousClass = new NumberProcessor<Double, List<Double>>() {
            @Override
            public List<Double> compute(ArrayList<Double> numbers) {
                return numbers.stream().sorted().collect(Collectors.toList());
            }
        };

        System.out.println("RESULTS FROM THE THIRD NUMBER PROCESSOR");
        doubleNumbers.process(thirdProcessor);

        NumberProcessor<Double, Double> fourthProcessor = numbers -> {

            List<Double> sorted = thirdProcessor.compute(numbers);

            int count = sorted.size();
            if (count % 2 != 0) return sorted.get((count-1)/2);
            else {
                Double num1 = sorted.get(count / 2);
                Double num2 = sorted.get((count / 2) - 1);

                return (num1 + num2) / 2.0;
            }
        };
        System.out.println("RESULTS FROM THE FOURTH NUMBER PROCESSOR");
        doubleNumbers.process(fourthProcessor);


    }

}
