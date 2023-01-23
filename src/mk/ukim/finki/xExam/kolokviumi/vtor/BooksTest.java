package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

class BookCollection {
    Map<String, Set<Book>> books;
    Set<Book> forTheCheapestN;

    public BookCollection() {
        this.books = new TreeMap<>();
        this.forTheCheapestN = new HashSet<>();
    }

    public void addBook(Book book) {
        this.books.putIfAbsent(book.getCategory(), new TreeSet<>());

        this.books.get(book.getCategory()).add(book);
        this.forTheCheapestN.add(book);
    }

    public void printByCategory(String category) {
        this.books.get(category).forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {

        if (forTheCheapestN.size() <= n) return forTheCheapestN.stream().sorted(Comparator.comparing(Book::getPrice)).collect(Collectors.toList());

        return forTheCheapestN.stream()
                .sorted(Comparator.comparing(Book::getPrice))
                .limit(n)
                .collect(Collectors.toList());

    }
}

class Book implements Comparable<Book> {
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public int compareTo(Book o) {
        int r = title.compareToIgnoreCase(o.title);
        if (r == 0) return Float.compare(this.price, o.price);

        return r;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}