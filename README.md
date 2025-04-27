🌤️ Weather Forecast App

A JavaFX-based Weather Forecast Application that shows the current weather, a 5-day forecast, supports city suggestions, IP-based location detection, and has a Dark/Light theme toggle!
It fetches real-time weather data from OpenWeatherMap API and uses beautiful animations to enhance the user experience.
✨ Features

    🔎 Auto-detects your location using your IP address.

    🏙️ City suggestions using a ComboBox with editable input.

    ☀️ Current Weather Information (Temperature, Humidity, Wind Speed, Conditions).

    📅 5-Day Forecast at noon time.

    🎨 Dark Mode & Light Mode toggle.

    📈 Smooth Animations (Fade-in, Zoom-in, Theme transitions).

    ⏳ Progress Indicator while fetching data.

    🌐 City Name Parsing to ignore extra characters.

📷 Screenshots
Light Mode	Dark Mode
	

(You can add your screenshots in a folder named /assets and update the links)
⚙️ Technologies Used

    Java 17+

    JavaFX 17+

    OpenWeatherMap API

    IPInfo API (for auto-detecting city)

    JSON parsing with org.json

🏗️ Project Structure

src/
 └── com.weather.app/
      ├── WeatherAppUI.java   // Main JavaFX application
      ├── WeatherFetcher.java // (Expected helper class for API calls)
resources/
 └── icons/                   // Weather icons (sun, cloud, rain, etc.)
README.md
🚀 Getting Started
Prerequisites

    Java 17 or newer

    JavaFX SDK installed (or use Maven/Gradle JavaFX dependencies)

Clone the Repository

git clone https://github.com/your-username/weather-app.git
cd weather-app

Run the App

    Open the project in your favorite IDE (IntelliJ IDEA, Eclipse, etc.).

    Add JavaFX libraries to your project settings.

    Make sure you have a valid OpenWeatherMap API key.

    Update your WeatherAppUI.java file:

    String apiKey = "YOUR_OPENWEATHERMAP_API_KEY";

    Run WeatherAppUI.java.

🔑 API Keys

    OpenWeatherMap for weather data: Get API Key

    IPInfo.io for IP-based location detection (free without API key).

🛠️ Improvements and Future Work

    🌍 Add support for multiple languages.

    📍 Improve city suggestions with real-time search.

    🌡️ Allow temperature unit switching (Celsius ↔️ Fahrenheit).

    📈 Add historical weather data and charts.

    🏞️ Background changes based on weather conditions.

🤝 Contributing

Pull requests are welcome!
Feel free to open an issue if you find bugs or want new features.
📄 License

This project is licensed under the MIT License.
💬 Contact

Created with ❤️ by [Abhishek Tripathi]
GitHub: Abhitripathi2
