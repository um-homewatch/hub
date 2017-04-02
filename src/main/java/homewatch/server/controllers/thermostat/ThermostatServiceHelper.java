package homewatch.server.controllers.thermostat;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.pojos.HttpThingInfo;
import homewatch.server.pojos.ThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;
import spark.Request;

public class ThermostatServiceHelper extends ServiceHelper<Thermostat> {
  private static final ThingServiceFactory<Thermostat> thermostatServiceFactory = new ThermostatServiceFactory();

  public ThermostatServiceHelper(Request req) {
    super(req);
  }

  public ThingService<Thermostat> createService() throws NetworkException {
    try {
      ThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
      ThingService<Thermostat> thermostatThingService = thermostatServiceFactory.create(info.getSubType());


      if (thermostatThingService instanceof HttpThingService){
        thermostatThingService = httpService(thermostatThingService);
      }

      return thermostatThingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
