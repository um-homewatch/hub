package homewatch.things.services.thermostat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.exceptions.NetworkException;
import homewatch.stubs.ThermostatStubs;
import homewatch.things.ThingService;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestRestThermostat {
  private static final int PORT = 8080;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(PORT).bindAddress("0.0.0.0"));

  private static ThingService<Thermostat> thermostatService;
  private static Thermostat originalThermostat;

  @BeforeClass
  public static void setup() throws UnknownHostException {
    thermostatService = new RestThermostatService(InetAddress.getLocalHost().getHostName(), PORT);
    originalThermostat = new Thermostat(new Random().nextDouble());
  }

  @Test
  public void getThermostat() throws UnknownHostException, NetworkException {
    ThermostatStubs.stubGetStatus(wireMockRule, originalThermostat);

    Thermostat newThermostat = thermostatService.get();

    assertThat(newThermostat.getTargetTemperature(), is(originalThermostat.getTargetTemperature()));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void setThermostat() throws IOException, NetworkException {
    ThermostatStubs.stubPutStatus(wireMockRule, new Thermostat(24.5));

    Thermostat thermostat = new Thermostat(24.5);
    thermostatService.put(thermostat);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"target_temperature\":24.5}")));
  }

  @Test
  public void testGoodPing() {
    ThermostatStubs.stubGetStatus(wireMockRule, originalThermostat);

    boolean ping = thermostatService.ping();

    assertTrue(ping);
  }

  @Test
  public void testBadPing() throws UnknownHostException {
    ThingService<Thermostat> badThermostatService = new RestThermostatService(InetAddress.getLocalHost().getHostName());

    boolean ping = badThermostatService.ping();

    assertFalse(ping);
  }
}
