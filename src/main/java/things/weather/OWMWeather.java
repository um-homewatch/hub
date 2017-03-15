package things.weather;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;

public class OWMWeather implements Weather {
  private static String URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=3e7e26039e4050a3edaaf374adb887de";
  private JsonNode weatherData;

  OWMWeather(String city) throws NetworkException {
    URL = String.format(URL, city);
    this.weatherData = NetUtils.get(HttpUrl.parse(URL)).getJson();
  }

  @Override
  public double getTemperature() {
    return weatherData.get("main").get("temp").asDouble() - 273.15d;
  }

  @Override
  public double getWindSpeed() {
    return weatherData.get("wind").get("speed").asDouble();
  }

  @Override
  @JsonGetter
  public boolean hasRain() {
    boolean rain = false;

    for (JsonNode weatherNode : weatherData.get("weather")) {
      int statusCode = weatherNode.get("id").asInt();
      rain = statusCode >= 300 && statusCode <= 531;
      if (rain)
        break;
    }

    return rain;
  }

  @Override
  @JsonGetter
  public boolean hasClouds() {
    return weatherData.get("clouds").get("all").asInt() > 0;
  }

  @Override
  public boolean ping() {
    try {
      this.weatherData = NetUtils.get(HttpUrl.parse(URL)).getJson();

      return true;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getSubType() {
    return "owm";
  }
}
