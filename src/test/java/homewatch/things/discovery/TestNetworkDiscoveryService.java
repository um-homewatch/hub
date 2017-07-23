package homewatch.things.discovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.stubs.LightStubs;
import homewatch.things.ThingService;
import homewatch.things.services.lights.Light;
import homewatch.things.services.lights.LightServiceFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NetworkInterfaceUtils.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestNetworkDiscoveryService {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Before
  public void setup() throws UnknownHostException, SocketException {
    List<String> addresses = Collections.singletonList(InetAddress.getLocalHost().getHostName());

    PowerMockito.mockStatic(NetworkInterfaceUtils.class);
    PowerMockito.when(NetworkInterfaceUtils.getAddressesInNetwork()).thenReturn(addresses);
  }

  @Test
  public void discoverRestLights() throws Exception {
    LightStubs.stubGetRest(wireMockRule, true);
    NetworkThingDiscoveryService<Light> networkThingDiscoveryService = new NetworkThingDiscoveryService<>(new LightServiceFactory(), "rest", 8080);

    List<ThingService<Light>> lights = networkThingDiscoveryService.perform();

    assertThat(lights.size(), is(1));
  }
}
