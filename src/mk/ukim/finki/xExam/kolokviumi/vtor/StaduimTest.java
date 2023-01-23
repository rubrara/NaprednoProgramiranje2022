package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class Stadium {
    private String name;
    private Map<String, Sector> sectors;


    public Stadium(String name) {
        this.name = name;
        this.sectors = new HashMap<>();
    }

    void createSectors(String[] sectorNames, int[] sizes) {
        IntStream.range(0, sectorNames.length).forEach(i -> sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i])));
    }

    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        sectors.get(sectorName).buyTicket(seat, type);
    }

    void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::getAvailableSeats).reversed().thenComparing(Sector::getName))
                .forEach(System.out::println);
    }
}

class Sector {
    private String name;
    private int size;
    private Map<Integer, Integer> seats; // key -> seatNumber; value -> type
    private int restriction;

    public Sector(String name, int size) {
        this.name = name;
        this.size = size;
        this.seats = new HashMap<>();
        this.restriction = 0;
    }

    public void buyTicket(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (seats.containsKey(seat)) throw new SeatTakenException("");
        if ((type == 1 && restriction == 2) || (type == 2 && restriction == 1)) throw new SeatNotAllowedException("");
        if (restriction == 0) this.restriction = type;
        seats.put(seat, type);
    }

    public int getAvailableSeats() {
        return this.size - seats.size();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", name, getAvailableSeats(), size, 100 - (getAvailableSeats() * 1.0 / size) * 100.0);
    }

}

class SeatTakenException extends Exception {
    public SeatTakenException(String message) {
        super(message);
    }
}

class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException(String message) {
        super(message);
    }
}
