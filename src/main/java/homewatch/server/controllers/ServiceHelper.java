package homewatch.server.controllers;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.Thing;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import spark.Request;

class ServiceHelper<T extends Thing> {
  private final ThingServiceFactory<T> serviceFactory;

  public ServiceHelper(ThingServiceFactory<T> serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  ThingService<T> createThingService(Request req) throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<T> thingService = serviceFactory.createThingService(info.getSubType());

      thingService.setAttributes(QueryStringUtils.convertQueryMap(req.queryMap()));

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
