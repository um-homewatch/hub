package homewatch.net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;
import okhttp3.Response;

import java.io.IOException;

public class JsonResponse {
  private static final ObjectMapper OM = JsonUtils.getOM();

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
