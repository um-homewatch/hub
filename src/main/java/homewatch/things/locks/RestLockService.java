package homewatch.things.locks;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.net.InetAddress;

class RestLockService extends HttpThingService<Lock> {
  RestLockService() {
    super();
  }

  RestLockService(InetAddress ipAddress) {
    super(ipAddress);
  }

  RestLockService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Lock get() throws NetworkException {
    JsonNode response = NetUtils.get(this.baseUrl()).getJson();

    return this.jsonToLock(response);
  }

  @Override
  public Lock put(Lock lock) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("locked", lock.isLocked());

    JsonNode response = NetUtils.put(this.baseUrl(), json).getJson();

    return this.jsonToLock(response);
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
    return "Things::Lock";
  }

  @Override
  public String getSubType() {
    return "rest";
  }

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private Lock jsonToLock(JsonNode json) {
    boolean lock = json.get("locked").asBoolean();

    return new Lock(lock);
  }
}