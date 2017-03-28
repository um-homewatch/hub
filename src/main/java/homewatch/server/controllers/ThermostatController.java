package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.pojos.HttpThingInfo;
import spark.Request;
import spark.Response;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;

import java.io.IOException;

public class ThermostatController {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final HttpThingServiceFactory<Thermostat> thermostatServiceFactory = new ThermostatServiceFactory();

  private ThermostatController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Thermostat> thermostatService = createThermostatService(info);

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
      ThingService<Thermostat> thermostatService = createThermostatService(info);
      Thermostat thermostat = OM.readValue(req.body(), Thermostat.class);

      Thermostat newThermostat = thermostatService.put(thermostat);

      res.status(200);
      return OM.writeValueAsString(newThermostat);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private static ThingService<Thermostat> createThermostatService(HttpThingInfo info) throws NetworkException {
    try {
      return thermostatServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}