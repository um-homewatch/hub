package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.JsonUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import org.xml.sax.SAXException;
import server.controllers.LightController;
import server.controllers.LockController;
import server.controllers.WeatherController;
import spark.Spark;
import things.DiscoveryService;
import things.ThingService;
import things.lights.RestLightService;
import things.locks.RestLockService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
  private static final ObjectMapper OM = JsonUtils.getOM();

  public static void main(String[] args) throws IOException, SAXException {
    Map<String, ThingService> things = new HashMap<>();

    Spark.get("/lights/discover", (req, res) -> {
      DiscoveryService<RestLightService> discoveryService = new DiscoveryService<>(RestLightService.class);
      return OM.writeValueAsString(discoveryService.discovery());
    });

    Spark.get("/locks/discover", (req, res) -> {
      DiscoveryService<RestLockService> discoveryService = new DiscoveryService<>(RestLockService.class);
      return OM.writeValueAsString(discoveryService.discovery());
    });

    Spark.get("/lights", LightController::get);
    Spark.put("/lights", LightController::put);

    Spark.get("/locks", LockController::get);
    Spark.put("/locks", LockController::put);

    Spark.get("/weather", WeatherController::get);


    Spark.exception(IllegalArgumentException.class, (exception, req, res) -> {
      res.status(400);
      res.body(exceptionToString(exception));
      exception.printStackTrace();
    });

    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> {
      res.status(400);
      res.body(exceptionToString(exception));
      exception.printStackTrace();
    });

    Spark.exception(NetworkException.class, (exception, req, res) -> {
      NetworkException e = (NetworkException) exception;
      res.status(e.getStatusCode());
      res.body(exceptionToString(exception));
      exception.printStackTrace();
    });

    Spark.exception(Exception.class, (exception, req, res) -> {
      res.status(500);
      res.body(exceptionToString(exception));
      exception.printStackTrace();
    });

    Spark.after((request, response) -> {
      response.header("Content-Encoding", "gzip");
      response.header("Content-Type", "application/json");
    });
  }

  private static String exceptionToString(Exception e) {
    try {
      return OM.writeValueAsString(new ErrorMessage(e.getMessage()));
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
