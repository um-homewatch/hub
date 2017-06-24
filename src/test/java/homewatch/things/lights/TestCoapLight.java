package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.constants.LightStubs;
import homewatch.exceptions.NetworkException;
import homewatch.net.CoapUtils;
import homewatch.net.JsonResponse;
import homewatch.things.ThingService;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CoapUtils.class)
public class TestCoapLight {
  private static ThingService<Light> lightService;
  private static String ADDRESS;

  @BeforeClass
  public static void setup() throws UnknownHostException, NetworkException {
    ADDRESS = String.format("coap://%s/status", InetAddress.getLocalHost().getHostAddress());

    lightService = new CoapLightService(InetAddress.getLocalHost());
  }

  private void stubGet(boolean power) throws NetworkException, IOException {
    JsonNode json = JsonUtils.getOM().readTree("{\"power\": " + power + "}");

    PowerMockito.mockStatic(CoapUtils.class);
    PowerMockito.when(CoapUtils.get(ADDRESS)).thenReturn(new JsonResponse(json, 205));
  }

  private void stubPut(boolean power) throws NetworkException, IOException {
    JsonNode json = JsonUtils.getOM().readTree("{\"power\": " + power + "}");

    PowerMockito.mockStatic(CoapUtils.class);
    PowerMockito.when(CoapUtils.put(Mockito.eq(ADDRESS), Mockito.any())).thenReturn(new JsonResponse(json, 205));
  }

  @Test
  public void getStatus() throws IOException, NetworkException {
    stubGet(true);

    Light light = lightService.get();

    assertThat(light.isOn(), is(true));
  }

  @Test
  public void getStatusOff() throws IOException, NetworkException {
    stubGet(false);

    Light light = lightService.get();

    assertThat(light.isOn(), is(false));
  }

  @Test
  public void setStatusOn() throws IOException, NetworkException {
    stubPut(true);

    Light light = new Light(true);
    lightService.put(light);

    assertThat(light.isOn(), is(true));
  }

  @Test
  public void setStatusOff() throws IOException, NetworkException, InterruptedException {
    stubPut(false);

    Light light = new Light(false);
    lightService.put(light);

    assertThat(light.isOn(), is(false));
  }

  @Test
  public void testGoodPing() throws IOException, NetworkException {
    stubGet(true);

    boolean ping = lightService.ping();

    assertTrue(ping);
  }

  @Test
  public void testBadPing() throws UnknownHostException, NetworkException {
    PowerMockito.mockStatic(CoapUtils.class);
    PowerMockito.when(CoapUtils.get(ADDRESS)).thenThrow(new NetworkException(500));

    ThingService<Light> badLightService = new CoapLightService(InetAddress.getLocalHost());

    boolean ping = badLightService.ping();

    assertFalse(ping);
  }
}
