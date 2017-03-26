package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.JsonUtils;
import exceptions.NetworkException;
import net.JsonResponse;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class NgrokController {
  private NgrokController(){
  }

  public static String get(Request req, Response res) throws NetworkException {
    JsonResponse jsonResponse = NetUtils.get(HttpUrl.parse("http://localhost:4040/api/tunnels/homewatch-hub"));
    
    return convertResponse(jsonResponse);
  }

  private static String convertResponse(JsonResponse jsonResponse){
    JSONObject json = new JSONObject();

    String url = jsonResponse.getJson().get("public_url").asText();

    return json.put("url", url).toString()  ;
  }
}
