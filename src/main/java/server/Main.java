package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.JsonUtils;
import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import org.xml.sax.SAXException;
import server.controllers.LightController;
import server.controllers.LockController;
import server.controllers.WeatherController;
import spark.Spark;
import things.DiscoveryService;
import things.lights.Light;
import things.lights.LightServiceFactory;
import things.locks.Lock;
import things.locks.LockServiceFactory;

import java.io.IOException;

public class Main {
  private static final ObjectMapper OM = JsonUtils.getOM();

  private Main() {
  }

  public static void main(String[] args) throws IOException, SAXException {
    Spark.get("/lights/discover", (req, res) -> {
      DiscoveryService<Light> discoveryService = new DiscoveryService<>(new LightServiceFactory(), "rest");
      return OM.writeValueAsString(discoveryService.discovery());
    });

    Spark.get("/locks/discover", (req, res) -> {
      DiscoveryService<Lock> discoveryService = new DiscoveryService<>(new LockServiceFactory(), "rest");
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
      LoggerUtils.logException(exception);
    });

    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> {
      res.status(400);
      res.body(exceptionToString(exception));
      LoggerUtils.logException(exception);
    });

    Spark.exception(NetworkException.class, (exception, req, res) -> {
      NetworkException e = (NetworkException) exception;
      res.status(e.getStatusCode());
      res.body(exceptionToString(exception));
      LoggerUtils.logException(exception);
    });

    Spark.exception(Exception.class, (exception, req, res) -> {
      res.status(500);
      res.body(exceptionToString(exception));
      LoggerUtils.logException(exception);
    });

    Spark.after((request, response) -> {
      response.header("Content-Type", "application/json");
    });
  }

  private static String exceptionToString(Exception e) {
    try {
      return OM.writeValueAsString(new ErrorMessage(e.getMessage()));
    } catch (JsonProcessingException e1) {
      LoggerUtils.logException(e1);
      return null;
    }
  }
}
