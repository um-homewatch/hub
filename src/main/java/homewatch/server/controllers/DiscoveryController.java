package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.DiscoveryService;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class DiscoveryController<T extends Thing> {
  private final ObjectMapper objectMapper = JsonUtils.getOM();
  private final ThingServiceFactory<T> thingServiceFactory;

  public DiscoveryController(ThingServiceFactory<T> httpThingService) {
    this.thingServiceFactory = httpThingService;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      String subtype = req.queryParams("subtype");

      if (subtype == null)
        throw new IllegalArgumentException("missing subtype query param");

      DiscoveryService discoveryService = this.thingServiceFactory.createDiscoveryService(subtype);
      if (discoveryService == null) {
        throw new IllegalArgumentException("device subtype has no discovery candidate at the moment");
      }
      discoveryService.setAttributes(QueryStringUtils.convertQueryMap(req.queryMap()));
      res.status(200);

      return objectMapper.writeValueAsString(discoveryService.perform());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
