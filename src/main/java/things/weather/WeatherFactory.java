package things.weather;

import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;

@SuppressWarnings("SameParameterValue")
public class WeatherFactory {
  public static Weather create(String city, String subtype) throws InvalidSubTypeException, NetworkException {
    switch (subtype) {
      case "owm":
        return new OWMWeather(city);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
