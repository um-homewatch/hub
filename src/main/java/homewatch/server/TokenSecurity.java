package homewatch.server;

import spark.Spark;

import java.security.SecureRandom;

class TokenSecurity {
  private static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static String TOKEN;

  private TokenSecurity() {
  }

  public static void perform() {
    secureRoutes();
    setupTokenCreationRoute();
  }

  private static void setupTokenCreationRoute() {
    Spark.get("/token", (request, response) -> {
      TOKEN = generateRandomToken(1024);

      return TOKEN;
    });
  }

  private static void secureRoutes() {
    Spark.before("/devices/*", (request, response) -> {
      String requestToken = request.headers("Authorization");

      if (TOKEN != null && isInvalidToken(requestToken))
        throw Spark.halt(401, "{ \"message\": \"Bad tunnel token!\"}");
    });
  }

  private static boolean isInvalidToken(String requestToken) {
    return requestToken == null || !requestToken.trim().equals(TOKEN);
  }

  private static String generateRandomToken(int len) {
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder stringBuilder = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      stringBuilder.append(ALLOWED_CHARS.charAt(secureRandom.nextInt(ALLOWED_CHARS.length())));
    return stringBuilder.toString();
  }
}
