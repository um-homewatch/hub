package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import exceptions.NetworkException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import things.ThingService;
import things.locks.Lock;
import things.locks.RestLockService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestRestLock {
  private static final int PORT = 8080;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(PORT).bindAddress("0.0.0.0"));

  private static ThingService<Lock> lockService;

  @BeforeClass
  public static void setup() throws UnknownHostException {
    lockService = new RestLockService(InetAddress.getLocalHost(), PORT);
  }

  @Test
  public void getLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, true);

    Lock lock = lockService.get();

    assertThat(lock.isLocked(), is(true));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void getLockFalse() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, false);

    Lock lock = lockService.get();

    assertThat(lock.isLocked(), is(false));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void setLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubPutStatus(wireMockRule, true);

    Lock lock = new Lock(true);
    lockService.put(lock);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":true}")));
  }

  @Test
  public void setLockFalse() throws UnknownHostException, NetworkException, InterruptedException {
    LockStubs.stubPutStatus(wireMockRule, false);

    Lock lock = new Lock(false);
    lockService.put(lock);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":false}")));
  }
}
