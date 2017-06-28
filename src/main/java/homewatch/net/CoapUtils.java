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
  public static int JSON_FORMAT = 50;

  public static ThingResponse get(String url) throws NetworkException {
    CoapClient coapClient = new CoapClient(url);
    CoapResponse response = coapClient.get();

    return new ThingResponse(response.getPayload(), response.getCode().value);
  }

  public static ThingResponse put(String url, byte[] payload, int format) throws NetworkException {
    CoapClient coapClient = new CoapClient(url);

    CoapResponse response = coapClient.put(payload, format);

    return new ThingResponse(response.getPayload(), response.getCode().value);
  }

  private static void analyzeStatusCode(Response response) throws NetworkException, IOException {
    int statusCode = response.code();

    if (statusCode != 200) {
      throw new NetworkException(response.body().string(), statusCode);
    }
  }
}
