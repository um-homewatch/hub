package homewatch.server.controllers.locks;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.pojos.ThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import spark.Request;

public class LockServiceHelper extends ServiceHelper<Lock> {
  private static final ThingServiceFactory<Lock> lockServiceFactory = new LockServiceFactory();

  public LockServiceHelper(Request req) {
    super(req);
  }

  @Override
  public ThingService<Lock> createService() throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());
      ThingService<Lock> lockThingService = lockServiceFactory.create(info.getSubType());

      if (lockThingService instanceof HttpThingService) {
        lockThingService = this.httpService(lockThingService);
      }

      return lockThingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
