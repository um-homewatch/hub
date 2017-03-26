package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import server.controllers.pojos.HttpThingInfo;
import spark.Request;
import spark.Response;
import things.HttpThingServiceFactory;
import things.ThingService;
import things.thermostat.Thermostat;
import things.thermostat.ThermostatServiceFactory;

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

      thermostatService.put(thermostat);

      res.status(200);
      return OM.writeValueAsString(thermostat);
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