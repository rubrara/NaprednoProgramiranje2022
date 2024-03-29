package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private String category;
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;
    private int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = 0;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getPrice() {
        return this.price;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold += quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {

    Map<String, List<Product>> products;
    Map<String, Product> productsById;

    OnlineShop() {
        this.products = new HashMap<>();
        this.productsById = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product p = new Product(category, id, name, createdAt, price);
        this.products.putIfAbsent(category, new ArrayList<>());
        this.products.computeIfPresent(category, (k, v) -> {
            v.add(p);
            return v;
        });
        this.productsById.putIfAbsent(id, p);

    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if (productsById.get(id) == null)
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));

        Product product = productsById.get(id);
        product.setQuantitySold(quantity);
        return product.getPrice() * quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();

        List<Product> productsByCategory = products.get(category) != null ? products.get(category)
                : new ArrayList<>(productsById.values());




        Comparator<Product> comparator = getComparator(comparatorType);

        productsByCategory.sort(comparator);

        for (int i = 0; i < productsByCategory.size(); i+=pageSize) {
            result.add(productsByCategory.subList(i, Math.min(i + pageSize, productsByCategory.size())));
        }
        return result;
    }


    private Comparator<Product> getComparator(COMPARATOR_TYPE type) {
        Comparator<Product> comparator;

        if (type.equals(COMPARATOR_TYPE.OLDEST_FIRST))
            comparator = Comparator.comparing(Product::getCreatedAt);
        else if (type.equals(COMPARATOR_TYPE.NEWEST_FIRST))
            comparator = Comparator.comparing(Product::getCreatedAt).reversed();
        else if (type.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST))
            comparator = Comparator.comparing(Product::getPrice);
        else if (type.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST))
            comparator = Comparator.comparing(Product::getPrice).reversed();
        else if (type.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST))
            comparator = Comparator.comparing(Product::getQuantitySold).reversed();
        else comparator = Comparator.comparing(Product::getQuantitySold);
        return comparator;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


