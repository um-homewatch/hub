package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidSubTypeException;
import org.xml.sax.SAXException;
import server.controllers.LightController;
import server.controllers.LockController;
import server.controllers.WeatherController;
import spark.Spark;
import things.DiscoveryService;
import things.ThingService;
import things.lights.RestLight;
import things.locks.RestLock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
  private static final ObjectMapper OM = new ObjectMapper();

  public static void main(String[] args) throws IOException, SAXException {
    Map<String, ThingService> things = new HashMap<>();

    Spark.get("/lights/discover", (req, res) -> {
      DiscoveryService<RestLight> discoveryService = new DiscoveryService<>(RestLight.class);
      return OM.writeValueAsString(discoveryService.discovery());
    });

    Spark.get("/locks/discover", (req, res) -> {
      DiscoveryService<RestLock> discoveryService = new DiscoveryService<>(RestLock.class);
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
      return OM.writeValueAsString(e);
    } catch (JsonProcessingException e1) {
      return "{}";
    }
  }
}
