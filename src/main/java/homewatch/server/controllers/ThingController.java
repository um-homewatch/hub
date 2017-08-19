package homewatch.server.controllers;

import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.things.Thing;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ThingController<T extends Thing> {
  private final ServiceHelper<T> serviceHelper;
  private final Class<T> klass;

  public ThingController(ThingServiceFactory<T> thingServiceFactory, Class<T> klass) {
    this.serviceHelper = new ServiceHelper<>(thingServiceFactory);
    this.klass = klass;
  }

  public String get(Request req, Response res) throws NetworkException {
    try {
      ThingService<T> thingService = serviceHelper.createThingService(req);

      res.status(200);
      return JsonUtils.getOM().writeValueAsString(thingService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public String put(Request req, Response res) throws NetworkException {
    try {
      ThingService<T> thingService = serviceHelper.createThingService(req);
      T thing = this.bodyToThing(req.body());

      T newThing = thingService.put(thing);

      res.status(200);
      return JsonUtils.getOM().writeValueAsString(newThing);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private T bodyToThing(String body) throws NetworkException {
    try {
      return JsonUtils.getOM().readValue(body, this.klass);
    } catch (IOException e) {
      throw new NetworkException(e, 400);
    }
  }
}