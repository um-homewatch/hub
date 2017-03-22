package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import server.controllers.pojos.HttpThingInfo;
import spark.Request;
import spark.Response;
import things.HttpThingServiceFactory;
import things.ThingService;
import things.locks.Lock;
import things.locks.LockServiceFactory;

import java.io.IOException;

public class LockController {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final HttpThingServiceFactory<Lock> lockServiceFactory = new LockServiceFactory();

  private LockController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockService = lockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

      res.status(200);
      return OM.writeValueAsString(lockService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  public static String put(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockService = lockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
      Lock lock = OM.readValue(req.body(), Lock.class);

      lockService.put(lock);

      res.status(200);
      return OM.writeValueAsString(lock);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}