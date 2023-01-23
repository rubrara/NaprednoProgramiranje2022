package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;
import java.util.stream.Collectors;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

class Airports {

    Map<String, Airport> airports;

    public Airports() {
        this.airports = new HashMap<>();
    }


    public void addAirport(String name, String country, String code, int passengers) {
        this.airports.putIfAbsent(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        this.airports.get(from).addFlight(new Flight(from, to, time, duration));
    }

    public void showFlightsFromAirport(String code) {
        System.out.print(this.airports.get(code));


        int i = 1;
        for (Flight f : this.airports.get(code).getFlights()) {
            System.out.printf("%d. %s\n", i++, f);
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Set<Flight> flightSet = new TreeSet<>();
         this.airports.get(from).getFlights().stream()
                .filter(f -> f.getTo().equals(to))
                .forEach(flightSet::add);

         if (flightSet.isEmpty()) System.out.printf("No flights from %s to %s\n", from, to);
         else flightSet.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        Set<Flight> flightSet = new TreeSet<>();
        airports.values().stream().flatMap(a -> a.getFlights().stream())
                .filter(f -> f.getTo().equals(to))
                .forEach(flightSet::add);

        flightSet.forEach(System.out::println);
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;
    private Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new TreeSet<>();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d\n", name, code, country, passengers);
    }
}

class Flight implements Comparable<Flight> {
    private String from;
    private String to;
    private int time;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(Flight o) {
        return Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime).thenComparing(Flight::getFrom).compare(this, o);
    }

    @Override
    public String toString() {
        int end = time + duration;
        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", from, to, time/60, time%60, end/60 % 24, end%60,end/60 >= 24 ? "+1d " : "" , duration/60, duration%60);
    }
}


