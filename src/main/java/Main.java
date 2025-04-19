import com.weather.app.WeatherFetcher;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter City Name: ");
        String city = scanner.nextLine();


        WeatherFetcher.fetchWeather(city);
    }
}
