package homewatch.things.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.net.InetAddress;

public class RestThermostatService extends HttpThingService<Thermostat> {
  public RestThermostatService() {
    super();
  }

  public RestThermostatService(InetAddress ipAddress) {
    super(ipAddress);
  }

  public RestThermostatService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Thermostat get() throws NetworkException {
    JsonNode response = NetUtils.get(this.baseUrl()).getJson();

    return this.jsonToThermostat(response);
  }

  @Override
  public Thermostat put(Thermostat thermostat) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("target_temperature", thermostat.getTargetTemperature());

    JsonNode response = NetUtils.put(this.baseUrl(), json).getJson();

    return this.jsonToThermostat(response);
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(this.baseUrl()).getResponse().code() == 200;
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

  private HttpUrl baseUrl(){
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private Thermostat jsonToThermostat(JsonNode json) {
    double targetTemperature = json.get("target_temperature").asDouble();

    return new Thermostat(targetTemperature);
  }
}
