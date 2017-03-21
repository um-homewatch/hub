package things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import constants.LoggerUtils;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

class RestLightService extends HttpThingService<Light> {
  public RestLightService() {
    super();
  }

  public RestLightService(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLightService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Light get() throws NetworkException {
    JsonNode response = NetUtils.get(HttpUrl.parse(this.getUrl() + "/status")).getJson();

    boolean on = response.get("power").asBoolean();

    return new Light(on);
  }

  @Override
  public void put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("power", light.isOn());

    NetUtils.put(HttpUrl.parse(this.getUrl() + "/status"), json);
  }

  @Override
  public boolean ping() {
    try {
      get();
    } catch (NetworkException e) {
      LoggerUtils.logException(e);
      return false;
    }
    return true;
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
