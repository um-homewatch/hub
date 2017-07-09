package homewatch.things.services.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class HueLightService extends HttpThingService<Light> {
  private int lightID;

  HueLightService() {
    super();
  }

  HueLightService(String ipAddress) {
    super(ipAddress);
  }

  HueLightService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  public int getLightID() {
    return lightID;
  }

  public void setLightID(int lightID) {
    this.lightID = lightID;
  }

  @Override
  public Light get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(HttpUrl.parse(String.format("%s/%d", this.baseUrl(), lightID)));

      return this.jsonToLight(JsonUtils.getOM().readTree(response.getPayload()));
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Light put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("on", light.isOn());

    HttpUtils.put(HttpUrl.parse(String.format("%s/%d/state", this.baseUrl(), lightID)), json);

    return light;
  }

  @Override
  public void setAttributes(Map<String, ?> attributes) {
    super.setAttributes(attributes);

    String lightIDString = this.getAttribute(attributes, "light_id", String.class);

    if (lightIDString == null) throw new IllegalArgumentException("missing light_id");

    try {
      this.lightID = Integer.parseInt(lightIDString);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("light_id must be a number");
    }
  }

  @Override
  public boolean ping() {
    try {
      HttpUrl url = HttpUrl.parse(String.format("%s/%d", this.baseUrl(), lightID));

      return HttpUtils.get(url).getStatusCode() == 200;
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
    return "hue";
  }

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/api/newdeveloper/lights");
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("state").get("on").asBoolean();

    return new Light(on);
  }
}
