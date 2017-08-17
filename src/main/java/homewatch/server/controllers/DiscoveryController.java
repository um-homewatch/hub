package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class DiscoveryController<T extends Thing> {
  private final ObjectMapper objectMapper = JsonUtils.getOM();
  private final ThingServiceFactory<T> httpThingService;

  public DiscoveryController(ThingServiceFactory<T> httpThingService) {
    this.httpThingService = httpThingService;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      String subtype = req.queryParams("subtype");
      Integer port = req.queryMap("port").integerValue();

      if (subtype == null)
        throw new IllegalArgumentException("missing subtype query param");

      NetworkThingDiscoveryService<T> networkThingDiscoveryService;

      if (port == null)
        networkThingDiscoveryService = new NetworkThingDiscoveryService<>(httpThingService, subtype);
      else
        networkThingDiscoveryService = new NetworkThingDiscoveryService<>(httpThingService, subtype, port);

      res.status(200);

      return objectMapper.writeValueAsString(networkThingDiscoveryService.perform());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
