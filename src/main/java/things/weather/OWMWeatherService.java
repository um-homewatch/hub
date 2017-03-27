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

  @Override
  public Weather get() throws NetworkException {
    try {
      JsonNode response = this.getWeatherData();

      return this.jsonToWeather(response);
    } catch (ExecutionException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Weather put(Weather weather) throws NetworkException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean ping() {
    try {
      get();
      return true;
    } catch (NetworkException e) {
      Logger.getGlobal().info("FAILED PING REASON:" + e);
      return false;
    }
  }

  private JsonNode getWeatherData() throws ExecutionException {
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

  private Weather jsonToWeather(JsonNode json) {
    double temperature = json.get("main").get("temp").asDouble() - 273.15d;
    double windspeed = json.get("wind").get("speed").asDouble();
    boolean rain = getRain(json);
    boolean cloudy = json.get("clouds").get("all").asInt() > 0;

    return new Weather(temperature, windspeed, rain, cloudy);
  }

  private boolean getRain(JsonNode json) {
    boolean rain = false;

    for (JsonNode weatherNode : json.get("weather")) {
      int statusCode = weatherNode.get("id").asInt();
      rain = statusCode >= 300 && statusCode <= 531;
      if (rain)
        break;
    }

    return rain;
  }
}
