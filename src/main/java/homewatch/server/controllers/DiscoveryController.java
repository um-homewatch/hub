package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.things.DiscoveryService;
import homewatch.things.HttpThingServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class DiscoveryController<T> {
  private final ObjectMapper objectMapper = JsonUtils.getOM();
  private final HttpThingServiceFactory<T> httpThingService;

  public DiscoveryController(HttpThingServiceFactory<T> httpThingService) {
    this.httpThingService = httpThingService;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      String subType = req.queryParams("subType");
      Integer port = req.queryMap("port").integerValue();

      if (subType == null)
        throw new IllegalArgumentException("missing subType query param");

      DiscoveryService<T> discoveryService;

      if (port == null)
        discoveryService = new DiscoveryService<>(httpThingService, subType);
      else
        discoveryService = new DiscoveryService<>(httpThingService, subType, port);

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
