package homewatch.server.controllers.thermostat;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ThermostatController {
  private static final ObjectMapper OM = new ObjectMapper();

  private ThermostatController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Thermostat> thermostatService = new ThermostatServiceHelper(req).createService();

      res.status(200);
      return OM.writeValueAsString(thermostatService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public static String put(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Thermostat> thermostatService = new ThermostatServiceHelper(req).createService();
      Thermostat thermostat = OM.readValue(req.body(), Thermostat.class);

      Thermostat newThermostat = thermostatService.put(thermostat);

      res.status(200);
      return OM.writeValueAsString(newThermostat);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }
}