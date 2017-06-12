package homewatch.things.motionsensors;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.net.InetAddress;

class RestMotionSensorService extends HttpThingService<MotionSensor> {
  RestMotionSensorService() {
    super();
  }

  RestMotionSensorService(InetAddress ipAddress) {
    super(ipAddress);
  }

  RestMotionSensorService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public MotionSensor get() throws NetworkException {
    JsonNode response = NetUtils.get(this.baseUrl()).getJson();

    return this.jsonToMotionSensor(response);
  }

  @Override
  public MotionSensor put(MotionSensor motionsensor) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("moving", motionsensor.isMoving());

    JsonNode response = NetUtils.put(this.baseUrl(), json).getJson();

    return this.jsonToMotionSensor(response);
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(this.baseUrl()).getResponse().code() == 200;
    } catch (NetworkException e) {
      LoggerUtils.logException(e);
      return false;
    }
  }

  @Override
  public String getType() {
    return "Things::MotionSensor";
  }

  @Override
  public String getSubtype() {
    return "rest";
  }

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private MotionSensor jsonToMotionSensor(JsonNode json) {
    boolean motionsensor = json.get("moving").asBoolean();

    return new MotionSensor(motionsensor);
  }
}