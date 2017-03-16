package things.locks;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

public class RestLockService extends HttpThingService<Lock> {
  private final OkHttpClient httpClient = new OkHttpClient();

  public RestLockService(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLockService(InetAddress ipAddress, int port) {
    super(ipAddress, port);
  }

  @Override
  public Lock get() throws NetworkException {
    JsonNode response = NetUtils.get(HttpUrl.parse(this.getUrl() + "/status")).getJson();

    boolean lock = response.get("locked").asBoolean();

    return new Lock(lock);
  }

  @Override
  public void put(Lock lock) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("locked", lock.isLocked());

    NetUtils.put(HttpUrl.parse(this.getUrl() + "/status"), json);
  }

  @Override
  public boolean ping() {
    try {
      get();
    } catch (NetworkException e) {
      return false;
    }
    return true;
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