//package com.weather.app;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//
//public class WeatherAppUI extends Application {
//
//    @Override
//    public void start(Stage primaryStage){
//        TextField cityInput = new TextField();
//        cityInput.setPromptText("Enter City Name");
//
//        Button getWeatherButton = new Button("Get Weather");
//
//        TextArea weatherOutput = new TextArea();
//        weatherOutput.setEditable(false);
//
//        getWeatherButton.setOnAction(e -> {
//            String city = cityInput.getText();
//            String weather = WeatherFetcher.fetchWeather(city);
//            weatherOutput.setText(weather);
//        });
//
//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(cityInput, getWeatherButton, weatherOutput);
//        layout.setStyle("-fx-padding: 20;");
//
//        primaryStage.setTitle("Weather Forecast App");
//        primaryStage.setScene(new Scene(layout, 400, 300));
//        primaryStage.show();
//
//
//    }
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
package com.weather.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;

public class WeatherAppUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create UI elements
        Label cityLabel = new Label("Enter City Name:");
        TextField cityTextField = new TextField();
        Button getWeatherButton = new Button("Get Weather");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(200);

        // Set padding and spacing
        VBox vbox = new VBox(10, cityLabel, cityTextField, getWeatherButton, resultArea);
        vbox.setPadding(new Insets(20));

        // Event handling for the Get Weather button
        getWeatherButton.setOnAction(e -> {
            String city = cityTextField.getText();
            if (!city.isEmpty()) {
                String weatherInfo = fetchWeather(city);
                resultArea.setText(weatherInfo);
            } else {
                resultArea.setText("Please enter a city name.");
            }
        });

        // Set up the scene
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather App");
        primaryStage.show();
    }

    private String fetchWeather(String city) {
        String apiKey = "e4e1c82d1a5432a532f6bba792a86a46";  // Replace with your API key
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            // Fetch weather data from API
            String response = WeatherFetcher.fetchWeatherData(url);
            JSONObject json = new JSONObject(response);

            // Extract data
            String cityName = json.getString("name");
            String country = json.getJSONObject("sys").getString("country");
            JSONObject main = json.getJSONObject("main");
            double temp = main.getDouble("temp") - 273.15;
            int humidity = main.getInt("humidity");

            JSONObject wind = json.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");

            String condition = json.getJSONArray("weather").getJSONObject(0).getString("description");

            // Return a formatted string
            return String.format(
                    "📍 Location: %s, %s\n🌡️ Temperature: %.2f°C\n💧 Humidity: %d%%\n💨 Wind Speed: %.2f m/s\n🌥️ Condition: %s",
                    cityName, country, temp, humidity, windSpeed, condition
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Error fetching weather data. Please check the city name or try again.";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

