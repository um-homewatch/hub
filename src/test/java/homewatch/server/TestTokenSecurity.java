package homewatch.server;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.stubs.LightStubs;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestTokenSecurity extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void testTokenSize() throws UnirestException, IOException {
    String body = Unirest.get("http://localhost:4567/token")
        .asString()
        .getBody();

    assertThat(body.length(), is(1024));
  }

  @Test
  public void testTokenAuthSuccess() throws UnirestException, IOException {
    LightStubs.stubGetRest(wireMockRule, true);
    String token = Unirest.get("http://localhost:4567/token").asString().getBody();

    int status = Unirest.get("http://localhost:4567/devices/lights")
        .header("Authorization", token)
        .queryString("address", "127.0.0.1")
        .queryString("port", 8080)
        .queryString("subtype", "rest")
        .asString()
        .getStatus();

    assertThat(status, is(200));
  }

  @Test
  public void testTokenAuthFail() throws UnirestException, IOException {
    LightStubs.stubGetRest(wireMockRule, true);
    Unirest.get("http://localhost:4567/token").asString().getBody();
    String token = "THIS-IS-WRONG-TOKEN";

    int status = Unirest.get("http://localhost:4567/devices/lights")
        .header("Authorization", token)
        .queryString("address", "127.0.0.1")
        .queryString("port", 8080)
        .queryString("subtype", "rest")
        .asString()
        .getStatus();

    assertThat(status, is(401));
  }
}
