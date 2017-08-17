package homewatch.things.services.weather;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestWeatherServiceFactory {
  private static final WeatherServiceFactory serviceFactory = new WeatherServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    ThingService<Weather> weatherService = serviceFactory.create("rest");

    assertTrue(weatherService instanceof RestWeatherService);
    assertThat(weatherService.getType(), is("Things::Weather"));
    assertThat(weatherService.getSubtype(), is("rest"));
  }

  @Test
  public void testOwmCreate() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.create("owm");

    assertTrue(weatherService instanceof OWMWeatherService);
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.create("rest");

    assertTrue(weatherService instanceof RestWeatherService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("owm"));
  }
}
