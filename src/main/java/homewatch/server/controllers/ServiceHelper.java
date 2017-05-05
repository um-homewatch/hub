package homewatch.server.controllers;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.server.pojos.ThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.lights.HueLightService;
import spark.QueryParamsMap;
import spark.Request;

public class ServiceHelper<T> {
  private final ThingServiceFactory<T> serviceFactory;

  public ServiceHelper(ThingServiceFactory<T> serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  ThingService<T> createService(Request req) throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<T> thingService = serviceFactory.create(info.getSubType());

      if (thingService instanceof HttpThingService) {
        this.httpService(thingService, req);
      }

      if (thingService instanceof HueLightService) {
        this.hueLightService(thingService, req);
      }

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  private void httpService(ThingService<T> thingService, Request req) {
    HttpThingInfo httpThingInfo = HttpThingInfo.fromQueryString(req.queryMap());

    HttpThingService<T> httpThingService = (HttpThingService<T>) thingService;
    httpThingService.setAddress(httpThingInfo.getAddress());
    httpThingService.setPort(httpThingInfo.getPort());
  }

  private void hueLightService(ThingService<T> thingService, Request req) {
    QueryParamsMap lightIdParam = req.queryMap().get("light_id");

    if (!lightIdParam.hasValue())
      throw new IllegalArgumentException("missing light_id");
    HueLightService hueLightService = (HueLightService) thingService;
    hueLightService.setLightID(lightIdParam.integerValue());
  }
}
