package things.lights;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import things.HttpThing;
import things.exceptions.NetworkException;

import java.net.InetAddress;

/**
 * Created by joses on 22/02/2017.
 */
public class RestLight extends HttpThing implements Light {
  public RestLight(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestLight(InetAddress ipAddress, int port) {
    super(ipAddress, port);
  }

  @Override
  public boolean ping() {
    Unirest.setTimeouts(1000, 500);
    try {
      getStatus();
    } catch (NetworkException e) {
      return false;
    }
    return true;
  }

  public void setStatus(boolean status) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("power", status);
      Unirest.put(this.getUrl() + "/status").body(json).asJson();
    } catch (UnirestException e) {
      throw new NetworkException(e.getMessage());
    }
  }

  public boolean getStatus() throws NetworkException {
    try {
      return Unirest.get(this.getUrl() + "/status").asJson()
              .getBody()
              .getObject()
              .getBoolean("power");
    } catch (UnirestException e) {
      throw new NetworkException("Couldn't get lights status");
    }
  }

  @Override
  public String getSubType() {
    return "rest";
  }
}
