package things.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import things.HttpThingService;

import java.net.InetAddress;

public class RestThermostatService extends HttpThingService<Thermostat> {
  private HttpUrl baseUrl;

  public RestThermostatService() {
    super();
  }

  public RestThermostatService(InetAddress ipAddress) {
    super(ipAddress);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  public RestThermostatService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
    this.baseUrl = HttpUrl.parse(this.getUrl() + "/status");
  }

  @Override
  public Thermostat get() throws NetworkException {
    JsonNode response = NetUtils.get(this.baseUrl).getJson();

    return this.jsonToThermostat(response);
  }

  @Override
  public Thermostat put(Thermostat thermostat) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("target_temperature", thermostat.getTargetTemperature());

    JsonNode response = NetUtils.put(this.baseUrl, json).getJson();

    return this.jsonToThermostat(response);
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(this.baseUrl).getResponse().code() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "thermostat";
  }

  @Override
  public String getSubType() {
    return "rest";
  }

  private Thermostat jsonToThermostat(JsonNode json) {
    double targetTemperature = json.get("target_temperature").asDouble();

    return new Thermostat(targetTemperature);
  }
}
