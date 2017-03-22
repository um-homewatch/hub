package things.weather;

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
}
