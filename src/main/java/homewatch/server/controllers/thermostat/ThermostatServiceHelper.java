package homewatch.server.controllers.thermostat;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;
import spark.Request;

public class ThermostatServiceHelper extends ServiceHelper<Thermostat> {
  private static final HttpThingServiceFactory<Thermostat> thermostatServiceFactory = new ThermostatServiceFactory();

  public ThermostatServiceHelper(Request req) {
    super(req);
  }

  public ThingService<Thermostat> createService() throws NetworkException {
    try {
      HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Thermostat> thingService = thermostatServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

      return thingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
