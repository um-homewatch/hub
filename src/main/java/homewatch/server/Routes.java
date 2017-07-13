package homewatch.server;

import homewatch.constants.LoggerUtils;
import homewatch.server.controllers.DiscoveryController;
import homewatch.server.controllers.NgrokController;
import homewatch.server.controllers.ThingController;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;
import spark.Spark;

class Routes {
  private Routes() {
  }

  public static void perform() {
    Spark.get("/tunnel", NgrokController::get);
    discoveryControllers();
    deviceControllers();
  }

  @SuppressWarnings("unchecked")
  private static void discoveryControllers() {
    ClassDiscoverer.getThings().forEach(klass -> {
      try {
        Thing t = klass.newInstance();
        ThingServiceFactory thingServiceFactory = t.getFactory();

        if (thingServiceFactory instanceof NetworkThingServiceFactory) {
          NetworkThingServiceFactory networkThingServiceFactory = (NetworkThingServiceFactory) t.getFactory();
          Spark.get("/devices/" + t.getStringRepresentation() + "/discover", new DiscoveryController<>(networkThingServiceFactory)::get);
        }
      } catch (InstantiationException | IllegalAccessException e) {
        LoggerUtils.logException(e);
      }
    });
  }

  @SuppressWarnings("unchecked")
  private static void deviceControllers() {
    ClassDiscoverer.getThings().forEach(klass -> {
      try {
        Thing thing = klass.newInstance();
        ThingController controller = new ThingController(thing.getFactory(), klass);

        Spark.get("/devices/" + thing.getStringRepresentation(), controller::get);
        Spark.put("/devices/" + thing.getStringRepresentation(), controller::put);
      } catch (InstantiationException | IllegalAccessException e) {
        LoggerUtils.logException(e);
      }
    });
  }
}
