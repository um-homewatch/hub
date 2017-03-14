package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import things.DiscoveryService;
import things.exceptions.NetworkException;
import things.locks.RestLock;

import java.net.UnknownHostException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by joses on 22/02/2017.
 */
public class TestDiscoverRestLock {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLocks() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, true);

    List<RestLock> restLockList = new DiscoveryService<>(RestLock.class, 8080).discovery();
    assertThat(restLockList.size(), is(1));
  }
}
