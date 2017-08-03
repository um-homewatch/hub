package homewatch.things.services.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;
import org.json.JSONObject;

import java.io.IOException;

class RestLightService extends HttpThingService<Light> {
  RestLightService() {
    super();
  }

  RestLightService(String ipAddress) {
    super(ipAddress);
  }

  RestLightService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Light get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(this.baseUrl());

      JsonNode jsonResponse = JsonUtils.getOM().readTree(response.getPayload());

      return this.jsonToLight(jsonResponse);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Light put(Light light) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("power", light.isOn());

      JsonNode response = JsonUtils.getOM().readTree(HttpUtils.put(this.baseUrl(), json).getPayload());

      return this.jsonToLight(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
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
    return "Things::Light";
  }

  @Override
  public String getSubtype() {
    return "rest";
  }

  private String baseUrl() {
    return this.getUrl() + "/status";
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("power").asBoolean();

    return new Light(on);
  }
}
