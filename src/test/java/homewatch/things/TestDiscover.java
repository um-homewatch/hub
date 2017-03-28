package homewatch.things;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.constants.LightStubs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DiscoveryService.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestDiscover {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLights() throws Exception {
    LightStubs.stubGetStatus(wireMockRule, true);

    DiscoveryService<Light> discoveryService = spy(new DiscoveryService<>(new LightServiceFactory(), "rest", 8080));

    doReturn(Arrays.asList(InetAddress.getLocalHost().getHostAddress())).when(discoveryService, "getAddressList");

    List<HttpThingService<Light>> lights = discoveryService.discovery();

    assertThat(lights.size(), is(1));
  }
}
