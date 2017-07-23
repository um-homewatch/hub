package homewatch.server;

import homewatch.constants.LoggerUtils;
import homewatch.server.controllers.DiscoveryController;
import homewatch.server.controllers.NgrokController;
import homewatch.server.controllers.ThingController;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;
import spark.Spark;

@SuppressWarnings("unchecked")
class Routes {
  private Routes() {
  }

  public static void perform() {
    discoveryControllers();
    deviceControllers();

    Spark.get("/tunnel", NgrokController::get);
    Spark.options("/tunnel", CorsUtils::corsOptions);
    Spark.before("/tunnel", CorsUtils::corsBeforeFilter);
  }

  private static void discoveryControllers() {
    ClassDiscoverer.getThings().forEach(klass -> {
      try {
        Thing t = klass.newInstance();
        ThingServiceFactory thingServiceFactory = t.getFactory();

        if (thingServiceFactory instanceof NetworkThingServiceFactory) {
          NetworkThingServiceFactory networkThingServiceFactory = (NetworkThingServiceFactory) t.getFactory();
          DiscoveryController discoveryController = new DiscoveryController<>(networkThingServiceFactory);
          Spark.get("/devices/" + t.getStringRepresentation() + "/discover", discoveryController::get);
        }
      } catch (InstantiationException | IllegalAccessException e) {
        LoggerUtils.logException(e);
      }
    });
  }

  private static void deviceControllers() {
    ClassDiscoverer.getThings().forEach(klass -> {
      try {
        Thing t = klass.newInstance();
        ThingServiceFactory thingServiceFactory = t.getFactory();
        ThingController controller = new ThingController(thingServiceFactory, klass);

        Spark.get("/devices/" + t.getStringRepresentation(), controller::get);
        Spark.put("/devices/" + t.getStringRepresentation(), controller::put);
      } catch (InstantiationException | IllegalAccessException e) {
        LoggerUtils.logException(e);
      }
    });
  }
}
