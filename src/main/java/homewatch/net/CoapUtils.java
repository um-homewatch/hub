package homewatch.net;

import homewatch.exceptions.NetworkException;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

public class CoapUtils {
  public static final int JSON_FORMAT = 50;

  private CoapUtils() {
  }

  public static ThingResponse get(String url) throws NetworkException {
    CoapClient coapClient = new CoapClient(url);

    CoapResponse response = coapClient.get();

    analyzeResponseSuccess(response);

    return new ThingResponse(response.getPayload(), response.getCode().value);
  }

  public static ThingResponse put(String url, byte[] payload, int format) throws NetworkException {
    CoapClient coapClient = new CoapClient(url);

    CoapResponse response = coapClient.put(payload, format);

    analyzeResponseSuccess(response);

    return new ThingResponse(response.getPayload(), response.getCode().value);
  }

  private static void analyzeResponseSuccess(CoapResponse response) throws NetworkException {
    if (response == null) {
      throw new NetworkException("Connection timeout", 404);
    } else if (!response.isSuccess()) {
      throw new NetworkException(response.getResponseText(), response.getCode().value);
    }
  }
}
