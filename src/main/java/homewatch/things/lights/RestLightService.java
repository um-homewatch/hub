package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.*;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;

class RestLightService extends HttpThingService<Light> {
  RestLightService() {
    super();
  }

  RestLightService(InetAddress ipAddress) {
    super(ipAddress);
  }

  RestLightService(InetAddress ipAddress, Integer port) {
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
      LoggerUtils.logException(e);
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

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("power").asBoolean();

    return new Light(on);
  }
}
