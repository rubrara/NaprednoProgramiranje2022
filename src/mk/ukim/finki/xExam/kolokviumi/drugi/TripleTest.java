package mk.ukim.finki.xExam.kolokviumi.drugi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Triple<T extends Number> {

    List<T> nums = new ArrayList<>();

    public Triple(T num1, T num2, T num3) {
        nums.add(num1);
        nums.add(num2);
        nums.add(num3);
    }

    double max() {
        return nums.stream().mapToDouble(Number::doubleValue).max().orElse(0.00);
    }

    double average() {
        return nums.stream().mapToDouble(Number::doubleValue).average().orElse(0.00);
    }

    void sort() {
        this.nums = nums.stream().sorted(Comparator.comparing(Number::doubleValue)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        nums.forEach(n -> sb.append(String.format("%.2f ", n.doubleValue())));

        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}


