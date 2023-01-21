package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class WeatherStation {
    private int days;
    private TreeMap<Date, Measurement> measurements;
    private static final int MAX_DIFF = 150000;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new TreeMap<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        if (!this.measurements.isEmpty()) {
            if (date.getTime() - this.measurements.lastKey().getTime() < MAX_DIFF) return;

            Set<Date> dateSet = this.measurements.keySet().stream()
                    .filter(k -> daysBetween(k, date) >= this.days)
                    .collect(Collectors.toSet());

            for (Date date1 : dateSet) {
                this.measurements.remove(date1);
            }
        }

        this.measurements.put(date, new Measurement(temperature, wind, humidity, visibility, date));
    }

    public int total() {
        return this.measurements.size();
    }

    public void status(Date from, Date to) throws RuntimeException {

        Map<Date, Measurement> subMap = this.measurements.subMap(from, true, to, true);

        if (subMap.isEmpty()) throw new RuntimeException();

        double average = subMap.values().stream()
                .peek(System.out::println)
                .mapToDouble(Measurement::getTemp)
                .average()
                .orElse(0);

        System.out.printf("Average temperature: %.2f%n", average);

    }

    private int daysBetween(Date d1, Date d2) {
        return (int) Math.abs(ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant()));
    }
}

class Measurement {
    private float temp;
    private float wet;
    private float wind;
    private float see;
    private Date time;

    public Measurement(float temp, float wind, float wet,  float see, Date time) {
        this.temp = temp;
        this.wet = wet;
        this.wind = wind;
        this.see = see;
        this.time = time;
    }

    public float getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temp, wind, wet, see, time.toString());
    }

    //24.6 80.2 km/h 28.7% 51.7 km Tue Dec 17 23:40:15 CET 2013
}