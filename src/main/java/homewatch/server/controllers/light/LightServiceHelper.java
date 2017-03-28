package homewatch.server.controllers.light;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.lights.HueLightService;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;
import spark.QueryParamsMap;
import spark.Request;

public class LightServiceHelper {
  private static final HttpThingServiceFactory<Light> lightServiceFactory = new LightServiceFactory();
  private final Request req;

  public LightServiceHelper(Request req) {
    this.req = req;
  }

  public ThingService<Light> createService() throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Light> thingService = lightServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

      if (thingService instanceof HueLightService) {
        hueLightService(thingService);
      }

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  private void hueLightService(ThingService<Light> thingService) {
    QueryParamsMap lightIdParam = req.queryMap().get("light_id");
    if (!lightIdParam.hasValue())
      throw new IllegalArgumentException("missing light_id");
    HueLightService hueLightService = (HueLightService) thingService;
    hueLightService.setLightID(lightIdParam.integerValue());
  }
}
