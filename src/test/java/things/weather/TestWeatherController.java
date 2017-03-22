package things.weather;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.JsonUtils;
import exceptions.NetworkException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import server.controllers.WeatherController;
import things.ServerRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@PrepareForTest(OWMWeatherService.class)
public class TestWeatherController extends ServerRunner {
  @Before
  public void setup() throws Exception {
    Weather WEATHER = new Weather(6.93, 1.11, true, true);
    OWMWeatherService owmWeatherService = PowerMockito.mock(OWMWeatherService.class);
    PowerMockito.when(owmWeatherService.get()).thenReturn(WEATHER);

    WeatherServiceFactory weatherServiceFactory = PowerMockito.mock(WeatherServiceFactory.class);
    PowerMockito.when(weatherServiceFactory.create(Mockito.anyString(), Mockito.anyString())).thenReturn(owmWeatherService);

    Whitebox.setInternalState(WeatherController.class, "weatherServiceFactory", weatherServiceFactory);
  }

  @Test
  public void getWeather() throws IOException, NetworkException, UnirestException {
    String json = Unirest.get("http://localhost:4567/weather")
            .queryString("city", "Vancouver")
            .queryString("subType", "owm")
            .asJson()
            .getBody()
            .toString();

    System.out.println(json);

    Weather weather = JsonUtils.getOM().readValue(json, Weather.class);

    assertEquals(6.93, weather.getTemperature(), 0.01);
    assertTrue(weather.isRaining());
    assertTrue(weather.isCloudy());
    assertEquals(1.11, weather.getWindSpeed(), 0.01);
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
