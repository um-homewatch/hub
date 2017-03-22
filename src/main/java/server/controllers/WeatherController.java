package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import server.ErrorMessage;
import spark.Request;
import spark.Response;
import things.weather.Weather;
import things.weather.WeatherServiceFactory;

import java.io.IOException;

public class WeatherController {
  private static final ObjectMapper OM = new ObjectMapper();
  private static final WeatherServiceFactory weatherServiceFactory = new WeatherServiceFactory();

  private WeatherController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      if (!req.queryMap("city").hasValue()) {
        res.status(400);
        return OM.writeValueAsString(new ErrorMessage("missing params: city"));
      }

      String city = req.queryMap("city").value();
      Weather weather = weatherServiceFactory.create(city, "owm").get();

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