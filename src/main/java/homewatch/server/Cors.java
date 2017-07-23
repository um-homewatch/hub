package homewatch.server;

import spark.Request;
import spark.Response;
import spark.Spark;

class Cors {
  private Cors() {
  }

  public static void perform() {
    Spark.options("/tunnel", Cors::corsOptions);
    Spark.before("/tunnel", Cors::corsBeforeFilter);
  }

  private static String corsOptions(Request request, Response response) {
    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    if (accessControlRequestHeaders != null) {
      response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    }

    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    if (accessControlRequestMethod != null) {
      response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    }

    return "OK";
  }

  private static void corsBeforeFilter(Request request, Response response) {
    response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
    response.header("Access-Control-Allow-Origin", "*");
    response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
  }

}
