package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import exceptions.NetworkException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import things.DiscoveryService;
import things.locks.RestLockService;

import java.net.UnknownHostException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class TestDiscoverRestLock {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLocks() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, true);

    List<RestLockService> restLockServiceList = new DiscoveryService<>(RestLockService.class, 8080).discovery();
    assertThat(restLockServiceList.size(), is(1));
  }
}
