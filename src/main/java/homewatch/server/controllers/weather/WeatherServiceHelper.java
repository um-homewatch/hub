package homewatch.server.controllers.weather;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.pojos.ThingInfo;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;
import spark.Request;

public class WeatherServiceHelper extends ServiceHelper<Weather> {
  private static final ThingServiceFactory<Weather> weatherServiceFactory = new WeatherServiceFactory();

  WeatherServiceHelper(Request req) {
    super(req);
  }

  @Override
  public ThingService<Weather> createService() throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<Weather> weatherThingService = weatherServiceFactory.create(info.getSubType());

      if (weatherThingService instanceof HttpThingService) {
        weatherThingService = this.httpService(weatherThingService);
      }

      return weatherThingService;
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}
