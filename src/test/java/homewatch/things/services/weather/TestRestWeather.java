package homewatch.things.services.weather;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.stubs.WeatherStubs;
import homewatch.exceptions.NetworkException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestRestWeather {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private RestWeatherService restWeatherService;
  private final Weather originalWeather = new Weather(1.1, 2.2, true, true);

  @Test
  public void testWeather() throws NetworkException, UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost().getHostName(), 8080);
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    Weather returnedWeather = restWeatherService.get();

    assertEquals(originalWeather, returnedWeather);
  }

  @Test
  public void goodPing() throws UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost().getHostName(), 8080);
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    assertTrue(restWeatherService.ping());
  }


  @Test
  public void badPing() throws UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost().getHostName(), 77);

    assertFalse(restWeatherService.ping());
  }
}
