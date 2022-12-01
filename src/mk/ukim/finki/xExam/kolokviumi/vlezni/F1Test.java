package mk.ukim.finki.xExam.kolokviumi.vlezni;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Lap {
    int minutes;
    int seconds;
    int milliSeconds;

    public Lap(int minutes, int seconds, int milliSeconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliSeconds = milliSeconds;
    }

    public static Lap createLap(String line) {
        String[] parts = line.split(":");

        return new Lap(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    public double timeInMinutes() {
        return minutes + seconds / 60.0 + (milliSeconds / 1000.0) / 60.0;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", minutes, seconds, milliSeconds);
    }
}

class Pilot implements Comparable<Pilot> {
    String name;
    List<Lap> laps;

    public Pilot(String name, List<Lap> laps) {
        this.name = name;
        this.laps = laps;
    }

    public static Pilot createPilot(String line) {

        String[] parts = line.split("\\s+");

        String name = parts[0];
        List<Lap> laps = new ArrayList<>();

        Arrays.stream(parts).skip(1).forEach(l -> laps.add(Lap.createLap(l)));

        return new Pilot(name, laps);
    }


    Lap getBestLap() {
        return laps.stream().min(Comparator.comparing(Lap::timeInMinutes)).get();
    }


    @Override
    public String toString() {
        return String.format("%-10s%10s", this.name, getBestLap());
    }

    @Override
    public int compareTo(Pilot o) {
        return Double.compare(this.getBestLap().timeInMinutes(), o.getBestLap().timeInMinutes());
    }
}

class F1Race {

    List<Pilot> pilots;

    public F1Race() {
        this.pilots = new ArrayList<>();
    }

    void readResults(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        pilots = reader.lines().map(Pilot::createPilot).collect(Collectors.toList());
    }

    void printSorted(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        StringBuilder sb = new StringBuilder();


        // ova e so comparable
        List<Pilot> list = pilots.stream().sorted().collect(Collectors.toList());


        // ova e so comparator
        List<Pilot> list2 = pilots.stream().sorted(Comparator.comparing(c -> c.getBestLap().timeInMinutes())).collect(Collectors.toList());

        for (int i = 0; i < list2.size(); i++){
           sb.append(String.format("%d. %s\n", i+1, list2.get(i)));
        }

        writer.println(sb.toString());

        writer.flush();
    }
}
