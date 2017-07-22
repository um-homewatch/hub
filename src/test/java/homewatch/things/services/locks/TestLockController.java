package homewatch.things.services.locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.exceptions.NetworkException;
import homewatch.stubs.LockStubs;
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
public class TestLockController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  // Query string to send on each test
  private static Map<String, Object> QUERY_STRING;
  // JsonObject to change the thing status
  private static JSONObject JSON;

  @BeforeClass
  public static void setup() throws IOException, SAXException {
    QUERY_STRING = new HashMap<>();
    QUERY_STRING.put("address", "localhost");
    QUERY_STRING.put("port", 8080);
    QUERY_STRING.put("subtype", "rest");

    JSON = new JSONObject();
    JSON.put("locked", false);
  }

  @Test
  public void getStatus() throws UnknownHostException, NetworkException, UnirestException {
    LockStubs.stubGetStatus(wireMockRule, true);

    boolean status = Unirest.get("http://localhost:4567/devices/locks").queryString(QUERY_STRING)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("locked");

    assertThat(status, is(true));
  }

  @Test
  public void setStatus() throws UnknownHostException, NetworkException, UnirestException {
    LockStubs.stubPutStatus(wireMockRule, false);
    LockStubs.stubGetStatus(wireMockRule, false);

    boolean status = Unirest.put("http://localhost:4567/devices/locks").queryString(QUERY_STRING).body(JSON)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("locked");

    assertThat(status, is(false));
  }
}
