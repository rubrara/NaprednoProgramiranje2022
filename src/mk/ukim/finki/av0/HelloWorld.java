package mk.ukim.finki.av0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Sale {

    List<Integer> integers;

    public Sale(List<Integer> integers) {
        this.integers = integers;
    }

    public Sale clone() throws CloneNotSupportedException {
        return new Sale(this.integers);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "integers=" + integers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return integers.equals(sale.integers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(integers);
    }
}

class DiscountSale extends Sale {

    int count;

    public DiscountSale(List<Integer> integers) {
        super(integers);
        this.count = integers.size();
    }

    @Override
    public Sale clone() throws CloneNotSupportedException {
        return new DiscountSale(super.integers);
    }

    @Override
    public String toString() {
        return "DiscountSale{" +
                "integers=" + integers +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountSale that = (DiscountSale) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}


public class HelloWorld {
    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        integers.add(2);
        integers.add(5);
        integers.add(7);
        integers.add(23);

        Sale[] sales = new Sale[2];

        sales[0] = new Sale(integers);
        sales[1] = new DiscountSale(integers);

        Sale[] copySale = new Sale[sales.length];

//         so clone metodite
        for (int i = 0; i < copySale.length; i++) {
            try {
                copySale[i] = sales[i].clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }


////         OVA E BEZ CLONE() METODO
//        for (int i = 0; i < sales.length; i++) {
//
//            // pogore - instanceof VS getClass() - podolu
//
//            if (sales[i] instanceof DiscountSale) copySale[i] = new DiscountSale(sales[i].integers);
//            else copySale[i] = new Sale(sales[i].integers);
//
////            if (sales[i].getClass() == Sale.class)
////            copySale[i] = new Sale(sales[i].integers);
////            else copySale[i] = new DiscountSale(sales[i].integers);
//        }


        Sale sale = sales[0];

        for (int i = 0; i < sales.length; i++) {
            System.out.println(copySale[i] + " -> copied " + copySale[i].getClass());
            System.out.println(sales[i] + " -> original " + sales[i].getClass());
            System.out.println(sales[i] == copySale[i]);
            System.out.println(sales[i].equals(copySale[i]));
            System.out.println("---------");
        }

        System.out.println(sale == sales[0]);
        System.out.println(sale == copySale[0]);

    }
}
