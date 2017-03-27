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

    return this.jsonToLock(response);
  }

  @Override
  public Lock put(Lock lock) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("locked", lock.isLocked());

    JsonNode response = NetUtils.put(baseUrl, json).getJson();

    return this.jsonToLock(response);
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(baseUrl).getResponse().code() == 200;
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

  private Lock jsonToLock(JsonNode json) {
    boolean lock = json.get("locked").asBoolean();

    return new Lock(lock);
  }
}