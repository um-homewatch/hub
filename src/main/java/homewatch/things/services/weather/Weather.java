package homewatch.things.services.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather implements Thing {
  private final double temperature;
  private final double windSpeed;
  private final boolean raining;
  private final boolean cloudy;

  public Weather() {
    this.temperature = 0;
    this.windSpeed = 0;
    this.raining = false;
    this.cloudy = false;
  }

  Weather(double temperature, double windSpeed, boolean raining, boolean cloudy) {
    this.temperature = temperature;
    this.windSpeed = windSpeed;
    this.raining = raining;
    this.cloudy = cloudy;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getWindSpeed() {
    return windSpeed;
  }

  public boolean isRaining() {
    return raining;
  }

  public boolean isCloudy() {
    return cloudy;
  }

  @Override
  public ThingServiceFactory getFactory() {
    return new WeatherServiceFactory();
  }

  @Override
  public String getStringRepresentation() {
    return "weather";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Weather))
      return false;

    Weather weather = (Weather) o;

    return Double.compare(weather.getTemperature(), getTemperature()) == 0 && Double.compare(weather.getWindSpeed(), getWindSpeed()) == 0 && isRaining() == weather.isRaining() && isCloudy() == weather.isCloudy();
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(temperature);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(windSpeed);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (raining ? 1 : 0);
    result = 31 * result + (cloudy ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Weather{" +
            "temperature=" + temperature +
            ", windSpeed=" + windSpeed +
            ", raining=" + raining +
            ", cloudy=" + cloudy +
            '}';
  }
}
