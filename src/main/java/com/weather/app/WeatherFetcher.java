package com.weather.app;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFetcher {
    private static final String API_KEY = "e4e1c82d1a5432a532f6bba792a86a46";

    public static String fetchWeather(String city){
        try {
            String endpoint = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null){
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());

            String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = json.getJSONObject("main").getDouble("temp");
            int humidity = json.getJSONObject("main").getInt("humidity");
            double windSpeed = json.getJSONObject("wind").getDouble("speed");

            System.out.println("\nWeather in " + city + ":");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Condition: " + weather);
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " m/s");
        }
        catch (Exception e){
            System.out.println("Error fetching weather data");
            System.out.println("Reason: " + e.getMessage());
        }

        return city;
    }

    public static String fetchWeatherData(String urlStr) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather data.";
        }
        return result.toString();
    }
}
