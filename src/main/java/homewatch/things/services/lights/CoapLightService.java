package homewatch.things.services.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.CoapUtils;
import homewatch.net.ThingResponse;
import homewatch.things.CoapThingService;
import org.json.JSONObject;

import java.io.IOException;

class CoapLightService extends CoapThingService<Light> {
  CoapLightService() {
    super();
  }

  CoapLightService(String ipAddress) {
    super(ipAddress);
  }

  CoapLightService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Light get() throws NetworkException {
    try {
      ThingResponse response = CoapUtils.get(this.getUrl());

      return responseToLight(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Light put(Light light) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("power", light.isOn());

      ThingResponse response = CoapUtils.put(this.getUrl(), json.toString().getBytes("UTF-8"), CoapUtils.JSON_FORMAT);

      return responseToLight(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public boolean ping() {
    try {
      this.get();

      return true;
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
    return "coap";
  }

  @Override
  protected String getUrl() {
    String url = super.getUrl();

    return url + "/status";
  }

  private Light responseToLight(ThingResponse response) throws IOException {
    JsonNode json = JsonUtils.getOM().readTree(response.getPayload());

    boolean on = json.get("power").asBoolean();

    return new Light(on);
  }
}
