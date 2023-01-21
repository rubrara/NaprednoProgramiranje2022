package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

class EventCalendar {
    private int year;
    private Map<Integer, Integer> months;
    private Map<Integer, Set<Event>> eventsByDay;

    public EventCalendar(int year) {
        this.year = year;
        months = new HashMap<>();
        eventsByDay = new HashMap<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        int year = getYear(date);
        if (year != this.year) throw new WrongDateException("Wrong date: " + date.toString());

        int day = getDayOfYear(date);
        Event event = new Event(name, location, date);
        Set<Event> eventSet = eventsByDay.get(day);
        if (eventSet == null) eventSet = new TreeSet<>();
        eventSet.add(event);

        int month = getMonth(date) + 1;

        eventsByDay.put(day, eventSet);
        months.putIfAbsent(month, 0);
        months.computeIfPresent(month, (k, v) -> v += 1);
    }

    public void listEvents(Date date) {
        int day = getDayOfYear(date);

        if (eventsByDay.get(day) == null) System.out.println("No events on this day!");
        else
            eventsByDay.get(day).forEach(System.out::println);
    }

    public void listByMonth() {
        IntStream.range(1, 13).forEach(i -> System.out.printf("%d : %d\n", i, months.get(i) != null ? months.get(i) : 0));
    }

    static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
}


class Event implements Comparable<Event> {
    private String name;
    private String location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, yyy HH:mm"); //Fri Feb 15 19:45:00 GMT
        return String.format("%s at %s, %s", df.format(date), location, name);
    }

    @Override
    public int compareTo(Event o) {
        int r = date.compareTo(o.date);

        if (r == 0) return name.compareTo(o.name);

        return r;
    }
}

class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super(message);
    }
}
