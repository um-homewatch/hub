package homewatch.server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class NgrokController {
  private NgrokController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    try {
      ThingResponse thingResponse = HttpUtils.get("http://localhost:4040/api/tunnels/homewatch-hub");

      return convertResponse(thingResponse);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  private static String convertResponse(ThingResponse thingResponse) throws IOException {
    JSONObject json = new JSONObject();

    JsonNode response = JsonUtils.getOM().readTree(thingResponse.getPayload());

    String url = response.get("public_url").asText();

    return json.put("url", url).toString();
  }
}
