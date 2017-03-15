package things.weather;

import things.Thing;

public interface Weather extends Thing {
  double getTemperature();

  double getWindSpeed();

  boolean hasRain();

  boolean hasClouds();

  default String getType() {
    return "weather";
  }
}
