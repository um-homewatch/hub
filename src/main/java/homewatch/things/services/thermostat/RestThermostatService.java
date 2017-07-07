package homewatch.things.services.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;

public class RestThermostatService extends HttpThingService<Thermostat> {
  public RestThermostatService() {
    super();
  }

  public RestThermostatService(String ipAddress) {
    super(ipAddress);
  }

  public RestThermostatService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Thermostat get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(this.baseUrl());

      return jsonToThermostat(JsonUtils.getOM().readTree(response.getPayload()));
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Thermostat put(Thermostat thermostat) throws NetworkException {
    try {
      JSONObject json = new JSONObject();
      json.put("target_temperature", thermostat.getTargetTemperature());

      ThingResponse response = HttpUtils.put(this.baseUrl(), json);

      return jsonToThermostat(JsonUtils.getOM().readTree(response.getPayload()));
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
    return "Things::Thermostat";
  }

  @Override
  public String getSubtype() {
    return "rest";
  }

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private Thermostat jsonToThermostat(JsonNode json) {
    double targetTemperature = json.get("target_temperature").asDouble();

    return new Thermostat(targetTemperature);
  }
}
