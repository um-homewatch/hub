package homewatch.server.controllers.weather;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.server.controllers.pojos.ThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;
import spark.Request;

public class WeatherServiceHelper extends ServiceHelper<Weather> {
  private static ThingServiceFactory<Weather> weatherServiceFactory = new WeatherServiceFactory();

  protected WeatherServiceHelper(Request req) {
    super(req);
  }

  @Override
  public ThingService<Weather> createService() throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<Weather> weatherThingService = weatherServiceFactory.create(info.getSubType());

      if (weatherThingService instanceof HttpThingService) {
        weatherThingService = httpThingService(weatherThingService);
      }

      return weatherThingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }

  private ThingService<Weather> httpThingService(ThingService<Weather> thingService) {
    HttpThingInfo httpThingInfo = HttpThingInfo.fromQueryString(req.queryMap());

    HttpThingService<Weather> weatherHttpThingService = (HttpThingService<Weather>) thingService;
    weatherHttpThingService.setIpAddress(httpThingInfo.getAddress());
    weatherHttpThingService.setPort(httpThingInfo.getPort());

    return weatherHttpThingService;
  }
}
