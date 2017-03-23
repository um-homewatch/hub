package things.weather;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import constants.WeatherStubs;
import exceptions.NetworkException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(OWMWeatherService.class)
public class TestRestWeather {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private RestWeatherService restWeatherService;
  private Weather originalWeather = new Weather(1.1, 2.2, true, true);
  private Weather returnedWeather;

  @Test
  public void testWeather() throws NetworkException, UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost(), 8080);
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    returnedWeather = restWeatherService.get();

    assertEquals(originalWeather, returnedWeather);
  }

  @Test
  public void goodPing() throws UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost(), 8080);
    WeatherStubs.stubGetStatus(wireMockRule, originalWeather);

    assertTrue(restWeatherService.ping());
  }


  @Test
  public void badPing() throws UnknownHostException {
    restWeatherService = new RestWeatherService(InetAddress.getLocalHost(), 77);

    assertFalse(restWeatherService.ping());
  }
}
