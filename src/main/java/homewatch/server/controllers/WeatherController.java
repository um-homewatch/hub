package homewatch.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.pojos.HttpThingInfo;
import homewatch.server.controllers.pojos.ThingInfo;
import spark.Request;
import spark.Response;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;

import java.io.IOException;

public class WeatherController {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final WeatherServiceFactory weatherServiceFactory = new WeatherServiceFactory();

  private WeatherController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      ThingInfo info = ThingInfo.fromQueryString(req.queryMap());

      ThingService<Weather> weatherThingService = weatherServiceFactory.create(info.getSubType());

      if (weatherThingService instanceof HttpThingService) {
        HttpThingInfo httpThingInfo = HttpThingInfo.fromQueryString(req.queryMap());
        HttpThingService<Weather> weatherHttpThingService = (HttpThingService<Weather>) weatherThingService;
        weatherHttpThingService.setIpAddress(httpThingInfo.getAddress());
        weatherHttpThingService.setPort(httpThingInfo.getPort());
      }

      Weather weather = weatherThingService.get();
      res.status(200);

      return OM.writeValueAsString(weather);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    } catch (InvalidSubTypeException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 400);
    }
  }
}