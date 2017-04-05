package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.things.ThingService;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ThingController<T> {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ServiceHelper<T> serviceHelper;
  private final Class<T> klass;

  public ThingController(ServiceHelper<T> serviceHelper, Class<T> klass) {
    this.serviceHelper = serviceHelper;
    this.klass = klass;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      ThingService<T> thingService = serviceHelper.createService(req);

      res.status(200);
      return objectMapper.writeValueAsString(thingService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public String put(Request req, Response res) throws NetworkException {
    try {
      ThingService<T> thingService = serviceHelper.createService(req);
      T t = objectMapper.readValue(req.body(), this.klass);

      T newT = thingService.put(t);

      res.status(200);
      return objectMapper.writeValueAsString(newT);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }
}