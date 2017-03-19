package things.weather;

import exceptions.InvalidSubTypeException;
import things.ThingService;

public class WeatherServiceFactory {
  private WeatherServiceFactory() {
  }

  public static ThingService<Weather> create(String city, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService(city);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
