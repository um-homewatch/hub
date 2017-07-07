package homewatch.things.services.motionsensors;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import homewatch.constants.JsonUtils;
import homewatch.constants.MotionSensorStubs;
import homewatch.exceptions.NetworkException;
import homewatch.things.ServerRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import java.io.IOException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

@PowerMockIgnore("javax.net.ssl.*")
public class TestMotionSensorController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private MotionSensor originalMotionSensor;

  @Before
  public void setup() {
    this.originalMotionSensor = new MotionSensor(new Random().nextBoolean());
  }

  @Test
  public void getMotionSensorRest() throws IOException, NetworkException, UnirestException {
    MotionSensorStubs.stubGetStatus(wireMockRule, originalMotionSensor.hasMovement());

    String json = Unirest.get("http://localhost:4567/devices/motionsensors")
        .queryString("address", "localhost")
        .queryString("port", 8080)
        .queryString("subtype", "rest")
        .asJson()
        .getBody()
        .toString();

    System.out.println(json);

    MotionSensor motionsensor = JsonUtils.getOM().readValue(json, MotionSensor.class);

    assertEquals(originalMotionSensor, motionsensor);
  }
}
