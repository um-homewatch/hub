package homewatch.net;

import com.google.common.io.ByteStreams;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import homewatch.exceptions.NetworkException;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class HttpUtils {
  private static final MediaType MEDIATYPE_JSON = MediaType.APPLICATION_JSON_TYPE;

  private HttpUtils() {
  }

  public static ThingResponse put(String url, JSONObject jsonBody) throws NetworkException {
    Client client = new Client();

    return internalPut(url, jsonBody, new Client());
  }

  public static ThingResponse put(String url, JSONObject jsonBody, int connectionTimeout, int socketTimeout) throws NetworkException {
    return internalPut(url, jsonBody, timeoutHttpClient(connectionTimeout, socketTimeout));
  }

  public static ThingResponse get(String url) throws NetworkException {
    return internalGet(url, new Client());
  }

  public static ThingResponse get(String url, int connectionTimeout, int socketTimeout) throws NetworkException {
    return internalGet(url, timeoutHttpClient(connectionTimeout, socketTimeout));
  }

  private static Client timeoutHttpClient(int connectionTimeout, int socketTimeout) {
    Client client = Client.create();
    client.setConnectTimeout(connectionTimeout);
    client.setConnectTimeout(socketTimeout);

    return client;
  }

  private static ThingResponse internalPut(String url, JSONObject jsonBody, Client httpClient) throws NetworkException {
    try {
      WebResource webResource = httpClient.resource(url);

      ClientResponse response = webResource.accept(MEDIATYPE_JSON).put(ClientResponse.class, jsonBody.toString());

      return analyzeStatusCode(response);
    } catch (UnknownHostException | ConnectException e) {
      throw new NetworkException(e, 404);
    } catch (IOException | ClientHandlerException e) {
      int statusCode = (e.getCause() instanceof UnknownHostException) ? 404 : 500;

      throw new NetworkException(e, exceptionStatus(e));
    }
  }

  private static ThingResponse internalGet(String url, Client httpClient) throws NetworkException {
    try {
      WebResource webResource = httpClient.resource(url);

      ClientResponse response = webResource.accept(MEDIATYPE_JSON).get(ClientResponse.class);

      return analyzeStatusCode(response);
    } catch (UnknownHostException | ConnectException e) {
      throw new NetworkException(e, 404);
    } catch (IOException | ClientHandlerException e) {
      throw new NetworkException(e, exceptionStatus(e));
    }
  }

  private static ThingResponse analyzeStatusCode(ClientResponse response) throws NetworkException, IOException {
    int statusCode = response.getStatus();

    if (statusCode != 200) {
      throw new NetworkException(response.getEntity(String.class), statusCode);
    }

    byte[] responseBody = ByteStreams.toByteArray(response.getEntityInputStream());

    return new ThingResponse(responseBody, statusCode);
  }

  private static int exceptionStatus(Exception e) {
    return (e.getCause() instanceof UnknownHostException) ? 404 : 500;
  }
}
