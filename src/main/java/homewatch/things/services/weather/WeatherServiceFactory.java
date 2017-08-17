package homewatch.things.services.weather;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;

public class WeatherServiceFactory implements ThingServiceFactory<Weather> {
  @Override
  public ThingService<Weather> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService();
      case "rest":
        return new RestWeatherService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subtypeUpper = subtype.toUpperCase();

    try {
      SubType.valueOf(subtypeUpper);

      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}

enum SubType {
  REST, OWM
}
