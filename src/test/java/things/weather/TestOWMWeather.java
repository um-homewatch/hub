package things.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import things.ThingService;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(OWMWeatherService.class)
public class TestOWMWeather {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final File fixture = new File("src/test/fixtures/owmweather.json");
  private static ThingService<Weather> weatherService;
  private static Weather WEATHER;

  @BeforeClass
  public static void setup() throws Exception {
    weatherService = spy(new OWMWeatherService());

    doReturn(OM.readTree(fixture)).when(weatherService, "getWeatherData");

    WEATHER = weatherService.get();
  }

  @Test
  public void getTemperature() {
    double temp = WEATHER.getTemperature();

    assertEquals(6.93, temp, 0.01);
  }

  @Test
  public void hasRain() {
    assertTrue(WEATHER.isRaining());
  }

  @Test
  public void hasCloud() {
    assertTrue(WEATHER.isCloudy());
  }

  @Test
  public void getWindSpeed() {
    double temp = WEATHER.getWindSpeed();

    assertEquals(1.11, temp, 0.01);
  }
}
