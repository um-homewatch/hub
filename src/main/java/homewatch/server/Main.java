package homewatch.server;

import spark.Spark;

public class Main {
  private Main() {
  }

  public static void main(String[] args) {
    Spark.before((request, response) -> response.header("Content-Type", "application/json"));
    TokenSecurity.perform();
    ExceptionHandling.perform();
    Cors.perform();
    Routes.perform();
  }
}
