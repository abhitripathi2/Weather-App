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
            // Fetch weather data
            String response = WeatherFetcher.fetchWeatherData(url);
            JSONObject jsonResponse = new JSONObject(response);

            // Extract weather information
            String cityName = jsonResponse.getString("name");
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp") - 273.15; // Convert Kelvin to Celsius

            // Return formatted result
            return String.format("Weather in %s:\nDescription: %s\nTemperature: %.2f°C", cityName, weatherDescription, temperature);
        } catch (Exception e) {
            return "Error fetching weather data. Please try again.";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

