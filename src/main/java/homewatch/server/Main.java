package homewatch.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.LockController;
import homewatch.server.controllers.NgrokController;
import homewatch.server.controllers.ThermostatController;
import homewatch.server.controllers.WeatherController;
import homewatch.server.controllers.light.LightController;
import homewatch.things.DiscoveryService;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import org.xml.sax.SAXException;
import spark.Spark;

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

    Spark.get("/thermostats", ThermostatController::get);
    Spark.put("/thermostats", ThermostatController::put);

    Spark.get("/tunnel", NgrokController::get);


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

    Spark.after((request, response) -> response.header("Content-Type", "application/json"));
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
