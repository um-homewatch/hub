package homewatch.server.controllers;

import homewatch.exceptions.NetworkException;
import homewatch.things.ThingService;
import spark.Request;

public abstract class ServiceHelper<T> {
  protected final Request req;

  protected ServiceHelper(Request req) {
    this.req = req;
  }

  public abstract ThingService<T> createService() throws NetworkException;
}
