package things.weather;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.JsonUtils;
import constants.WeatherStubs;
import exceptions.NetworkException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import server.controllers.WeatherController;
import things.ServerRunner;

import java.io.IOException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@PrepareForTest(OWMWeatherService.class)
public class TestWeatherController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private Weather originalWeather;

  @Before
  public void setup() {
    this.originalWeather = new Weather(new Random().nextDouble(), new Random().nextDouble(), true, true);
  }

  private void mockOWM() throws Exception {
    OWMWeatherService owmWeatherService = PowerMockito.mock(OWMWeatherService.class);
    PowerMockito.when(owmWeatherService.get()).thenReturn(originalWeather);

    WeatherServiceFactory weatherServiceFactory = PowerMockito.mock(WeatherServiceFactory.class);
    PowerMockito.when(weatherServiceFactory.create("owm")).thenReturn(owmWeatherService);
    PowerMockito.when(weatherServiceFactory.create("rest")).thenReturn(new RestWeatherService());

    Whitebox.setInternalState(WeatherController.class, "weatherServiceFactory", weatherServiceFactory);
  }

  @Test
  public void getWeatherOWM() throws Exception {
    mockOWM();

    String json = Unirest.get("http://localhost:4567/weather")
            .queryString("subType", "owm")
            .asJson()
            .getBody()
            .toString();

    Weather weather = JsonUtils.getOM().readValue(json, Weather.class);

    assertEquals(originalWeather, weather);
  }

  @Test
  public void getWeatherRest() throws IOException, NetworkException, UnirestException {
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    String json = Unirest.get("http://localhost:4567/weather")
            .queryString("address", "localhost")
            .queryString("port", 8080)
            .queryString("subType", "rest")
            .asJson()
            .getBody()
            .toString();

    Weather weather = JsonUtils.getOM().readValue(json, Weather.class);

    assertEquals(originalWeather, weather);
  }

  @Test
  public void errorInvalidArgument() throws UnirestException {
    int status = Unirest.get("http://localhost:4567/weather")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorInvalidSubType() throws UnirestException {
    Whitebox.setInternalState(WeatherController.class, "weatherServiceFactory", new WeatherServiceFactory());

    int status = Unirest.get("http://localhost:4567/weather")
            .queryString("city", "Vancouver")
            .queryString("subType", "cenas")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }
}
