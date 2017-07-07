package homewatch.things.services.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
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
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Weather))
      return false;

    Weather weather = (Weather) o;

    return Double.compare(weather.getTemperature(), getTemperature()) == 0 && Double.compare(weather.getWindSpeed(), getWindSpeed()) == 0 && isRaining() == weather.isRaining() && isCloudy() == weather.isCloudy();
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
