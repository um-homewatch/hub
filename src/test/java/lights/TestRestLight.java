package lights;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lights.LightStubs;
import org.junit.Rule;
import org.junit.Test;
import things.exceptions.NetworkException;
import things.lights.RestLight;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestRestLight {
  private static int PORT = 8080;
  
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(PORT).bindAddress("0.0.0.0"));

  @Test
  public void getStatus() throws UnknownHostException, NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    RestLight light = new RestLight(InetAddress.getLocalHost(), PORT);

    assertThat(light.getStatus(), is(true));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void getStatusOff() throws UnknownHostException, NetworkException {
    LightStubs.stubGetStatus(wireMockRule, false);

    RestLight light = new RestLight(InetAddress.getLocalHost(), PORT);

    assertThat(light.getStatus(), is(false));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void setStatusOn() throws UnknownHostException, NetworkException {
    LightStubs.stubPutStatus(wireMockRule, true);

    RestLight light = new RestLight(InetAddress.getLocalHost(), PORT);
    light.setStatus(true);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"power\":true}")));
  }

  @Test
  public void setStatusOff() throws UnknownHostException, NetworkException, InterruptedException {
    LightStubs.stubPutStatus(wireMockRule, false);

    RestLight light = new RestLight(InetAddress.getLocalHost(), PORT);
    light.setStatus(false);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"power\":false}")));
  }
}
