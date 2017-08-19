package homewatch.things.services.weather;

import com.sun.javaws.exceptions.InvalidArgumentException;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestWeatherServiceFactory {
  private static final WeatherServiceFactory serviceFactory = new WeatherServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    ThingService<Weather> weatherService = serviceFactory.createThingService("rest");
    DiscoveryService<Weather> discoveryService = serviceFactory.createDiscoveryService("rest");

    assertTrue(weatherService instanceof RestWeatherService);
    assertTrue(discoveryService instanceof NetworkThingDiscoveryService);
    assertThat(weatherService.getType(), is("Things::Weather"));
    assertThat(weatherService.getSubtype(), is("rest"));
  }

  @Test
  public void testOwmCreate() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.createThingService("owm");
    DiscoveryService<Weather> discoveryService = serviceFactory.createDiscoveryService("owm");

    assertTrue(weatherService instanceof OWMWeatherService);
    assertNull(discoveryService);
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.createThingService("rest");

    assertTrue(weatherService instanceof RestWeatherService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("owm"));
  }
}
