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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class WeatherAppUI extends Application {
    private Label cityLabel;
    private TextField cityInput;
    private Button fetchButton;
    private Label temperatureLabel;
    private Label humidityLabel;
    private Label windLabel;
    private Label conditionLabel;
    private Label locationLabel;
    private Label errorLabel;

    private ImageView weatherIcon;

    private Label title;





    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🌤️ Weather App");


        weatherIcon = new ImageView();
        weatherIcon.setFitWidth(80);   // You can adjust size if needed
        weatherIcon.setFitHeight(80);


        //Title
        title = new Label("Weather Forecast");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);


        //Input Selection
        cityLabel = new Label("Enter City Name:");
        cityLabel.setTextFill(Color.WHITE);

        cityInput = new TextField();
        cityInput.setPromptText("e.g., Delhi");
        cityInput.setPrefWidth(200);
        cityInput.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa;");

        fetchButton = new Button("Get Weather");
        fetchButton.setOnAction(e -> updateWeather());
        fetchButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");

        HBox inputBox = new HBox(10, cityLabel, cityInput, fetchButton);
        inputBox.setAlignment(Pos.CENTER);

        //Weather Display
        locationLabel = new Label();
        temperatureLabel = new Label();
        humidityLabel = new Label();
        windLabel = new Label();
        conditionLabel = new Label();

        // Set all weather labels to white
        List<Label> weatherLabels = Arrays.asList(locationLabel, temperatureLabel, humidityLabel, windLabel, conditionLabel);
        for (Label label : weatherLabels) {
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        }

        VBox weatherBox = new VBox(8, weatherIcon, locationLabel, temperatureLabel, humidityLabel, windLabel, conditionLabel);
        weatherBox.setAlignment(Pos.CENTER);
        weatherBox.setPadding(new Insets(10));

        //Error Label
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1e1e1e;");  // DARK BACKGROUND

        //  Toggle button comes after root is defined
        ToggleButton themeToggle = new ToggleButton("🌙 Dark Mode");
        themeToggle.setStyle("-fx-background-color: #444; -fx-text-fill: white;");

        themeToggle.setOnAction(e -> {
            if (themeToggle.isSelected()) {
                themeToggle.setText("☀️ Light Mode");
                applyDarkTheme(root);
            } else {
                themeToggle.setText("🌙 Dark Mode");
                applyLightTheme(root);
            }
        });

        //  Add all elements to root
        root.getChildren().addAll(title, themeToggle, inputBox, weatherBox, errorLabel);

        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

        applyDarkTheme(root); // Or applyLightTheme(root); depending on default


    }

//        // Create UI elements
//        Label cityLabel = new Label("Enter City Name:");
//        TextField cityTextField = new TextField();
//        Button getWeatherButton = new Button("Get Weather");
//        TextArea resultArea = new TextArea();
//        resultArea.setEditable(false);
//        resultArea.setPrefHeight(200);
//
//        // Set padding and spacing
//        VBox vbox = new VBox(10, cityLabel, cityTextField, getWeatherButton, resultArea);
//        vbox.setPadding(new Insets(20));
//
//        // Event handling for the Get Weather button
//        getWeatherButton.setOnAction(e -> {
//            String city = cityTextField.getText();
//            if (!city.isEmpty()) {
//                String weatherInfo = fetchWeather(city);
//                resultArea.setText(weatherInfo);
//            } else {
//                resultArea.setText("Please enter a city name.");
//            }
//        });
//
//        // Set up the scene
//        Scene scene = new Scene(vbox, 400, 300);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Weather App");
//        primaryStage.show();
//    }



//    private String fetchWeather(String city) {
//        String apiKey = "e4e1c82d1a5432a532f6bba792a86a46";  // Replace with your API key
//        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
//
//        try {
//            // Fetch weather data from API
//            String response = WeatherFetcher.fetchWeatherData(url);
//            JSONObject json = new JSONObject(response);
//
//            // Extract data
//            String cityName = json.getString("name");
//            String country = json.getJSONObject("sys").getString("country");
//            JSONObject main = json.getJSONObject("main");
//            double temp = main.getDouble("temp") - 273.15;
//            int humidity = main.getInt("humidity");
//
//            JSONObject wind = json.getJSONObject("wind");
//            double windSpeed = wind.getDouble("speed");
//
//            String condition = json.getJSONArray("weather").getJSONObject(0).getString("description");
//
//            // Return a formatted string
//            return String.format(
//                    "📍 Location: %s, %s\n🌡️ Temperature: %.2f°C\n💧 Humidity: %d%%\n💨 Wind Speed: %.2f m/s\n🌥️ Condition: %s",
//                    cityName, country, temp, humidity, windSpeed, condition
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "⚠️ Error fetching weather data. Please check the city name or try again.";
//        }
//    }



    private void updateWeather() {
        String city = cityInput.getText().trim();
        if (city.isEmpty()) {
            errorLabel.setText("❌ Please enter a city name.");
            return;
        }

        String apiKey = "e4e1c82d1a5432a532f6bba792a86a46"; // Replace with your actual key
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            String response = WeatherFetcher.fetchWeatherData(url);
            JSONObject json = new JSONObject(response);

            // Parse JSON
            String cityName = json.getString("name");
            String country = json.getJSONObject("sys").getString("country");
            JSONObject main = json.getJSONObject("main");
            double tempCelsius = main.getDouble("temp") - 273.15;
            int humidity = main.getInt("humidity");
            double windSpeed = json.getJSONObject("wind").getDouble("speed");
            String condition = json.getJSONArray("weather").getJSONObject(0).getString("description");

            // Extract icon code from JSON
            String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");

            // Construct local path to icon
            String iconPath = "/icons/" + iconCode + ".png";

            // Load image from resources
            Image icon = new Image(getClass().getResourceAsStream(iconPath));

            // Set image in the ImageView
            weatherIcon.setImage(icon);

            // Update UI
            locationLabel.setText("📍 Location: " + cityName + ", " + country);
            temperatureLabel.setText("🌡️ Temperature: " + String.format("%.2f", tempCelsius) + " °C");
            humidityLabel.setText("💧 Humidity: " + humidity + "%");
            windLabel.setText("💨 Wind Speed: " + windSpeed + " m/s");
            conditionLabel.setText("🌥️ Condition: " + condition);

            errorLabel.setText(""); // Clear error
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠️ Could not fetch weather. Try again.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void applyDarkTheme(Parent root) {
        root.setStyle("-fx-background-color: #1e1e1e;");
        cityLabel.setTextFill(Color.WHITE);
        locationLabel.setTextFill(Color.WHITE);
        temperatureLabel.setTextFill(Color.WHITE);
        humidityLabel.setTextFill(Color.WHITE);
        windLabel.setTextFill(Color.WHITE);
        conditionLabel.setTextFill(Color.WHITE);
        errorLabel.setTextFill(Color.RED);

        cityInput.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa;");
        fetchButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");
        cityLabel.setTextFill(Color.WHITE);
        title.setTextFill(Color.WHITE); //change title color
    }

    private void applyLightTheme(Parent root) {
        root.setStyle("-fx-background-color: #f0f0f0;");
        cityLabel.setTextFill(Color.BLACK);
        locationLabel.setTextFill(Color.BLACK);
        temperatureLabel.setTextFill(Color.BLACK);
        humidityLabel.setTextFill(Color.BLACK);
        windLabel.setTextFill(Color.BLACK);
        conditionLabel.setTextFill(Color.BLACK);
        errorLabel.setTextFill(Color.RED);

        cityInput.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-prompt-text-fill: #666666;");
        fetchButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");
        cityLabel.setTextFill(Color.BLACK);
        title.setTextFill(Color.BLACK); //change title color
    }

}



