package services;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {

    private static final String API_KEY = "7676b956d7d850be44a867febbfe67d2";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static WeatherData getWeather(String location) throws Exception {
        String urlString = BASE_URL + "?q=" + location + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner scanner = new Scanner(url.openStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject jsonObject = new JSONObject(response);
            JSONObject mainData = jsonObject.getJSONObject("main");
            JSONObject weatherData = jsonObject.getJSONArray("weather").getJSONObject(0);

            return new WeatherData(
                    mainData.getDouble("temp"),
                    weatherData.getString("main"),
                    weatherData.getString("description")
            );
        }
    }
}

