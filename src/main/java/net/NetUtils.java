package net;

import constants.LoggerUtils;
import exceptions.NetworkException;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class NetUtils {
  private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

  private NetUtils() {
  }

  public static void put(HttpUrl url, JSONObject jsonBody) throws NetworkException {
    try {
      RequestBody body = RequestBody.create(MediaTypes.JSON, jsonBody.toString());

      Request request = new Request.Builder().url(url).put(body).build();

      Response response = HTTP_CLIENT.newCall(request).execute();

      analyzeStatusCode(response);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  public static JsonResponse get(HttpUrl url) throws NetworkException {
    try {
      Request request = new Request.Builder().url(url).get().build();

      Response response = HTTP_CLIENT.newCall(request).execute();

      analyzeStatusCode(response);

      return new JsonResponse(response);
    } catch (IOException e) {
      LoggerUtils.logException(e);
      throw new NetworkException(e.getMessage(), 500);
    }
  }

  private static void analyzeStatusCode(Response response) throws NetworkException, IOException {
    int statusCode = response.code();

    if (statusCode != 200) {
      throw new NetworkException(response.body().string(), statusCode);
    }
  }
}
