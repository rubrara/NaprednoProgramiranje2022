package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

class Window {
    private String name;

    Map<Integer, Component> componentByPosition;

    public Window(String name) {
        this.name = name;
        this.componentByPosition = new TreeMap<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if (componentByPosition.containsKey(position))
            throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!", position));

        componentByPosition.putIfAbsent(position, component);
    }

    void changeColor(int weight, String color) {
        componentByPosition.values().forEach(c -> c.changeColor(weight, color));

    }

    void swichComponents(int pos1, int pos2) {
        Component comp1 = componentByPosition.get(pos1);
        Component comp2 = componentByPosition.get(pos2);

        componentByPosition.replace(pos1, comp2);
        componentByPosition.replace(pos2, comp1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("WINDOW ").append(name).append("\n");

        componentByPosition.forEach((key, value) ->
                sb.append(key)
                        .append(":")
                        .append(value.getWeight())
                        .append(":")
                        .append(value.getColor())
                        .append("\n")
                        .append(value.getRepresentation("")));

        return sb.toString();
    }
}

class Component implements Comparable<Component> {
    private String color;
    private int weight;

    private Set<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new TreeSet<>();
    }

    void addComponent(Component component) {
        this.components.add(component);
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public void changeColor(int weight, String color) {
        if (this.weight < weight) this.color = color;

        components.forEach(c -> c.changeColor(weight, color));
    }

    @Override
    public int compareTo(Component o) {
        int r = Integer.compare(this.weight, o.weight);

        if (r == 0) return color.compareTo(o.color);

        return r;
    }

    public String getRepresentation(String tab) {
        StringBuilder sb = new StringBuilder();

        tab += "---";
        for (Component c : components) {

            sb.append(tab).append(c.weight).append(":").append(c.color).append("\n");

            if (!c.components.isEmpty()) {
                sb.append(c.getRepresentation(tab));
            }
        }

        return sb.toString();
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}
