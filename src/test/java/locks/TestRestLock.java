package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import exceptions.NetworkException;
import things.locks.RestLock;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestRestLock {
  private static int PORT = 8080;
  
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(PORT).bindAddress("0.0.0.0"));

  @Test
  public void getLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, true);

    RestLock lock = new RestLock(InetAddress.getLocalHost(), PORT);

    assertThat(lock.isLocked(), is(true));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void getLockFalse() throws UnknownHostException, NetworkException {
    LockStubs.stubGetStatus(wireMockRule, false);

    RestLock lock = new RestLock(InetAddress.getLocalHost(), PORT);

    assertThat(lock.isLocked(), is(false));
    verify(getRequestedFor(urlPathEqualTo("/status")));
  }

  @Test
  public void setLockTrue() throws UnknownHostException, NetworkException {
    LockStubs.stubPutStatus(wireMockRule, true);

    RestLock lock = new RestLock(InetAddress.getLocalHost(), PORT);
    lock.setLock(true);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":true}")));
  }

  @Test
  public void setLockFalse() throws UnknownHostException, NetworkException, InterruptedException {
    LockStubs.stubPutStatus(wireMockRule, false);

    RestLock lock = new RestLock(InetAddress.getLocalHost(), PORT);
    lock.setLock(false);

    verify(putRequestedFor(urlPathEqualTo("/status")).withRequestBody(equalTo("{\"locked\":false}")));
  }
}
