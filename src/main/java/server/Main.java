package server;

/**
 * Created by joses on 03/03/2017.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.SAXException;
import server.controllers.LightController;
import server.controllers.LockController;
import spark.Spark;
import things.DiscoveryService;
import things.Thing;
import things.lights.RestLight;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws IOException, SAXException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Thing> things = new HashMap<>();

    Spark.get("/discover", (req, res) -> {
      DiscoveryService discoveryService = new DiscoveryService(RestLight.class);

      return objectMapper.writeValueAsString(discoveryService.discovery());
    });

    Spark.get("/lights", LightController::get);
    Spark.put("/lights", LightController::put);

    Spark.get("/locks", LockController::get);
    Spark.put("/locks", LockController::put);


    Spark.exception(Exception.class, (exception, req, res) -> {
      res.status(500);
      exception.printStackTrace();
    });
  }
}
