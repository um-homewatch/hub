package constants;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.json.JSONObject;
import things.weather.Weather;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WeatherStubs {
  public static void stubGetStatus(WireMockRule wireMockRule, Weather weather) {
    JSONObject json = new JSONObject();
    json.put("temperature", weather.getTemperature());
    json.put("windspeed", weather.getWindSpeed());
    json.put("raining", weather.isCloudy());
    json.put("cloudy", weather.isRaining());

    wireMockRule.stubFor(get(urlPathEqualTo("/status"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(json.toString())));
  }
}
