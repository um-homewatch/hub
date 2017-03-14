package net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.MediaTypes;
import exceptions.NetworkException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

public class NetUtils {
  private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
  private static final ObjectMapper OM = new ObjectMapper();

  public static String put(HttpUrl url, JSONObject jsonBody) throws NetworkException {
    try {
      RequestBody body = RequestBody.create(MediaTypes.JSON, jsonBody.toString());

      Request request = new Request.Builder().url(url).put(body).build();

      return HTTP_CLIENT.newCall(request).execute().body().string();
    } catch (IOException e) {
      throw new NetworkException("could not reach " + url);
    }
  }

  public static JsonNode get(HttpUrl url) throws NetworkException {
    try {
      Request request = new Request.Builder().url(url).get().build();

      String response =  HTTP_CLIENT.newCall(request).execute().body().string();

      return OM.readTree(response);
    } catch (IOException e) {
      throw new NetworkException("could not reach " + url);
    }
  }
}
