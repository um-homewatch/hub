package homewatch.server.controllers.lights;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.lights.HueLightService;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;
import spark.QueryParamsMap;
import spark.Request;

public class LightServiceHelper extends ServiceHelper<Light> {
  private static final ThingServiceFactory<Light> lightServiceFactory = new LightServiceFactory();

  public LightServiceHelper(Request req) {
    super(req);
  }

  public ThingService<Light> createService() throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Light> lightThingService = lightServiceFactory.create(info.getSubType());

      if (lightThingService instanceof HttpThingService) {
        lightThingService = this.httpService(lightThingService);
      }

      if (lightThingService instanceof HueLightService) {
        lightThingService = hueLightService(lightThingService);
      }

      return lightThingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  private ThingService<Light> hueLightService(ThingService<Light> thingService) {
    QueryParamsMap lightIdParam = req.queryMap().get("light_id");

    if (!lightIdParam.hasValue())
      throw new IllegalArgumentException("missing light_id");
    HueLightService hueLightService = (HueLightService) thingService;
    hueLightService.setLightID(lightIdParam.integerValue());

    return hueLightService;
  }
}
