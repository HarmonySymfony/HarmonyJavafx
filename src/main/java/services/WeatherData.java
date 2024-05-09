package services;

public class WeatherData {

    private final double temperature;
    private final String main;
    private final String description;

    public WeatherData(double temperature, String main, String description) {
        this.temperature = temperature;
        this.main = main;
        this.description = description;
    }

    // Getters here
    public double getTemperature() { return temperature; }
    public String getMain() { return main; }
    public String getDescription() { return description; }
}
