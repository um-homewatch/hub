package things.weather;

import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;

public class WeatherFactory {
  public static Weather create(String city, String subtype) throws InvalidSubTypeException, NetworkException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService(city).get();
      default:
        throw new InvalidSubTypeException();
    }
  }
}
