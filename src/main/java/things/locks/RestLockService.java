package things.locks;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

class RestLockService extends HttpThingService<Lock> {
  private HttpUrl baseUrl;

  RestLockService() {
    super();
  }

  RestLockService(InetAddress ipAddress) {
    super(ipAddress);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  RestLockService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  @Override
  public Lock get() throws NetworkException {
    JsonNode response = NetUtils.get(baseUrl).getJson();

    boolean lock = response.get("locked").asBoolean();

    return new Lock(lock);
  }

  @Override
  public void put(Lock lock) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("locked", lock.isLocked());

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
    return "lock";
  }

  @Override
  public String getSubType() {
    return "rest";
  }
}