package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.NetworkThingServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class DiscoveryController<T> {
  private final ObjectMapper objectMapper = JsonUtils.getOM();
  private final NetworkThingServiceFactory<T> httpThingService;

  public DiscoveryController(NetworkThingServiceFactory<T> httpThingService) {
    this.httpThingService = httpThingService;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      String subtype = req.queryParams("subtype");
      Integer port = req.queryMap("port").integerValue();

      if (subtype == null)
        throw new IllegalArgumentException("missing subtype query param");

      DiscoveryService<T> discoveryService;

      if (port == null)
        discoveryService = new DiscoveryService<>(httpThingService, subtype);
      else
        discoveryService = new DiscoveryService<>(httpThingService, subtype, port);

      res.status(200);

      return objectMapper.writeValueAsString(discoveryService.discovery());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
