package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.JsonUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
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

  public static String get(Request req, Response res) throws IOException, InvalidSubTypeException, NetworkException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    ThingService<Light> lightService = lightServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

    return OM.writeValueAsString(lightService.get());
  }

  public static String put(Request req, Response res) throws NetworkException, IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    ThingService<Light> lightService = lightServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    Light light = OM.readValue(req.body(), Light.class);

    lightService.put(light);

    return OM.writeValueAsString(light);
  }
}
