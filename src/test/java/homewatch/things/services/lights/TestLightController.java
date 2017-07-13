package homewatch.things.services.lights;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.exceptions.NetworkException;
import homewatch.stubs.LightStubs;
import homewatch.things.ServerRunner;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@PowerMockIgnore("javax.net.ssl.*")
public class TestLightController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  // Query string to send on each test
  private static Map<String, Object> QUERY_STRING;
  // JsonObject to change the thing status
  private static JSONObject JSON;

  @BeforeClass
  public static void setup() throws IOException, SAXException {
    QUERY_STRING = new HashMap<>();
    QUERY_STRING.put("address", "127.0.0.1");
    QUERY_STRING.put("port", 8080);
    QUERY_STRING.put("subtype", "rest");

    JSON = new JSONObject();
    JSON.put("on", false);
  }

  @Test
  public void getRest() throws UnknownHostException, NetworkException, UnirestException {
    LightStubs.stubGetRest(wireMockRule, true);

    boolean status = Unirest.get("http://127.0.0.1:4567/devices/lights")
        .queryString(QUERY_STRING)
        .asJson()
        .getBody()
        .getObject()
        .getBoolean("on");

    assertThat(status, is(true));
  }

  @Test
  public void putRest() throws UnknownHostException, NetworkException, UnirestException {
    LightStubs.stubPutRest(wireMockRule, false);
    LightStubs.stubGetRest(wireMockRule, false);

    boolean status = Unirest.put("http://localhost:4567/devices/lights").queryString(QUERY_STRING).body(JSON)
        .asJson()
        .getBody()
        .getObject()
        .getBoolean("on");

    assertThat(status, is(false));
  }

  @Test
  public void getHue() throws UnirestException {
    LightStubs.stubGetHue(wireMockRule, true);

    boolean status = Unirest.get("http://localhost:4567/devices/lights")
        .queryString("subtype", "hue")
        .queryString("address", "localhost")
        .queryString("port", 8080)
        .queryString("light_id", 1)
        .asJson()
        .getBody()
        .getObject()
        .getBoolean("on");

    assertThat(status, is(true));
  }

  @Test
  public void putHue() throws UnirestException {
    LightStubs.stubPutHue(wireMockRule, false);

    boolean status = Unirest.put("http://localhost:4567/devices/lights")
        .queryString("subtype", "hue")
        .queryString("address", "localhost")
        .queryString("port", 8080)
        .queryString("light_id", 1)
        .body(JSON)
        .asJson()
        .getBody()
        .getObject()
        .getBoolean("on");

    assertThat(status, is(false));
  }
}
