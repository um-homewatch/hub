package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.constants.JsonUtils;
import homewatch.constants.LightStubs;
import homewatch.exceptions.NetworkException;
import homewatch.things.ThingService;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestHueLight {
  private static final int PORT = 8080;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(PORT).bindAddress("0.0.0.0"));

  private static HueLightService lightService;

  private static JsonNode HUE_RESPONSE;

  @BeforeClass
  public static void setup() throws IOException {
    lightService = new HueLightService(InetAddress.getLocalHost(), PORT);
    lightService.setLightID(1);

    HUE_RESPONSE = JsonUtils.getOM().readTree(new File("src/test/fixtures/hue/get.json"));
  }

  @Test
  public void getLightStatusTrue() throws UnknownHostException, NetworkException {
    LightStubs.stubGetHue(wireMockRule, true);

    Light light = lightService.get();

    assertThat(light.isOn(), is(true));
    verify(getRequestedFor(urlPathEqualTo("/api/newdeveloper/lights/1")));
  }

  @Test
  public void getLightStatusFalse() throws UnknownHostException, NetworkException {
    LightStubs.stubGetHue(wireMockRule, false);

    Light light = lightService.get();

    assertThat(light.isOn(), is(false));
    verify(getRequestedFor(urlPathEqualTo("/api/newdeveloper/lights/1")));
  }

  @Test
  public void turnLightOn() throws IOException, NetworkException {
    LightStubs.stubPutHue(wireMockRule, true);

    Light light = new Light(true);
    lightService.put(light);

    verify(putRequestedFor(urlPathEqualTo("/api/newdeveloper/lights/1/state")).withRequestBody(equalTo("{\"on\":true}")));
  }

  @Test
  public void turnLightOff() throws IOException, NetworkException, InterruptedException {
    LightStubs.stubPutHue(wireMockRule, false);

    Light light = new Light(false);
    lightService.put(light);

    verify(putRequestedFor(urlPathEqualTo("/api/newdeveloper/lights/1/state")).withRequestBody(equalTo("{\"on\":false}")));
  }

  @Test
  public void testGoodPing() {
    LightStubs.stubGetHue(wireMockRule, true);

    boolean ping = lightService.ping();

    assertTrue(ping);
  }

  @Test
  public void testBadPing() throws UnknownHostException {
    ThingService<Light> badLightService = new HueLightService(InetAddress.getLocalHost());

    boolean ping = badLightService.ping();

    assertFalse(ping);
  }
}
