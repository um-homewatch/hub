package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class LockController {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final HttpThingServiceFactory<Lock> lockServiceFactory = new LockServiceFactory();

  private LockController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockService = createLockService(info);

      res.status(200);
      return OM.writeValueAsString(lockService.get());
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public static String put(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockService = createLockService(info);
      Lock lock = OM.readValue(req.body(), Lock.class);

      Lock newLock = lockService.put(lock);

      res.status(200);
      return OM.writeValueAsString(newLock);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private static ThingService<Lock> createLockService(HttpThingInfo info) throws NetworkException {
    try {
      return lockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}