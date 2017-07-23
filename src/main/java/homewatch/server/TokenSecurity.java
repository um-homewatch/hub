package homewatch.server;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import homewatch.constants.LoggerUtils;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

class TokenSecurity {
  static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static File TOKEN_FILE = new File("./token");
  private static String TOKEN;

  private TokenSecurity() {
  }

  public static void perform() {
    secureRoutes();
    readTokenFromFS();
    setupTokenCreationRoute();
  }

  private static void readTokenFromFS() {
    try {
      TOKEN = Files.toString(TOKEN_FILE, Charsets.UTF_8);
    } catch (IOException e) {
      LoggerUtils.logInfo("Couldn't detect a secure token! Hub unclaimed");
    }
  }

  private static void setupTokenCreationRoute() {
    Spark.get("/token", (request, response) -> {
      TOKEN = generateRandomToken(1024);
      Files.write(TOKEN.getBytes(), TOKEN_FILE);

      return TOKEN;
    });
  }

  private static void secureRoutes() {
    Spark.before("/devices/*", (request, response) -> {
      String requestToken = request.headers("Authorization");

      if (TOKEN != null && isInvalidToken(requestToken))
        Spark.halt(401, "{ \"message\": \"Bad tunnel token!\"}");
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
