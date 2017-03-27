package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.JsonUtils;
import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import server.controllers.pojos.HttpThingInfo;
import spark.Request;
import spark.Response;
import things.HttpThingServiceFactory;
import things.ThingService;
import things.lights.Light;
import things.lights.LightServiceFactory;

import java.io.IOException;

public class LightController {
  private static final ObjectMapper OM = JsonUtils.getOM();
  private static final HttpThingServiceFactory<Light> lightServiceFactory = new LightServiceFactory();

  private LightController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Light> lightService = createLightService(info);

      res.status(200);
      return OM.writeValueAsString(lightService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public static String put(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Light> lightService = createLightService(info);
      Light light = OM.readValue(req.body(), Light.class);

      Light newLight = lightService.put(light);

      res.status(200);
      return OM.writeValueAsString(newLight);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private static ThingService<Light> createLightService(HttpThingInfo info) throws NetworkException {
    try {
      return lightServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
