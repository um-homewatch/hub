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
import homewatch.things.motionsensors.MotionSensor;
import homewatch.things.motionsensors.MotionSensorServiceFactory;
import homewatch.things.thermostat.Thermostat;
import homewatch.things.thermostat.ThermostatServiceFactory;
import homewatch.things.weather.Weather;
import homewatch.things.weather.WeatherServiceFactory;
import org.xml.sax.SAXException;
import spark.Response;
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
    ThingController<MotionSensor> motionsensorController = new ThingController<>(new ServiceHelper<>(new MotionSensorServiceFactory()), MotionSensor.class);

    Spark.get("/lights", lightsController::get);
    Spark.put("/lights", lightsController::put);

    Spark.get("/locks", locksController::get);
    Spark.put("/locks", locksController::put);

    Spark.get("/weather", weatherController::get);

    Spark.get("/thermostats", thermostatsController::get);
    Spark.put("/thermostats", thermostatsController::put);

    Spark.get("/motionsensors", motionsensorController::get);
    Spark.put("/motionsensors", motionsensorController::put);

    Spark.get("/tunnel", NgrokController::get);
    Spark.options("/tunnel", CorsUtils::corsOptions);
    //enable cors for tunnel endpoint
    Spark.before("/tunnel", ((request, response) -> {
      response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    }));

    Spark.exception(IllegalArgumentException.class, (exception, req, res) -> resolveException(exception, res, 400));

    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> resolveException(exception, res, 400));

    Spark.exception(NetworkException.class, (exception, req, res) -> {
      NetworkException networkException = (NetworkException) exception;
      resolveException(exception, res, networkException.getStatusCode());
    });

    Spark.exception(Exception.class, (exception, req, res) -> resolveException(exception, res, 500));

    //set all responses to json format
    Spark.after((request, response) -> response.header("Content-Type", "application/json"));
  }

  private static void resolveException(Exception exception, Response res, int statusCode) {
    res.header("Content-Type", "application/json");
    res.status(statusCode);
    res.body(exceptionToString(exception));
    LoggerUtils.logException(exception);
  }

  private static String exceptionToString(Exception e) {
    try {
      return OM.writeValueAsString(new ErrorMessage(e));
    } catch (JsonProcessingException e1) {
      LoggerUtils.logException(e1);
      return null;
    }
  }
}
