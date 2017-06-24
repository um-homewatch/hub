package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.CoapUtils;
import homewatch.net.JsonResponse;
import homewatch.things.CoapThingService;
import org.eclipse.californium.core.CoapClient;
import org.json.JSONObject;

import java.net.InetAddress;

class CoapLightService extends CoapThingService<Light> {
  CoapLightService() {
    super();
  }

  CoapLightService(InetAddress ipAddress) {
    super(ipAddress);
  }

  CoapLightService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Light get() throws NetworkException {
    JsonResponse jsonResponse = CoapUtils.get(this.getUrl());

    return this.jsonToLight(jsonResponse.getJson());
  }

  @Override
  public Light put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("power", light.isOn());

    JsonResponse jsonResponse = CoapUtils.put(this.getUrl(), json);

    return this.jsonToLight(jsonResponse.getJson());
  }

  @Override
  public boolean ping() {
    try {
      this.get();

      return true;
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
    return "coap";
  }

  @Override
  protected String getUrl() {
    String url = super.getUrl();

    return url + "/status";
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("power").asBoolean();

    return new Light(on);
  }
}
