package homewatch.server;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@PowerMockIgnore("javax.net.ssl.*")
public class TestThingController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void errorInvalidArgument() throws UnirestException {
    int status = Unirest.get("http://localhost:4567/devices/lights")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorInvalidSubTypeGet() throws UnirestException {
    int status = Unirest.get("http://localhost:4567/devices/lights")
            .queryString("address", "192.168.1.1")
            .queryString("subtype", "cenas")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorInvalidSubTypePut() throws UnirestException {
    int status = Unirest.put("http://localhost:4567/devices/lights")
            .queryString("address", "192.168.1.1")
            .queryString("subtype", "cenas")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorUnknownHostGet() throws UnirestException {
    int status = Unirest.get("http://localhost:4567/devices/lights")
            .queryString("address", "ehuehuehe")
            .queryString("subtype", "rest")
            .asJson()
            .getStatus();

    assertThat(status, is(404));
  }

  @Test
  public void errorUnknownHostPut() throws UnirestException {
    String payload = String.format("{ \"on\": %b }", new Random().nextBoolean());

    int status = Unirest.put("http://localhost:4567/devices/lights")
            .queryString("address", "ehuehuehe")
            .queryString("subtype", "rest")
            .body(payload)
            .asJson()
            .getStatus();

    assertThat(status, is(404));
  }

  @Test
  public void errorEmptyBodyPut() throws UnirestException {
    String payload = String.format("{ \"on\": %b }", new Random().nextBoolean());

    int status = Unirest.put("http://localhost:4567/devices/lights")
            .queryString("address", "localhost")
            .queryString("subtype", "rest")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorReadOnlyDevicePut() throws UnirestException {
    String payload = String.format("{ \"on\": %b }", new Random().nextBoolean());

    int status = Unirest.put("http://localhost:4567/devices/weather")
            .queryString("subtype", "owm")
            .body(payload)
            .asJson()
            .getStatus();

    assertThat(status, is(404));
  }
}
