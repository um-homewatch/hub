package homewatch.things.services.thermostat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.server.ServerRunner;
import homewatch.stubs.ThermostatStubs;
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
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@PowerMockIgnore("javax.net.ssl.*")
public class TestThermostatController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  // Query string to send on each test
  private static Map<String, Object> QUERY_STRING;

  @BeforeClass
  public static void setup() throws IOException, SAXException {
    QUERY_STRING = new HashMap<>();
    QUERY_STRING.put("address", "localhost");
    QUERY_STRING.put("port", 8080);
    QUERY_STRING.put("subtype", "rest");

    JSONObject JSON = new JSONObject();
    JSON.put("targetTemperature", new Random().nextDouble());
  }

  @Test
  public void getStatus() throws UnknownHostException, NetworkException, UnirestException {
    Thermostat thermostat = new Thermostat(new Random().nextDouble());
    ThermostatStubs.stubGetStatus(wireMockRule, thermostat);

    double targetTemperature = Unirest.get("http://localhost:4567/devices/thermostats").queryString(QUERY_STRING)
        .asJson()
        .getBody()
        .getObject()
        .getDouble("targetTemperature");

    assertThat(targetTemperature, is(thermostat.getTargetTemperature()));
  }

  @Test
  public void setStatus() throws UnknownHostException, NetworkException, UnirestException, JsonProcessingException {
    Thermostat thermostat = new Thermostat(new Random().nextDouble());
    ThermostatStubs.stubPutStatus(wireMockRule, thermostat);

    double targetTemperature = Unirest.put("http://localhost:4567/devices/thermostats")
        .queryString(QUERY_STRING)
        .body(JsonUtils.getOM().writeValueAsString(thermostat))
        .asJson()
        .getBody()
        .getObject()
        .getDouble("targetTemperature");


    assertThat(targetTemperature, is(thermostat.getTargetTemperature()));
  }
}
