
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherApp {
    private static final String API_KEY = "1e19707b22ec2d4e5ef1408291ae3055";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWeather Information System");
            System.out.println("1. Get weather by city name");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter city name: ");
                String city = scanner.nextLine();
                getWeatherData(city);
            } else if (choice == 2) {
                System.out.println("Thank you for using the Weather App!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void getWeatherData(String city) {
        try {

            String urlString = String.format("%s?q=%s&appid=%s&units=metric",
                    BASE_URL, city, API_KEY);
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                displayWeatherInfo(new JSONObject(response.toString()));
            } else {
                System.out.println("Error: Unable to fetch weather data. Response code: "
                        + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayWeatherInfo(JSONObject weatherData) {
        // Extract main weather information
        JSONObject main = weatherData.getJSONObject("main");
        JSONObject weather = weatherData.getJSONArray("weather").getJSONObject(0);

        // Get city name and country
        String cityName = weatherData.getString("name");
        String country = weatherData.getJSONObject("sys").getString("country");

        // Display formatted weather information
        System.out.println("\nWeather Information for " + cityName + ", " + country);
        System.out.println("----------------------------------------");
        System.out.printf("Temperature: %.1f°C%n", main.getDouble("temp"));
        System.out.printf("Feels Like: %.1f°C%n", main.getDouble("feels_like"));
        System.out.printf("Humidity: %d%%%n", main.getInt("humidity"));
        System.out.printf("Pressure: %d hPa%n", main.getInt("pressure"));
        System.out.println("Weather: " + weather.getString("main"));
        System.out.println("Description: " + weather.getString("description"));
    }
}