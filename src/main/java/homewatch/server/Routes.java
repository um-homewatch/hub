package homewatch.server;

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
import spark.Spark;

public class Routes {
  public static void perform(){
    Spark.get("/devices/lights/discover", new DiscoveryController<>(new LightServiceFactory())::get);

    Spark.get("/devices/locks/discover", new DiscoveryController<>(new LockServiceFactory())::get);

    ThingController<Light> lightsController = new ThingController<>(new ServiceHelper<>(new LightServiceFactory()), Light.class);
    ThingController<Lock> locksController = new ThingController<>(new ServiceHelper<>(new LockServiceFactory()), Lock.class);
    ThingController<Weather> weatherController = new ThingController<>(new ServiceHelper<>(new WeatherServiceFactory()), Weather.class);
    ThingController<Thermostat> thermostatsController = new ThingController<>(new ServiceHelper<>(new ThermostatServiceFactory()), Thermostat.class);
    ThingController<MotionSensor> motionsensorController = new ThingController<>(new ServiceHelper<>(new MotionSensorServiceFactory()), MotionSensor.class);

    Spark.get("/devices/lights", lightsController::get);
    Spark.put("/devices/lights", lightsController::put);

    Spark.get("/devices/locks", locksController::get);
    Spark.put("/devices/locks", locksController::put);

    Spark.get("/devices/weather", weatherController::get);

    Spark.get("/devices/thermostats", thermostatsController::get);
    Spark.put("/devices/thermostats", thermostatsController::put);

    Spark.get("/devices/motionsensors", motionsensorController::get);

    Spark.get("/tunnel", NgrokController::get);
    Spark.options("/tunnel", CorsUtils::corsOptions);
    //enable cors for tunnel endpoint
    Spark.before("/tunnel", ((request, response) -> {
      response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    }));
  }
}
