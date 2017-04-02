package homewatch.server.controllers.locks;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import spark.Request;

public class LockServiceHelper extends ServiceHelper<Lock> {
  private static final HttpThingServiceFactory<Lock> lockServiceFactory = new LockServiceFactory();

  public LockServiceHelper(Request req) {
    super(req);
  }

  public ThingService<Lock> createService() throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> thingService = lockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
