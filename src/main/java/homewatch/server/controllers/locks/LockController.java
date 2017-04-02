package homewatch.server.controllers.locks;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.things.ThingService;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class LockController {
  private static final ObjectMapper OM = new ObjectMapper();

  private LockController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockService = new LockServiceHelper(req).createService();

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
      ThingService<Lock> lockService = new LockServiceHelper(req).createService();
      Lock lock = OM.readValue(req.body(), Lock.class);

      Lock newLock = lockService.put(lock);

      res.status(200);
      return OM.writeValueAsString(newLock);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }
}