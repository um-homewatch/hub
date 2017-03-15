package net;

import constants.MediaTypes;
import exceptions.NetworkException;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class NetUtils {
  private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

  public static JsonResponse put(HttpUrl url, JSONObject jsonBody) throws NetworkException {
    try {
      RequestBody body = RequestBody.create(MediaTypes.JSON, jsonBody.toString());

      Request request = new Request.Builder().url(url).put(body).build();

      Response response = HTTP_CLIENT.newCall(request).execute();

      return new JsonResponse(response);
    } catch (IOException e) {
      throw new NetworkException(e.getMessage());
    }
  }

  public static JsonResponse get(HttpUrl url) throws NetworkException {
    try {
      Request request = new Request.Builder().url(url).get().build();

      Response response = HTTP_CLIENT.newCall(request).execute();

      return new JsonResponse(response);
    } catch (IOException e) {
      throw new NetworkException(e.getMessage());
    }
  }
}
