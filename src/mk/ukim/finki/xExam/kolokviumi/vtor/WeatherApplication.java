package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.util.*;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}

interface Listener {
    void update(float temp, float humid, float pressure);
}

interface Displayable {
    void display();
}


class WeatherDispatcher {

    private float temperature;
    private float humidity;
    private float pressure;

    private Set<Listener> listeners;

    public WeatherDispatcher() {
        this.listeners = new HashSet<>();
        this.temperature = 0;
        this.humidity = 0;
        this.pressure = 0;
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;

        notifyListeners();
    }

    public void notifyListeners() {
        for (Listener l : listeners) {
            l.update(this.temperature, this.humidity, this.pressure);
        }
    }

    public void register(Listener listener) {
        this.listeners.add(listener);
    }

    public void remove(Listener listener) {
        this.listeners.remove(listener);
    }
}


class CurrentConditionsDisplay implements Listener, Displayable {

    private float temperature;
    private float humidity;

    WeatherDispatcher weatherDispatcher;

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temp, float humid, float pressure) {
        this.humidity = humid;
        this.temperature = temp;

        display();
    }

    @Override
    public void display() {
        System.out.println("Temperature: " + temperature + "F");
        System.out.println("Humidity: " + humidity + "%");
    }
}

class ForecastDisplay implements Listener, Displayable {
    private float currentPressure = 0.0f;
    private float lastPressure;
    WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temp, float humid, float pressure) {
        this.lastPressure = currentPressure;
        this.currentPressure = pressure;

        display();
    }

    @Override
    public void display() {
        System.out.printf("Forecast: %s%n\n", currentPressure > lastPressure ? "Improving" : currentPressure < lastPressure ? "Cooler" : "Same");
    }
}
