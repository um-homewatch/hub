package things.lights;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import constants.LightStubs;
import exceptions.NetworkException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.xml.sax.SAXException;
import things.ServerRunner;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    QUERY_STRING.put("address", "localhost");
    QUERY_STRING.put("port", 8080);
    QUERY_STRING.put("subType", "rest");

    JSON = new JSONObject();
    JSON.put("on", false);
  }

  @Test
  public void getStatus() throws UnknownHostException, NetworkException, UnirestException {
    LightStubs.stubGetStatus(wireMockRule, true);

    boolean status = Unirest.get("http://localhost:4567/lights").queryString(QUERY_STRING)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("on");

    assertThat(status, is(true));
  }

  @Test
  public void setStatus() throws UnknownHostException, NetworkException, UnirestException {
    LightStubs.stubPutStatus(wireMockRule, false);
    LightStubs.stubGetStatus(wireMockRule, false);

    boolean status = Unirest.put("http://localhost:4567/lights").queryString(QUERY_STRING).body(JSON)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("on");

    assertThat(status, is(false));
  }

  @Test
  public void errorInvalidArgument() throws UnirestException {
    LightStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/lights")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorInvalidSubType() throws UnirestException {
    LightStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/lights")
            .queryString("address", "192.168.1.1")
            .queryString("subType", "cenas")
            .asJson()
            .getStatus();


    assertThat(status, is(400));
  }
}
