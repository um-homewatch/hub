package things.weather;

import exceptions.InvalidSubTypeException;
import org.junit.Test;
import things.HttpThingService;
import things.ThingService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestWeatherServiceFactory {
  private static final WeatherServiceFactory serviceFactory = new WeatherServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    InetAddress addr = InetAddress.getByName("192.168.1.50");
    ThingService<Weather> weatherService = serviceFactory.create("Vancouver", "owm");

    assertTrue(weatherService instanceof OWMWeatherService);
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.create("owm");

    assertTrue(weatherService instanceof OWMWeatherService);
  }

  @Test
  public void testIsSubType(){
    assertTrue(serviceFactory.isSubType("owm"));
  }
}
