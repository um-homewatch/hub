package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import spark.Request;
import spark.Response;
import things.weather.Weather;
import things.weather.WeatherFactory;

import java.io.IOException;

public class WeatherController {
  private static final ObjectMapper OM = new ObjectMapper();

  public static String get(Request req, Response res) throws IOException, InvalidSubTypeException, NetworkException {
    if (!req.queryMap("city").hasValue()) {
      res.status(400);
      return OM.writeValueAsString(new ErrorMessage("missing params: city"));
    }

    String city = req.queryMap("city").value();
    Weather weather = WeatherFactory.create(city, "owm");

    return OM.writeValueAsString(weather);
  }
}