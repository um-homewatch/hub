package homewatch.server;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import homewatch.constants.LoggerUtils;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class TokenSecurity {
  public static void perform() {
    try {
      String token = Files.toString(new File("./token"), Charsets.UTF_8).trim();

      Spark.before((request, response) -> {
        String requestToken = request.headers("TunnelAuthorization");

        if (requestToken == null || !requestToken.trim().equals(token))
          Spark.halt(401, "{ \"message\": \"Bad tunnel token!\"}");
      });
    } catch (IOException e) {
      LoggerUtils.logInfo("No token detected! Running on insecure mode");
    }
  }
}
