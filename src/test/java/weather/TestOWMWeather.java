package weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NetworkException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import things.weather.OWMWeather;
import things.weather.Weather;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestOWMWeather {
  private static ObjectMapper OM = new ObjectMapper();
  private static File fixture = new File("src/test/fixtures/owmweather.json");
  private static Weather WEATHER;

  @BeforeClass
  public static void setup() throws NetworkException, IOException {
    WEATHER = PowerMockito.spy(new OWMWeather("Vancouver"));

    Whitebox.setInternalState(WEATHER, "weatherData", OM.readTree(fixture));
  }

  @Test
  public void getTemperature(){
    double temp = WEATHER.getTemperature();

    assertEquals(6.93, temp, 0.01);
  }

  @Test
  public void hasRain(){
    assertTrue(WEATHER.hasRain());
  }

  @Test
  public void hasCloud(){
    assertTrue(WEATHER.hasClouds());
  }

  @Test
  public void getWindSpeed(){
    double temp = WEATHER.getWindSpeed();

    assertEquals(1.11, temp, 0.01);
  }
}
