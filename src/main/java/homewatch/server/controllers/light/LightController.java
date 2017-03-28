package homewatch.server.controllers.light;

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

  private LightController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      ThingService<Light> lightService = new LightServiceHelper(req).createService();

      res.status(200);
      return OM.writeValueAsString(lightService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public static String put(Request req, Response res) throws NetworkException {
    try {
      ThingService<Light> lightService = new LightServiceHelper(req).createService();
      Light light = OM.readValue(req.body(), Light.class);

      Light newLight = lightService.put(light);

      res.status(200);
      return OM.writeValueAsString(newLight);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }
}
