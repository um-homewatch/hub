package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

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
    JsonNode response = NetUtils.get(this.baseUrl()).getJson();

    return this.jsonToLight(response);
  }

  @Override
  public Light put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("power", light.isOn());
    JsonNode response = NetUtils.put(this.baseUrl(), json).getJson();

    return this.jsonToLight(response);
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
