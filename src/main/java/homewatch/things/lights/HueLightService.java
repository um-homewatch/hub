package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.logging.Logger;

public class HueLightService extends HttpThingService<Light> {
  private int lightID;
  private HttpUrl baseUrl;

  HueLightService() {
    super();
  }

  HueLightService(InetAddress ipAddress) {
    super(ipAddress);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/api/newdeveloper/lights");
  }

  HueLightService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/api/newdeveloper/lights");
  }

  public int getLightID() {
    return lightID;
  }

  public void setLightID(int lightID) {
    this.lightID = lightID;
  }

  @Override
  public Light get() throws NetworkException {
    HttpUrl url = HttpUrl.parse(String.format("%s/%d", baseUrl, lightID));
    Logger.getGlobal().info(url.toString());
    JsonNode response = NetUtils.get(url).getJson();

    return this.jsonToLight(response);
  }

  @Override
  public Light put(Light light) throws NetworkException {
    HttpUrl url = HttpUrl.parse(String.format("%s/%d/state", baseUrl, lightID));
    JSONObject json = new JSONObject();
    json.put("on", light.isOn());

    JsonNode response = NetUtils.put(url, json).getJson();

    return light;
  }

  @Override
  public boolean ping() {
    try {
      HttpUrl url = HttpUrl.parse(String.format("%s/%d", baseUrl, lightID));

      return NetUtils.get(url).getResponse().code() == 200;
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
    return "hue";
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("state").get("on").asBoolean();

    return new Light(on);
  }
}
