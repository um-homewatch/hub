package things.weather;

import com.fasterxml.jackson.databind.JsonNode;
import constants.CacheUtils;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import things.ThingService;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class OWMWeatherService implements ThingService<Weather> {
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=3e7e26039e4050a3edaaf374adb887de";
  private static final HttpUrl REGION_URL = HttpUrl.parse("http://freegeoip.net/json/");
  private String url = "http://api.openweathermap.org/data/2.5/weather?Braga&APPID=3e7e26039e4050a3edaaf374adb887de";
  private JsonNode weatherData;

  @Override
  public Weather get() throws NetworkException {
    try {
      this.weatherData = this.getWeatherData();

      return new Weather(this.getTemperature(), this.getWindSpeed(), this.getRain(), this.getClouds());
    } catch (ExecutionException e) {
      throw new NetworkException(e, 500);
    }
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

  private JsonNode getWeatherData() throws NetworkException, ExecutionException {
    String region = CacheUtils.get(REGION_URL).getJson().get("region_name").asText();
    this.url = String.format(BASE_URL, region);

    return CacheUtils.get(HttpUrl.parse(url)).getJson();
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
