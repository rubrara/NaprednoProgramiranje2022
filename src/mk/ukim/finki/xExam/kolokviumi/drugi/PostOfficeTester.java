package mk.ukim.finki.xExam.kolokviumi.drugi;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*/
##Пошта Problem 2 Да се имплементира класа пошта PostOffice во која е чуваат името на поштата, локацијата и листа од пратки.
За пратките да се имплементира апстрактна класа Package во која се чуваат информации за пратка: name (String) за кого е наменета,
address (String), trackingNumber (int) за следење и weight (int) изразена во грамови.

Во системот постојат интернационални пратки (InternationalPackage) за кои дополнително се чува за кој регион се пратени (Africa, Asia, Europe и America)
и локални (LocalPackage) пратки за кои се чува дали пратката е со приоритет или без приоритет.

Цената на интернационалните пратки се наплаќа според масата и тоа по 1.5 долари за грам,
а локалните пратки со приоритет се наплаќаат по 5 долари, а по 3 долари пратките без приоритет.

Во класата PostOffice да се имплементираат следните методи:

void loadPackages(Scanner scanner) –
    метод преку кој се внесуваат пратките во следен формат: тип на пратка (I или L), име, адреса, број, тежина,
    за локалните – true ако е со приритет false ако не е, за интернационалните – регион.
    Доколку записот на пратка не одговара со опишаниот формат (типот на пратката е различна од I и L, или тежината е помала или еднаква на нула)
    се фрла исклучок од класата InvalidPackageException во кој се проследува невалидниот запис.

boolean addPackage(Package p) - за додавање на пакет во листата со пакети

Package mostExpensive() - ја враќа најскапата пратка

void printPackages(OutputStream out) – метод кој ги печати пратките според цената (од највисока кон најниска)
во следен формат:
    L, name, address, number, weight, true/false за приоритет – локална пратка
    I, name, address, number, weight, region – интернационална пратка

 */

class InvalidPackageException extends Exception {

}

class LocalPackage extends Package {

    boolean hasPriority;

    public LocalPackage(String name, String address, int trackingNumber, int weight, boolean hasPriority) {
        super(name, address, trackingNumber, weight);
        this.hasPriority = hasPriority;
    }

    @Override
    double price() {
        if (hasPriority) return 5.0 * weight;
        else return 3.0 * weight;
    }

    @Override
    public String toString() {
        return String.format("L, %s, %s, %d, %d, %s", name, address, trackingNumber, weight, hasPriority);
    }
}

class InternationalPackage extends Package {

    String region;
    static final double PRICE_PER_GRAM = 1.5;


    public InternationalPackage(String name, String address, int trackingNumber, int weight, String region) {
        super(name, address, trackingNumber, weight);
        this.region = region;
    }

    @Override
    double price() {
        return weight * 1.5;
    }


    @Override
    public String toString() {
        return String.format("I, %s, %s, %d, %d, %s", name, address, trackingNumber, weight, region);
    }
}

abstract class Package {

    String name;
    String address;
    int trackingNumber;
    int weight;

    public Package(String name, String address, int trackingNumber, int weight) {
        this.name = name;
        this.address = address;
        this.trackingNumber = trackingNumber;
        this.weight = weight;
    }



    abstract double price();

}

class PostOffice {
    String name;
    String location;
    List<Package> packages;

    public PostOffice(String name, String location) {
        this.name = name;
        this.location = location;
        this.packages = new ArrayList<>();
    }

    void loadPackages(InputStream inputStream) throws InvalidPackageException {

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] parts = line.split("\\s+");

            String name = parts[1];
            String addr = parts[2];
            int trn = Integer.parseInt(parts[3]);
            int weight = Integer.parseInt(parts[4]);

            if (weight <= 0) throw new InvalidPackageException();

            if (parts[0].equals("L")) {
                packages.add(new LocalPackage(name,addr,trn,weight,Boolean.parseBoolean(parts[5])));
            } else if (parts[0].equals("I")) packages.add(new InternationalPackage(name,addr,trn,weight, parts[5]));
            else throw new InvalidPackageException();
        }

    }



    boolean addPackage(Package p) {
        return packages.add(p);
    }

    Package mostExpensive() {
        return packages.stream().max(Comparator.comparing(Package::price)).orElse(null);
    }

    void printPackages(OutputStream out) {
        PrintWriter writer = new PrintWriter(out);

        packages.stream().sorted(Comparator.comparing(Package::price).reversed())
                .forEach(writer::println);
        writer.flush();
    }


}


public class PostOfficeTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PostOffice postOffice = new PostOffice("Poshta", "Skopje");
        File file = new File("C:\\Users\\koki_\\Desktop\\npProj\\no_2022\\src\\mk\\ukim\\finki\\xExam\\kolokviumi\\vlezni\\files\\lineprocesssss");


        try {
            postOffice.loadPackages(new FileInputStream(file));
            postOffice.printPackages(System.out);
            System.out.println();
            System.out.println("==Most expensive==");
            System.out.println(postOffice.mostExpensive());
        } catch (Exception e) {
            System.out.println("==TestingException==");
            System.out.println("Invalid value:");
            System.out.println(e.getMessage());
        }

        scanner.close();

    }
}
