package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import things.exceptions.NetworkException;
import things.locks.RestLock;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by joses on 22/02/2017.
 */
public class TestRestLock {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(80).bindAddress("0.0.0.0"));

  @Test
  public void getLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, true);

    RestLock lock = new RestLock(InetAddress.getLocalHost());

    assertThat(lock.isLocked(), is(true));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void getLockFalse() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, false);

    RestLock lock = new RestLock(InetAddress.getLocalHost());

    assertThat(lock.isLocked(), is(false));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void setLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubPutStatus(wireMockRule, true);

    RestLock lock = new RestLock(InetAddress.getLocalHost());
    lock.setLock(true);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":true}")));
  }

  @Test
  public void setLockFalse() throws UnknownHostException, NetworkException, InterruptedException {
    LockStubs.stubPutStatus(wireMockRule, false);

    RestLock lock = new RestLock(InetAddress.getLocalHost());
    lock.setLock(false);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":false}")));
  }
}
