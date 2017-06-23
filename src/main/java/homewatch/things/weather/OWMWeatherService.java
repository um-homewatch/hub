package homewatch.things.weather;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.CacheUtils;
import homewatch.exceptions.NetworkException;
import homewatch.things.ThingService;
import okhttp3.HttpUrl;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class OWMWeatherService extends ThingService<Weather> {
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=3e7e26039e4050a3edaaf374adb887de";
  private static final HttpUrl REGION_URL = HttpUrl.parse("http://freegeoip.net/json/");

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

  @Override
  public void setAttributes(Map<String, ?> attributes) {
    //no attributes to set...
  }

  private JsonNode getWeatherData() throws ExecutionException {
    String region = CacheUtils.get(REGION_URL).getJson().get("region_name").asText();
    String url = String.format(BASE_URL, region);

    return CacheUtils.get(HttpUrl.parse(url)).getJson();
  }

  @Override
  public String getType() {
    return "Things::Weather";
  }

  @Override
  public String getSubtype() {
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
