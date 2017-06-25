package homewatch.net;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import okhttp3.Response;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;

import java.io.IOException;

public class CoapUtils {
  public static JsonResponse get(String url) throws NetworkException {
    try {
      CoapClient coapClient = new CoapClient(url);
      CoapResponse response = coapClient.get();

      String responseText = response.getResponseText();
      JsonNode responseJson = JsonUtils.getOM().readTree(responseText);

      return new JsonResponse(responseJson, response.getCode().value);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  public static JsonResponse put(String url, JSONObject jsonBody) throws NetworkException {
    try {
      CoapClient coapClient = new CoapClient(url);
      //50 is the equivalent of application/json
      CoapResponse response = coapClient.put(jsonBody.toString(), 50);

      String responseText = response.getResponseText();
      JsonNode responseJson = JsonUtils.getOM().readTree(responseText);

      return new JsonResponse(responseJson, response.getCode().value);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  private static void analyzeStatusCode(Response response) throws NetworkException, IOException {
    int statusCode = response.code();

    if (statusCode != 200) {
      throw new NetworkException(response.body().string(), statusCode);
    }
  }
}
