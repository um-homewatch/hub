package lights;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import things.DiscoveryService;
import things.exceptions.NetworkException;
import things.lights.RestLight;

import java.net.UnknownHostException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by joses on 22/02/2017.
 */
public class TestDiscoverRestLight {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(80).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLights() throws UnknownHostException, NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    List<RestLight> restLightList = new DiscoveryService<>(RestLight.class).discovery();
    assertThat(restLightList.size(), is(1));
  }
}
