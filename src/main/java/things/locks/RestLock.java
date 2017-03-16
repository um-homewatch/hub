package things.locks;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

public class RestLock extends HttpThingService implements Lock {
  private final OkHttpClient httpClient = new OkHttpClient();

  public RestLock(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLock(InetAddress ipAddress, int port) {
    super(ipAddress, port);
  }

  @Override
  public void setLock(boolean isLocked) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("locked", isLocked);

    NetUtils.put(HttpUrl.parse(this.getUrl() + "/status"), json);
  }

  @Override
  public boolean isLocked() throws NetworkException {
    JsonNode response = NetUtils.get(HttpUrl.parse(this.getUrl() + "/status")).getJson();

    return response.get("locked").asBoolean();
  }

  @Override
  public Object get() throws NetworkException {
    return null;
  }

  @Override
  public boolean ping() {
    try {
      isLocked();
    } catch (NetworkException e) {
      return false;
    }
    return true;
  }

  @Override
  public String getSubType() {
    return "rest";
  }
}