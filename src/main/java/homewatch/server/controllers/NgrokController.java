package homewatch.server.controllers;

import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.JsonResponse;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class NgrokController {
  private NgrokController() {
  }

  public static String get(Request req, Response res) throws NetworkException {
    JsonResponse jsonResponse = HttpUtils.get(HttpUrl.parse("http://localhost:4040/api/tunnels/homewatch-hub"));

    return convertResponse(jsonResponse);
  }

  private static String convertResponse(JsonResponse jsonResponse) {
    JSONObject json = new JSONObject();

    String url = jsonResponse.getJson().get("public_url").asText();

    return json.put("url", url).toString();
  }
}
