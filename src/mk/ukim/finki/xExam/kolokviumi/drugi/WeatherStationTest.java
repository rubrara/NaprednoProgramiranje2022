package mk.ukim.finki.xExam.kolokviumi.drugi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            ws.addMeasurment(temp, wind, hum, vis, date);
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

// vashiot kod ovde

class WeatherStation {

    private int days;
    private List<Measurements> measurements;
    private static final long MAX_DIFF = (5*60*1000)/2;
    private static final long DAY_TO_MILS = 24*60*60*1000;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new ArrayList<>();
    }

    public void addMeasurment(float temp, float wind, float hum, float vis, Date date) {
        if (notValidCheck(date)) return;

        this.measurements.add(new Measurements(temp, wind, hum, vis, date));

//        measurements.removeIf(measurement -> ((measurement.getDate().getTime() - date.getTime()) > (this.days * DAY_TO_MILS)));
        measurements.removeIf(m -> date.getTime() - m.getDate().getTime() > days*DAY_TO_MILS);
    }

    boolean notValidCheck(Date date) {
        return measurements.stream().anyMatch(d -> Math.abs(d.getDate().getTime() - date.getTime()) < MAX_DIFF);
    }


    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {

        to.setTime(to.getTime()+1);
        from.setTime(from.getTime()-1);

        List<Measurements> newMeas = measurements.stream()
                .filter(m -> m.getDate().after(from) && m.getDate().before(to))
                .sorted(Comparator.comparing(Measurements::getDate))
                .collect(Collectors.toList());

        if (newMeas.size()==0) throw new RuntimeException();
        newMeas.forEach(System.out::println);

        double avgTemp = newMeas.stream().mapToDouble(Measurements::getTemperature).summaryStatistics().getAverage();

        System.out.printf("Average temperature: %.2f", avgTemp);

    }

}

class Measurements {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurements(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date);
    }
}
