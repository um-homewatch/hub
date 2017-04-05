package homewatch.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.server.controllers.DiscoveryController;
import homewatch.server.controllers.NgrokController;
import homewatch.server.controllers.ServiceHelper;
import homewatch.server.controllers.ThingController;
import homewatch.things.lights.Light;
import homewatch.things.lights.LightServiceFactory;
import homewatch.things.locks.Lock;
import homewatch.things.locks.LockServiceFactory;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;
import org.xml.sax.SAXException;
import spark.Spark;

import java.io.IOException;

public class Main {
  private static final ObjectMapper OM = JsonUtils.getOM();

  private Main() {
  }

  public static void main(String[] args) throws IOException, SAXException {
    Spark.get("/lights/discover", new DiscoveryController<>(new LightServiceFactory())::get);

    Spark.get("/locks/discover", new DiscoveryController<>(new LockServiceFactory())::get);

    ThingController<Light> lightsController = new ThingController<>(new ServiceHelper<>(new LightServiceFactory()), Light.class);
    ThingController<Lock> locksController = new ThingController<>(new ServiceHelper<>(new LockServiceFactory()), Lock.class);
    ThingController<Weather> weatherController = new ThingController<>(new ServiceHelper<>(new WeatherServiceFactory()), Weather.class);
    ThingController<Thermostat> thermostatsController = new ThingController<>(new ServiceHelper<>(new ThermostatServiceFactory()), Thermostat.class);

    Spark.get("/lights", lightsController::get);
    Spark.put("/lights", lightsController::put);

    Spark.get("/locks", locksController::get);
    Spark.put("/locks", locksController::put);

    Spark.get("/weather", weatherController::get);

    Spark.get("/thermostats", thermostatsController::get);
    Spark.put("/thermostats", thermostatsController::put);

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
