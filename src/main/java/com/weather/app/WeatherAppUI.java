
package com.weather.app;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;



public class WeatherAppUI extends Application {
    private Label cityLabel;
   // private TextField cityInput;
   private ComboBox<String> cityInput;
   private List<JSONObject> citySuggestions = new ArrayList<>();


    private Button fetchButton;
    private Label temperatureLabel;
    private Label humidityLabel;
    private Label windLabel;
    private Label conditionLabel;
    private Label locationLabel;
    private Label errorLabel;
    private ImageView weatherIcon;
    private Label title;
    private ProgressIndicator progressIndicator;

    private VBox root;
    private HBox forecastContainer;
    private List<Label> forecastTextLabels = new ArrayList<>();
    private boolean isDarkMode = false;




    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException {
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

        //cityInput = new TextField();
        cityInput = new ComboBox<>();
        cityInput.setEditable(true);
        cityInput.setPromptText("e.g., Delhi");
        cityInput.setPrefWidth(200);
        cityInput.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa;");


        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setOnAction(e -> updateWeather());
        refreshButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");


        fetchButton = new Button("Get Weather");
        fetchButton.setOnAction(e -> updateWeather());
        fetchButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");

        //5 day's forecast
        forecastContainer = new HBox(20);  // spacing between each day’s forecast
        forecastContainer.setAlignment(Pos.CENTER);


        HBox inputBox = new HBox(10, cityLabel, cityInput, fetchButton, refreshButton);
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


        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1e1e1e;");  // DARK BACKGROUND

        //  Toggle button comes after root is defined
        ToggleButton themeToggle = new ToggleButton("🌙 Dark Mode");
        themeToggle.setStyle("-fx-background-color: #444; -fx-text-fill: white;");

        themeToggle.setOnAction(e -> {
            if (themeToggle.isSelected()) {
                themeToggle.setText("☀️ Light Mode");
                isDarkMode = true; // dark mode active
                applyDarkTheme(root);
            } else {
                themeToggle.setText("🌙 Dark Mode");
                isDarkMode = false; // light mode active
                applyLightTheme(root);
            }

            // Refresh forecast colors after theme change
            updateForecastTheme();
        });


        //ProgressIndicator Creation
        progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-progress-color: blue;");
        progressIndicator.setVisible(false);  // Initially hidden
        root.getChildren().add(progressIndicator);  // Add once



        //  Add all elements to root
        root.getChildren().addAll(title, themeToggle, inputBox, weatherBox, forecastContainer, errorLabel);

        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

        applyLightTheme(root);// Or applyLightTheme(root); depending on default
        applyThemeTransition();
        applyZoomInEffect();

        String currentCity = getCurrentCityFromIP();
        //cityInput.setText(currentCity);
        cityInput.getEditor().setText(currentCity);
        updateWeather(); // optional: auto-fetch weather on launch

    }


    private void applyThemeTransition() {
    }

    private void applyThemeTransition(Parent root, String color) {
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(root);
            transition.setDuration(Duration.millis(500));
            transition.setByX(30);  // Horizontal movement
            transition.setCycleCount(1);
            transition.setAutoReverse(true);
            transition.setOnFinished(e -> {
                root.setStyle("-fx-background-color: " + color + ";");
            });
            transition.play();
    }




    private void updateWeather() {
        showLoading();  // Show loading indicator

        String fullInput = cityInput.getEditor().getText().trim();

        if (fullInput.isEmpty()) {
            errorLabel.setText("❌ Please enter a city name.");
            hideLoading();
            return;
        }

        // Extract only the city name before the first comma
        String city = fullInput.split(",")[0].trim();

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
            weatherIcon.setImage(icon);

            // Update UI
            locationLabel.setText("📍 Location: " + cityName + ", " + country);
            temperatureLabel.setText("🌡️ Temperature: " + String.format("%.2f", tempCelsius) + " °C");
            humidityLabel.setText("💧 Humidity: " + humidity + "%");
            windLabel.setText("💨 Wind Speed: " + windSpeed + " m/s");
            conditionLabel.setText("🌥️ Condition: " + condition);

            // Apply fade-in effect
            applyFadeInEffect(locationLabel);
            applyFadeInEffect(temperatureLabel);
            applyFadeInEffect(humidityLabel);
            applyFadeInEffect(windLabel);
            applyFadeInEffect(conditionLabel);

            // Also update 5-day forecast using just the city
            fetchFiveDayForecast(city);

            errorLabel.setText(""); // Clear error




        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠️ Could not fetch weather. Try again.");
        }

        hideLoading();  // Hide loading indicator after fetching is done
    }


    private void fetchFiveDayForecast(String city) {
        String apiKey = "e4e1c82d1a5432a532f6bba792a86a46";  // Your API key
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

        try {
            String response = WeatherFetcher.fetchWeatherData(url);
            JSONObject json = new JSONObject(response);
            JSONArray list = json.getJSONArray("list");

            Map<String, JSONObject> dailyData = new LinkedHashMap<>();

            for (int i = 0; i < list.length(); i++) {
                JSONObject entry = list.getJSONObject(i);
                String dateTime = entry.getString("dt_txt");

                if (dateTime.contains("12:00:00")) {  // pick one forecast per day (at 12 noon)
                    String date = dateTime.split(" ")[0];
                    dailyData.put(date, entry);

                    if (dailyData.size() == 5) break;
                }
            }

            showForecastUI(dailyData);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠️ Could not load forecast.");
        }
    }



    private void showForecastUI(Map<String, JSONObject> forecastMap) {
        forecastTextLabels.clear();               //clear previous labels
        forecastContainer.getChildren().clear();  //clear old UI cards

        for (Map.Entry<String, JSONObject> entry : forecastMap.entrySet()) {
            String date = entry.getKey();
            JSONObject obj = entry.getValue();

            double temp = obj.getJSONObject("main").getDouble("temp") - 273.15;
            String condition = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            String iconCode = obj.getJSONArray("weather").getJSONObject(0).getString("icon");

            String iconPath = "/icons/" + iconCode + ".png";
            Image icon = new Image(getClass().getResourceAsStream(iconPath));

            // Create UI card
            VBox forecastCard = new VBox(5);
            forecastCard.setAlignment(Pos.CENTER);

            Label dayLabel = new Label(LocalDate.parse(date).getDayOfWeek().toString());
            Label tempLabel = new Label(String.format("%.1f°C", temp));
            Label condLabel = new Label(condition);

            forecastTextLabels.add(dayLabel);
            forecastTextLabels.add(tempLabel);
            forecastTextLabels.add(condLabel);

            //Color textColor = null;
            Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
            dayLabel.setTextFill(textColor);
            tempLabel.setTextFill(textColor);
            condLabel.setTextFill(textColor);

            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(40);
            iconView.setFitHeight(40);

            forecastCard.getChildren().addAll(dayLabel, iconView, tempLabel, condLabel);
            forecastContainer.getChildren().add(forecastCard);
        }
    }

    private void hideLoading() {
        progressIndicator.setVisible(false);
    }

    private void showLoading() {
        progressIndicator.setVisible(true);
    }

    private void applyFadeInEffect(Label label) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void applyZoomInEffect() {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(1000), title);
        scaleIn.setFromX(0.5);
        scaleIn.setFromY(0.5);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.play();
    }

    private void updateForecastTheme() {
        for (Node node : forecastContainer.getChildren()) {
            if (node instanceof VBox forecastCard) {
                for (Node child : forecastCard.getChildren()) {
                    if (child instanceof Label label) {
                        label.setTextFill(isDarkMode ? Color.WHITE : Color.BLACK);
                    }
                }
            }
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

        for (Label lbl : forecastTextLabels) {
            lbl.setTextFill(Color.WHITE);
        }
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

    cityInput.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-prompt-text-fill: #888888;");
    fetchButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");
    title.setTextFill(Color.BLACK);

    for (Label lbl : forecastTextLabels) {
        lbl.setTextFill(Color.BLACK);
    }
}

    private String getCurrentCityFromIP() {
        String apiUrl = "https://ipinfo.io/json";  // Free service that gives location info from IP

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            String city = json.getString("city");  // Fetch city from response
            return city;
        } catch (Exception e) {
            e.printStackTrace();
            return "Delhi"; // Default fallback city if error happens
        }
    }


    private JSONObject getCurrentLocationFromIP() {
        try {
            String response = WeatherFetcher.fetchWeatherData("http://ip-api.com/json/");
            return new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



