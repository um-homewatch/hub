package things.weather;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import things.ThingService;

import java.util.logging.Logger;

class OWMWeatherService implements ThingService<Weather> {
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/things.weather?q=%s&APPID=3e7e26039e4050a3edaaf374adb887de";
  private String url;
  private JsonNode weatherData;

  public OWMWeatherService() {
    url = String.format(BASE_URL, "");
  }

  public OWMWeatherService(String city) {
    url = String.format(BASE_URL, city);
  }

  public void setCity(String city) {
    url = String.format(BASE_URL, city);
  }

  @Override
  public Weather get() throws NetworkException {
    this.weatherData = this.getWeatherData();

    return new Weather(this.getTemperature(), this.getWindSpeed(), this.getRain(), this.getClouds());
  }

  @Override
  public void put(Weather weather) throws NetworkException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(HttpUrl.parse(url)).getResponse().code() == 200;
    } catch (NetworkException e) {
      Logger.getGlobal().info("FAILED PING REASON:" + e);
      return false;
    }
  }

  private JsonNode getWeatherData() throws NetworkException {
    return NetUtils.get(HttpUrl.parse(url)).getJson();
  }

  @Override
  public String getType() {
    return "weather";
  }

  @Override
  public String getSubType() {
    return "owm";
  }

  private double getTemperature() {
    return weatherData.get("main").get("temp").asDouble() - 273.15d;
  }

  private double getWindSpeed() {
    return weatherData.get("wind").get("speed").asDouble();
  }

  private boolean getRain() {
    boolean rain = false;

    for (JsonNode weatherNode : weatherData.get("weather")) {
      int statusCode = weatherNode.get("id").asInt();
      rain = statusCode >= 300 && statusCode <= 531;
      if (rain)
        break;
    }

    return rain;
  }

  private boolean getClouds() {
    return weatherData.get("clouds").get("all").asInt() > 0;
  }
}
