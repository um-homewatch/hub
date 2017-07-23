package homewatch.things.services.motionsensors;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.exceptions.ReadOnlyDeviceException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;

import java.io.IOException;

class RestMotionSensorService extends HttpThingService<MotionSensor> {
  RestMotionSensorService() {
    super();
  }

  RestMotionSensorService(String ipAddress) {
    super(ipAddress);
  }

  RestMotionSensorService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public MotionSensor get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(this.baseUrl());

      return responseToMotionSensor(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public MotionSensor put(MotionSensor motionsensor) throws NetworkException {
    throw new ReadOnlyDeviceException();
  }

  @Override
  public boolean ping() {
    try {
      return HttpUtils.get(this.baseUrl()).getStatusCode() == 200;
    } catch (NetworkException e) {
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

  private MotionSensor responseToMotionSensor(ThingResponse thingResponse) throws IOException {
    JsonNode json = JsonUtils.getOM().readTree(thingResponse.getPayload());

    boolean motionsensor = json.get("movement").asBoolean();

    return new MotionSensor(motionsensor);
  }
}