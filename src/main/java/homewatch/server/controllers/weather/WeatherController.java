package homewatch.server.controllers.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.NetworkException;
import homewatch.things.ThingService;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class WeatherController {
  private static final ObjectMapper OM = new ObjectMapper();

  private WeatherController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      ThingService<Weather> weatherThingService = new WeatherServiceHelper(req).createService();

      Weather weather = weatherThingService.get();
      res.status(200);

      return OM.writeValueAsString(weather);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }
}