package things.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WeatherStubs;
import exceptions.NetworkException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import things.ThingService;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(OWMWeatherService.class)
public class TestOWMWeather {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final File fixture = new File("src/test/fixtures/owmweather.json");
  private static final Weather originalWeather = new Weather(7, 1.11, true, true);
  private ThingService<Weather> weatherService;


  @Before
  public void setup() throws Exception {
    weatherService = spy(new OWMWeatherService());

    doReturn(OM.readTree(fixture)).when(weatherService, "getWeatherData");
  }

  @Test
  public void testWeather() throws NetworkException, UnknownHostException {
    Weather returnedWeather = weatherService.get();

    assertEquals(originalWeather, returnedWeather);
  }

  @Test
  public void goodPing() throws UnknownHostException {
    assertTrue(weatherService.ping());
  }
}
