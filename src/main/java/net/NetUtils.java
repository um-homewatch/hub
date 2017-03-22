package net;

import exceptions.NetworkException;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NetUtils {
  private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

  private NetUtils() {
  }

  public static JsonResponse put(HttpUrl url, JSONObject jsonBody) throws NetworkException {
    return internalPut(url, jsonBody, HTTP_CLIENT);
  }

  public static JsonResponse put(HttpUrl url, JSONObject jsonBody, int connectionTimeout, int socketTimeout) throws NetworkException {
    return internalPut(url, jsonBody, timeoutHttpClient(connectionTimeout, socketTimeout));
  }

  public static JsonResponse get(HttpUrl url) throws NetworkException {
    return internalGet(url, HTTP_CLIENT);
  }

  public static JsonResponse get(HttpUrl url, int connectionTimeout, int socketTimeout) throws NetworkException {
    return internalGet(url, timeoutHttpClient(connectionTimeout, socketTimeout));
  }

  private static OkHttpClient timeoutHttpClient(int connectionTimeout, int socketTimeout) {
    return new OkHttpClient.Builder()
            .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(socketTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(socketTimeout, TimeUnit.MILLISECONDS)
            .build();
  }

  private static JsonResponse internalPut(HttpUrl url, JSONObject jsonBody, OkHttpClient httpClient) throws NetworkException {
    try {
      RequestBody body = RequestBody.create(MediaTypes.JSON, jsonBody.toString());

      Request request = new Request.Builder().url(url).put(body).build();

      Response response = httpClient.newCall(request).execute();

      analyzeStatusCode(response);

      return new JsonResponse(response);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  private static JsonResponse internalGet(HttpUrl url, OkHttpClient httpClient) throws NetworkException {
    try {
      Request request = new Request.Builder().url(url).get().build();

      Response response = httpClient.newCall(request).execute();

      analyzeStatusCode(response);

      return new JsonResponse(response);
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
