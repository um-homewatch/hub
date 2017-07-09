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
    discoveryControllers();
    deviceControllers();

    Spark.get("/tunnel", NgrokController::get);
    Spark.options("/tunnel", CorsUtils::corsOptions);

    //enable cors for tunnel endpoint
    Spark.before("/tunnel", ((request, response) -> {
      response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    }));
  }

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
