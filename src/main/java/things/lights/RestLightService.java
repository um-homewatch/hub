package things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

class RestLightService extends HttpThingService<Light> {
  private HttpUrl baseUrl;

  RestLightService() {
    super();
  }

  RestLightService(InetAddress ipAddress) {
    super(ipAddress);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  RestLightService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  @Override
  public Light get() throws NetworkException {
    JsonNode response = NetUtils.get(baseUrl).getJson();

    boolean on = response.get("power").asBoolean();

    return new Light(on);
  }

  @Override
  public void put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("power", light.isOn());

    NetUtils.put(baseUrl, json);
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(baseUrl, 500, 500).getResponse().code() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "light";
  }

  @Override
  public String getSubType() {
    return "rest";
  }
}
