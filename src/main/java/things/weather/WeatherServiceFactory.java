package things.weather;

import exceptions.InvalidSubTypeException;
import things.ThingService;
import things.ThingServiceFactory;

public class WeatherServiceFactory implements ThingServiceFactory<Weather>{
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

  @Override
  public ThingService<Weather> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    return false;
  }
}
