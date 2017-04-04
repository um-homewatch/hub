package homewatch.server.controllers;

import homewatch.exceptions.NetworkException;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import spark.Request;

public abstract class ServiceHelper<T> {
  public abstract ThingService<T> createService(Request req) throws NetworkException;

  protected HttpThingService<T> httpService(ThingService<T> thingService, Request req) {
    HttpThingInfo httpThingInfo = HttpThingInfo.fromQueryString(req.queryMap());

    HttpThingService<T> httpThingService = (HttpThingService<T>) thingService;
    httpThingService.setIpAddress(httpThingInfo.getAddress());
    httpThingService.setPort(httpThingInfo.getPort());

    return httpThingService;
  }
}
