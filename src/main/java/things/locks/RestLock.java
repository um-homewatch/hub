package things.locks;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import things.HttpThing;
import things.exceptions.NetworkException;

import java.net.InetAddress;

public class RestLock extends HttpThing implements Lock {
  public RestLock(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLock(InetAddress ipAddress, int port) {
    super(ipAddress, port);
  }

  @Override
  public void setLock(boolean isLocked) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("locked", isLocked);
      Unirest.put(this.getUrl() + "/status").body(json).asJson();
    } catch (UnirestException e) {
      throw new NetworkException(e.getMessage());
    }
  }

  @Override
  public boolean isLocked() throws NetworkException {
    try {
      return Unirest.get(this.getUrl() + "/status").asJson()
              .getBody()
              .getObject()
              .getBoolean("locked");
    } catch (UnirestException e) {
      throw new NetworkException("Couldn't get locks status");
    }
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