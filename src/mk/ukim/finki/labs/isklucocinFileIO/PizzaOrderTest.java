package mk.ukim.finki.labs.isklucocinFileIO;

import java.util.Scanner;

class InvalidExtraTypeException extends Exception {
}

class InvalidPizzaTypeException extends Exception {
}

class OrderLockedException extends Exception {
}

class ItemOutOfStockException extends Exception {
}

class ArrayIndexOutOfBoundsException extends Exception {
    public ArrayIndexOutOfBoundsException(int idx) {
    }
}

class EmptyOrder extends Exception{
}

interface Item {
    int getPrice();

    String getType();
}

//////////////
class ExtraItem implements Item {
    private final String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Coke") || type.equals("Ketchup"))
            this.type = type;
        else throw new InvalidExtraTypeException();
    }

    @Override
    public int getPrice() {
        if (this.type.equals("Coke")) return 5;
        else return 3;
    }

    @Override
    public String getType() {
        return this.type;
    }
}


/////////////
class PizzaItem implements Item {
    private final String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian"))
            this.type = type;
        else throw new InvalidPizzaTypeException();
    }

    @Override
    public int getPrice() {
        if (this.type.equals("Standard")) return 10;
        else if (this.type.equals("Pepperoni")) return 12;
        else return 8;
    }

    @Override
    public String getType() {
        return this.type;
    }
}

class Order {
    private Item[] items;
    private int[] counts;
    private boolean isLocked;

    public Order() {
        items = new Item[0];
        counts = new int[0];
        isLocked = false;
    }

    public void addItem(Item item, int count) throws OrderLockedException, ItemOutOfStockException {

        if (isLocked) throw new OrderLockedException();
        if (count > 10) throw new ItemOutOfStockException();

        if (alreadyInOrder(item) == -1) {
            Item[] tmpI = new Item[items.length + 1];
            int[] tmpC = new int[counts.length + 1];

            for (int i = 0; i < items.length; i++) {
                tmpC[i] = counts[i];
                tmpI[i] = items[i];
            }

            tmpI[items.length] = item;
            tmpC[counts.length] = count;

            items = tmpI;
            counts = tmpC;
        } else {
            int index = alreadyInOrder(item);
            counts[index] = count;
        }
    }

    public int getPrice() {
        int price = 0;
        for (int i = 0; i < items.length; i++) {
            price += getItemFullPrice(i);
        }

        return price;
    }

    private int getItemFullPrice(int index) {
        return items[index].getPrice() * counts[index];
    }

    public int alreadyInOrder(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getType().equals(item.getType())) return i;
        }

        return -1;
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", i + 1, items[i].getType(), counts[i], getItemFullPrice(i)));
        }

        sb.append(String.format("%-22s%5d$", "Total:", this.getPrice()));

        System.out.println(sb.toString());
    }

    public void removeItem(int idx) throws OrderLockedException, ArrayIndexOutOfBoundsException {
        if (isLocked) throw new OrderLockedException();
        if (idx < 0 || idx > counts.length) throw new ArrayIndexOutOfBoundsException(idx);

        Item[] tmpI = new Item[items.length - 1];
        int[] tmpC = new int[counts.length - 1];

        for (int i = 0; i < idx; i++) {
            tmpC[i] = counts[i];
            tmpI[i] = items[i];
        }

        for (int i = idx; i < items.length - 1; i++) {
            tmpC[i] = counts[i + 1];
            tmpI[i] = items[i + 1];
        }

        items = tmpI;
        counts = tmpC;
    }

    public void lock() throws EmptyOrder {
        if (items.length > 0) this.isLocked = true;
        else throw new EmptyOrder();
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}