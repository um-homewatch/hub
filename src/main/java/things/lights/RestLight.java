package things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import things.HttpThing;

import java.net.InetAddress;

public class RestLight extends HttpThing implements Light {
  public RestLight(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLight(InetAddress ipAddress, int port) {
    super(ipAddress, port);
  }

  @Override
  public boolean ping() {
    try {
      getStatus();
    } catch (NetworkException e) {
      return false;
    }
    return true;
  }

  public void setStatus(boolean status) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("power", status);

    NetUtils.put(HttpUrl.parse(this.getUrl() + "/status"), json);
  }

  public boolean getStatus() throws NetworkException {
    JsonNode response = NetUtils.get(HttpUrl.parse(this.getUrl() + "/status"));

    return response.get("power").asBoolean();
  }

  @Override
  public String getSubType() {
    return "rest";
  }
}
