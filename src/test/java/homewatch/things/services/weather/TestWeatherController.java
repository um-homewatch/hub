package homewatch.things.services.weather;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.constants.JsonUtils;
import homewatch.stubs.WeatherStubs;
import homewatch.exceptions.NetworkException;
import homewatch.things.ServerRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OWMWeatherService.class, WeatherServiceFactory.class})
@PowerMockIgnore("javax.net.ssl.*")
public class TestWeatherController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private Weather originalWeather;

  @Before
  public void setup() {
    this.originalWeather = new Weather(new Random().nextDouble(), new Random().nextDouble(), true, true);
  }


  private void mockOWM() throws Exception {
    OWMWeatherService owmWeatherService = Mockito.mock(OWMWeatherService.class);

    Mockito.when(owmWeatherService.get()).thenReturn(originalWeather);

    PowerMockito.whenNew(OWMWeatherService.class).withAnyArguments().thenReturn(owmWeatherService);
  }

  @Test
  public void getWeatherOWM() throws Exception {
    mockOWM();

    String json = Unirest.get("http://localhost:4567/devices/weather")
        .queryString("subtype", "owm")
        .asJson()
        .getBody()
        .toString();

    Weather weather = JsonUtils.getOM().readValue(json, Weather.class);

    assertEquals(originalWeather, weather);
  }

  @Test
  public void getWeatherRest() throws IOException, NetworkException, UnirestException {
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    String json = Unirest.get("http://localhost:4567/devices/weather")
        .queryString("address", "localhost")
        .queryString("port", 8080)
        .queryString("subtype", "rest")
        .asJson()
        .getBody()
        .toString();

    Weather weather = JsonUtils.getOM().readValue(json, Weather.class);

    assertEquals(originalWeather, weather);
  }
}
