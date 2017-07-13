package homewatch.server;

import spark.Spark;

class Cors {
  private Cors() {
  }

  public static void perform() {
    Spark.before("/tunnel", ((request, response) -> {
      response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    }));
  }
}
