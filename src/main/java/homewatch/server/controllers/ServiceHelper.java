package homewatch.server.controllers;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.Thing;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import spark.QueryParamsMap;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

class ServiceHelper<T extends Thing> {
  private final ThingServiceFactory<T> serviceFactory;

  public ServiceHelper(ThingServiceFactory<T> serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  ThingService<T> createService(Request req) throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<T> thingService = serviceFactory.create(info.getSubType());

      thingService.setAttributes(convertQueryMap(req.queryMap()));

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  private Map<String, String> convertQueryMap(QueryParamsMap queryMap) {
    Map<String, String[]> mapStringArray = queryMap.toMap();
    Map<String, String> map = new HashMap<>();

    mapStringArray.forEach((k, v) -> map.put(k, v[0]));

    return map;
  }
}
