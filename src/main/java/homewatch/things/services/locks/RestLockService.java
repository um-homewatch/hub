package homewatch.things.services.locks;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;
import org.json.JSONObject;

import java.io.IOException;

class RestLockService extends HttpThingService<Lock> {
  RestLockService() {
    super();
  }

  RestLockService(String ipAddress) {
    super(ipAddress);
  }

  RestLockService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Lock get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(this.baseUrl());

      return responseToLock(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Lock put(Lock lock) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("locked", lock.isLocked());

      ThingResponse response = HttpUtils.put(this.baseUrl(), json);

      return responseToLock(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public boolean ping() {
    try {
      return HttpUtils.get(this.baseUrl()).getStatusCode() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "Things::Lock";
  }

  @Override
  public String getSubtype() {
    return "rest";
  }

  private String baseUrl() {
    return this.getUrl() + "/status";
  }

  private Lock responseToLock(ThingResponse thingResponse) throws IOException {
    JsonNode json = JsonUtils.getOM().readTree(thingResponse.getPayload());

    boolean lock = json.get("locked").asBoolean();

    return new Lock(lock);
  }
}