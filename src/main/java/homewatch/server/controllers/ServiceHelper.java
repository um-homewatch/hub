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

import java.util.HashMap;
import java.util.Map;

public class ServiceHelper<T> {
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

  private Map<String, String> convertQueryMap(QueryParamsMap queryMap){
    Map<String, String[]> mapStringArray = queryMap.toMap();
    Map<String, String> map = new HashMap<>();

    mapStringArray.forEach((k,v) -> map.put(k, v[0]));

    return map;
  }
}
