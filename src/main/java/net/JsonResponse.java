package net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;

public class JsonResponse {
  private static final ObjectMapper OM = new ObjectMapper();

  private final JsonNode json;
  private final Response response;

  JsonResponse(Response response) throws IOException {
    this.response = response;
    this.json = OM.readTree(response.body().string());
  }

  public JsonNode getJson() {
    return json;
  }

  public Response getResponse() {
    return response;
  }
}
