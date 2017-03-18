package lights;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import exceptions.NetworkException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import things.DiscoveryService;
import things.lights.RestLightService;

import java.net.UnknownHostException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDiscoverRestLight {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLights() throws UnknownHostException, NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    List<RestLightService> restLightServiceList = new DiscoveryService<>(RestLightService.class, 8080).discovery();
    assertThat(restLightServiceList.size(), is(1));
  }
}
