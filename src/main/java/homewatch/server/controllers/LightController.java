package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.things.lights.HueLightService;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;

import java.io.IOException;

public class LightController {
  private static final ObjectMapper OM = JsonUtils.getOM();
  private static final HttpThingServiceFactory<Light> lightServiceFactory = new LightServiceFactory();

  private LightController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Light> lightService = createLightService(info, req);

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
      ThingService<Light> lightService = createLightService(info, req);
      Light light = OM.readValue(req.body(), Light.class);

      Light newLight = lightService.put(light);

      res.status(200);
      return OM.writeValueAsString(newLight);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private static ThingService<Light> createLightService(HttpThingInfo info, Request req) throws NetworkException {
    try {
      ThingService<Light> thingService =  lightServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

      if (thingService instanceof HueLightService){
        QueryParamsMap lightIdParam = req.queryMap().get("light_id");
        if (!lightIdParam.hasValue())
          throw new IllegalArgumentException("missing light_id");
        HueLightService hueLightService = (HueLightService) thingService;
        hueLightService.setLightID(lightIdParam.integerValue());
      }

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
